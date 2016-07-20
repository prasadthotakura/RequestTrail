package org.kira.requesttrail.context;

import org.kira.requesttrail.logging.LoggingContext;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class RequestTrailContext
{
    private String correlationId;
    private String correlationHeader;

    private RequestCallStack requestStack;
    private LoggingContext loggingContext;

    public RequestTrailContext()
    {
    }

    public RequestTrailContext(String correlationHeader, String correlationId, LoggingContext loggingContext, RequestCallStack callerContext)
    {
        this.correlationId = correlationId;
        this.correlationHeader = correlationHeader;
        this.loggingContext = loggingContext;
        this.requestStack = callerContext;
    }

    public RequestCallStack getRequestStack()
    {
        return requestStack;
    }

    public void setRequestStack(RequestCallStack requestStack)
    {
        this.requestStack = requestStack;
    }

    public String getCorrelationHeader()
    {
        return correlationHeader;
    }

    public void setCorrelationHeader(String correlationHeader)
    {
        this.correlationHeader = correlationHeader;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }

    public LoggingContext getLoggingContext()
    {
        return loggingContext;
    }

    public void setLoggingContext(LoggingContext loggingContext)
    {
        this.loggingContext = loggingContext;
    }
}
