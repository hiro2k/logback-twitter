/*
 * Copyright 2014 Hugo Garza <hiro2k@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hiro2k.logback.twitter;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.status.ErrorStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Logback {@link Appender} to write log data to Twitter
 * <p>
 * To configure, add an appender element referring to this class in your {@code
 * logback.xml} file. The following properties must be set:
 * <ul>
 * <li>consumerKey: Twitter Consumer key
 * <li>consumerSecret: Twitter Consumer secret
 * <li>accessToken: Twitter Access token
 * <li>accessSecret: Twitter Access token secret
 * </ul>
 * The easiest way to obtain these 4 values is to create an application at:
 * {@link https://dev.twitter.com/}
 *
 * @author Hugo Garza <hiro2k@gmail.com>
 */
public class TwitterAppender extends AppenderBase<ILoggingEvent> {

    private static final String DEFAULT_PATTERN = "%logger{0} - %m%n%ex{short}";
    // required properties
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;
    // optional properties
    private Layout<ILoggingEvent> layout;
    private Twitter twitter;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    /**
     * Sets the Twitter client to use, it's package private to facilitate
     * testing.
     *
     * @param twitter
     */
    void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void start() {
        boolean requiredPropsSet = true;
        if (accessToken == null) {
            addStatus(new ErrorStatus("Access token not set", this));
            requiredPropsSet = false;
        }
        if (accessSecret == null) {
            addStatus(new ErrorStatus("Access secret not set", this));
            requiredPropsSet = false;
        }
        if (consumerKey == null) {
            addStatus(new ErrorStatus("Consumer key not set", this));
            requiredPropsSet = false;
        }
        if (consumerSecret == null) {
            addStatus(new ErrorStatus("Consumer Secret not set", this));
            requiredPropsSet = false;
        }
        if (!requiredPropsSet && twitter == null) {
            addWarn("Appender not started because the required properties weren't all set.");
            return;
        }
        if (layout == null) {
            layout = makeDefaultPatternLayout();
        }
        if (twitter == null) {
            ConfigurationBuilder configBuilder = new ConfigurationBuilder();
            configBuilder.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessSecret);
            TwitterFactory tf = new TwitterFactory(configBuilder.build());
            this.twitter = tf.getInstance();
        }
        super.start();
    }

    /**
     * Creates the default PatternLayout in case the user doesn't supply one.
     *
     * @return the PatternLayout with pattern DEFAULT_PATTERN
     */
    protected PatternLayout makeDefaultPatternLayout() {
        PatternLayout pl = new PatternLayout();
        pl.setPattern(DEFAULT_PATTERN);
        return pl;
    }

    /**
     * Updates the status message of the twitter account with the eventObject
     * formatted as defined by layout.
     *
     * @param eventObject the event to be posted on twitter.
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        if (layout == null || twitter == null) {
            addError("layout or twitter are not set.");
            return;
        }
        String message = layout.doLayout(eventObject);
        try {
            twitter.updateStatus(message);
        } catch (TwitterException ex) {
            addWarn("Message was not posted to twitter.", ex);
        }
    }
}
