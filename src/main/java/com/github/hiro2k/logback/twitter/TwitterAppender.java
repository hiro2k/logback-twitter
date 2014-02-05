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

import ch.qos.logback.core.AppenderBase;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Hugo Garza <hiro2k@gmail.com>
 */
public class TwitterAppender<E> extends AppenderBase {

    ConfigurationBuilder cb = new ConfigurationBuilder();

    @Override
    protected void append(Object e) {        
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
