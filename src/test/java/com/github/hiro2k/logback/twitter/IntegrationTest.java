/*
 * Copyright 2014 Hugo Garza <hugog@opanga.com>.
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

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hugo Garza <hugog@opanga.com>
 */
public class IntegrationTest {

    @Before
    public void setTwitterLogger() {
        // The twitter API finds SLF4J on our path and tries to use it, but that screws everything up.
        System.setProperty("twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory");
    }

    @Test
    public void testRealTwitterFeed() {
        System.out.println("real twitter");
        Logger log = LoggerFactory.getLogger("test");
        log.info("Tweeting from logback suck it!");
    }
}
