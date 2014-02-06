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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.ContextBase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Hugo Garza <hiro2k@gmail.com>
 */
public class TwitterAppenderTest {

    Context context = new ContextBase();

    public TwitterAppenderTest() {
    }

    @Before
    public void setTwitterLogger() {
        // The twitter API finds SLF4J on our path and tries to use it, but that screws everything up.
        System.setProperty("twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory");
    }

    /**
     * Test of start method, of class TwitterAppender. This shouldn't start
     * because none of the required twitter keys are set.
     */
    @Test
    public void testNoStart() {
        System.out.println("no start");
        TwitterAppender instance = mock(TwitterAppender.class);
        doCallRealMethod().when(instance).start();
        instance.start();
        verify(instance, never()).makeDefaultPatternLayout();
    }

    /**
     * Test of start method, of class TwitterAppender. This should start because
     * all of the required twitter keys are set.
     */
    @Test
    public void testStartProperties() {
        System.out.println("start properties");
        // Spy is easier than callReal
        TwitterAppender instance = spy(new TwitterAppender());
        instance.setContext(context);
        instance.setAccessSecret("");
        instance.setAccessToken("");
        instance.setConsumerKey("");
        instance.setConsumerSecret("");
        instance.start();
        verify(instance, times(1)).makeDefaultPatternLayout();
    }

    /**
     * Test of start method, of class TwitterAppender. This should start because
     * the twitter client is mocked.
     */
    @Test
    public void testStartTwitter() {
        System.out.println("start twitter");
        TwitterAppender instance = spy(new TwitterAppender());
        instance.setTwitter(mock(Twitter.class));
        doCallRealMethod().when(instance).start();
        instance.start();
        verify(instance, times(1)).makeDefaultPatternLayout();
    }

    /**
     * Test of append method, of class TwitterAppender. This should log an error
     * because start was not called so twitter and the layout are null.
     */
    @Test
    public void testAppendNotStarted() {
        System.out.println("append not started");
        // LoggingEventVO is an empty event, useful for testing.
        ILoggingEvent eventObject = new LoggingEventVO();
        TwitterAppender instance = mock(TwitterAppender.class);
        instance.setContext(context);
        doCallRealMethod().when(instance).append(eventObject);
        instance.append(eventObject);
        verify(instance, times(1)).addError("layout or twitter are not set.");
    }

    /**
     * Test of append method, of class TwitterAppender. This should pass because
     * start was called and twitter was set.
     */
    @Test
    public void testAppendStarted() throws TwitterException {
        System.out.println("append");
        Twitter twitter = mock(Twitter.class);
        // LoggingEventVO is an empty event, useful for testing.
        ILoggingEvent eventObject = new LoggingEventVO();
        TwitterAppender instance = new TwitterAppender();
        instance.setTwitter(twitter);
        instance.setContext(context);
        instance.start();
        instance.append(eventObject);
        verify(twitter, times(1)).updateStatus("");
    }

    /**
     * Test of append method, of class TwitterAppender. This should pass because
     * start was called and twitter was set.
     */
    @Test
    public void testAppendTwitterException() throws TwitterException {
        System.out.println("append exception");
        Twitter twitter = mock(Twitter.class);
        TwitterException exception = new TwitterException("test");
        doThrow(exception).when(twitter).updateStatus("");
        // LoggingEventVO is an empty event, useful for testing.
        ILoggingEvent eventObject = new LoggingEventVO();
        TwitterAppender instance = spy(new TwitterAppender());
        instance.setTwitter(twitter);
        instance.setContext(context);
        instance.start();
        instance.append(eventObject);
        verify(twitter, times(1)).updateStatus("");
        verify(instance, times(1)).addWarn("Message was not posted to twitter.", exception);
    }
}