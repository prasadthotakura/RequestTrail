package org.kira.requesttrail.context;

import org.kira.requesttrail.logging.LoggingContext;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
public class RequestTrailContextHolder
{

    private RequestTrailContext correlationContext;

    private static ThreadLocal<RequestTrailContextHolder> INSTANCE = new ThreadLocal<RequestTrailContextHolder>()
    {
        @Override
        protected RequestTrailContextHolder initialValue()
        {
            return new RequestTrailContextHolder();
        }
    };

    private RequestTrailContextHolder()
    {
        correlationContext = new RequestTrailContext();
    }

    public static void clear()
    {
        INSTANCE.remove();
    }

    public static void setRequestCorrelationId(String correlationId)
    {
        INSTANCE.get().setCorrelationId(correlationId);
    }

    public static String getRequestCorrelationId()
    {
        return INSTANCE.get().getCorrelationId();
    }


    private String getCorrelationId()
    {
        return correlationContext.getCorrelationId();
    }


    private void setCorrelationId(String correlationId)
    {
        this.correlationContext.setCorrelationId(correlationId);
    }

    public static RequestTrailContext getRequestCorrelationContext()
    {
        return INSTANCE.get().correlationContext;
    }

    public static void setRequestCorrelationContext(RequestTrailContext correlationContext)
    {
        INSTANCE.get().correlationContext = correlationContext;
    }

    public static LoggingContext getLoggingContext()
    {
        return INSTANCE.get().correlationContext.getLoggingContext();
    }

    public static void setLoggingContext(LoggingContext loggingContext)
    {
        INSTANCE.get().correlationContext.setLoggingContext(loggingContext);
    }
}
