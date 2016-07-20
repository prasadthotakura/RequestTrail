package org.kira.requesttrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;


/**
 * @license Request Trail Sample Application v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class RequestTrailSampleApplication extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(RequestTrailSampleApplication.class, WebConfig.class);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RequestTrailSampleApplication.class, args);
    }

}