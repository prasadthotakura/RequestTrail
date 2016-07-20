package org.kira.requesttrail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class RequestTrailPropertyHolder
{
    public static final String REQUEST_TRAIL_CORRELATION_HEADER="request.trail.correlation.header";
    public static final String REQUEST_TRAIL_APP_CALL_STACK_HEADER="request.trail.app.call.stack.header";
    public static final String REQUEST_TRAIL_HEADER_STRATEGY="request.trail.header.strategy";
    public static final String REQUEST_TRAIL_ASPECT_ENABLE = "request.trail.aspect.enable";

    @Autowired
    private Environment environment;


    public Environment getEnvironment()
    {
        return environment;
    }

    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    public String getProperty(String property)
    {
        return this.environment.getProperty(property);
    }
}
