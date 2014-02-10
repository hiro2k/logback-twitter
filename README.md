logback-twitter
===============

Logback Appender to write log data to Twitter

Usage
---------------
Go to https://dev.twitter.com/ and login. Create an application and go to API Keys,
and generate an access token.

Now you can create a normal appender using the class com.github.hiro2k.logback.twitter.TwitterAppender
and configure it with your api keys from the twitter site.

```xml
<configuration>
    <!-- dump status message on the console as they arrive -->
    <statusListener class="ch.qos.logback.core.status.OnConsoleStatusListener" />

    <appender name="TWITTER" class="com.github.hiro2k.logback.twitter.TwitterAppender">
        <consumerKey>${consumerKey}</consumerKey>
        <consumerSecret>${consumerSecret}</consumerSecret>
        <accessToken>${accessToken}</accessToken>
        <accessSecret>${accessSecret}</accessSecret>
        <layout>
            <pattern>%logger{0} - %m%n%ex{short}</pattern>
        </layout>
    </appender>


    <root level="INFO">
        <appender-ref ref="TWITTER" />
    </root>
</configuration>
```

Try to keep the layout pattern as short as possible.

Maven
---------------
Include the following repository in your projects pom.xml

```xml
<repositories>
    <repository>
        <id>logback-twitter-mvn-repo</id>
        <url>https://raw.github.com/hiro2k/logback-twitter/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

Then you can include the library as a dependency

```xml
<dependency>
    <groupId>com.github.hiro2k</groupId>
    <artifactId>logback-twitter</artifactId>
    <version>1.1</version>
</dependency>
```