# RequestTrail
Java Library that provides mechanism to correlate or track a specific request to a micro service that might translated to one or multiple internal requests to other micro services.

In the era of highly distributed web applications, typically composed of micro services. A single request may cut across multiple micro services and translated to several requests delated to other micro services. It is extremely difficult to analyze and root cause the event of failure without sufficient contextual logging info. It may get worsen in case of cloud deployed services which generates logs of few GBs every day. Even though log management services like Logstash, fluentd,splunk, papertrail etc., unless  there is mechanism to correlate the requests across the services.

Request Trail library would solve this for applications developed using Spring by providing correlation id to each request and populating it to all the requests that might be invoked to serve it. Also, it provides application stack details such as the request origination app and request traversal from app to app.

With Request Trail in place, one can search log files or log management services with correlation id and get all relevant details to that specific request.
As mentioned earlier, the solution has two parts.
#####	Generating or retrieving the correlation id to or from request and populate it back to response: 

This job is done by RequestTrailFilter. It retrieves correlation id from the header as specified with the configuration or environmental property “request.trail.correlation.header”, or from the default header “KIRA-CORRELATION-ID” if it is not provided. If the correlation header is available, it would generate the id. In both the cases correlation header would set to response.

It also builds and populates request origin and applications details through which it traverse to. This would be retrieved or set from or to header as specified with configuration or environmental property “request.trail.app.call.stack.header”, adds the current application to stack and set the header to response. If the header name is not provided to the afore mentioned property, it would process default header "KIRA-APP-CALL-STACK"
 
And, it populates logging context provided by implementation for ILoggingContextProvider to MDC which can retrieved and printed to log files later.

#####	Populating correlation id down the request chain:

It is very common that an application communicating to with other application service to fulfill the request. The correlation id generated or retrieved by RequestTrailFilter has to be attached to request to be invoked to track or correlate all the translated requests as a single unit. 

With Spring in place, this inter communication typically would happen through RestTemplate.  So the correlation id can be attached using request interceptor RequestTrailInterceptor. There is preconfigured RestTemplate bean “kiraRestTemplate” with RequestTrailInterceptor and widely used message converters available with library. This can be used  full control is available on application development by changing the existing RestTemplate usage with kiraRestTemplate.  

Otherwise, RequestTrailAspect can be used to intercept all the request that going through RestTemplate. RequestTrailAspect can be enabled by setting configuration or environmental property “request.trail.aspect.enable” to “true”. But beware of the performance overhead that can be introduced by using aspect.

## Prerequisites:
* Java 8.
* Spring 4.x

## How to use?
Please see the available sample application. The sample application is developed using Spring Boot. For XML configuration based applications, please register DelegatingFilterProxy with target bean as “requestTrailFilter” and url pattern “/\*” in web.xml. For example,
```
<filter>
    <filter-name>requestTrailFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
<filter-mapping>
    <filter-name>requestTrailFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  
  ```

Also, enable annotation driven configuration (by adding &lt;context:annotation-config/&gt;  in config xml) and component scan for package “org.kira.requesttrail”.

And, create a bean definition by implementing interface ILoggingContextProvider interface to provide application’s LoggingContext like app name, user etc. Please see SampleAppLoggingContextProviderImpl from sample application for implementation example.

## Log Configuration:
The LoggingContext (provided by ILoggingContextProvider implementation), correlation id and request origin and app call stack details put to MDC can be retrieved and printed to the log files. Corrleation Id should be retrieved using correlation header name as key, request origin and app call stack details should be retrieved with key app call stack header name, application name with key “app” and all other values from LoggingContext with same keys as they provided. 

The following sample log configuration are based on sample app where  request.trail.correlation.header is configured with header name “req-correlation-id”, request.trail.app.call.stack.header configured with header name "app-call-stack" and current logged in user name with key “user”
### Log4j configuration:
```
log4j.rootLogger=WARN, default, fileAppender
# Enable logging inside your application classes.
#log4j.logger.<replace all this with your application package name>=WARN
log4j.logger.org.kira.requesttrail=WARN
log4j.logger.org.springframework=WARN
log4j.appender.default=org.apache.log4j.ConsoleAppender
# default uses PatternLayout.
log4j.appender.default.layout=org.apache.log4j.PatternLayout
# [Timestamp] [Trace Level] [P<ID>][T<ID>] [Context ID] [Context name] Message
# [      %d ] [        %p ] [  ?? ][  %t ] [ %X       ] [ %c         ] %m%n
log4j.appender.default.layout.ConversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ} %5p [T%t] [%X{req-correlation-id}|%X{user}|%X{app}|%X{app-call-stack}] [%C:%L] %m%n
#------------------- File Appender --------------------------
log4j.appender.fileAppender=org.apache.log4j.DailyRollingFileAppender
log4j.appender.fileAppender.threshold=DEBUG
log4j.appender.fileAppender.File=logs/request-trail.log
log4j.appender.fileAppender.layout=org.apache.log4j.PatternLayout
log4j.appender.fileAppender.layout.ConversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ} %5p [T%t] [%X{req-correlation-id}|%X{user}|%X{app}|%X{app-call-stack}] [%C:%L] %m%n

```
### Logback configuration:
```XML

<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} [%X{req-correlation-id}|%X{user}|%X{app}|%X{app-call-stack}] - %msg%n</pattern>
		</encoder>
	</appender>
	<appender name="dailyRollingFileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<File>logs/request-correlation.log</File>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<FileNamePattern>request-correlation.%d{yyyy-MM-dd}.log</FileNamePattern>
			<maxHistory>30</maxHistory>
		</rollingPolicy>
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} [%X{req-correlation-id}|%X{user}|%X{app}|%X{app-call-stack}] - %msg%n</pattern>
		</encoder>
	</appender>
	<logger name="org.kira.requesttrail" level="INFO" additivity="false">
            <appender-ref ref="consoleAppender" />
            <appender-ref ref="dailyRollingFileAppender" />
	</logger>
	<root level="WARN">
		<appender-ref ref="consoleAppender" />
		<appender-ref ref="dailyRollingFileAppender" />
	</root>
</configuration>

```
