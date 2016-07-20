package org.kira.requesttrail.logging;

import org.kira.requesttrail.context.RequestTrailContext;
import org.kira.requesttrail.context.RequestTrailContextHolder;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
@Component
public class MdcUtils
{


    public void putRequestCorrelationConextToMdc()
    {
        RequestTrailContext context = RequestTrailContextHolder.getRequestCorrelationContext();
        putToMdcQueitly(context.getCorrelationHeader(), context.getCorrelationId());
        putToMdcQueitly(context.getRequestStack().getHeader(), String.valueOf(context.getRequestStack()));
        LoggingContext logCtx = context.getLoggingContext();
        putToMdcQueitly("app", logCtx.getApp());
        logCtx.getParams().forEach((name, value) -> putToMdcQueitly(name, value));
    }

    public void clearMdc()
    {
        MDC.clear();
    }

    public void putToMdcQueitly(String key, String value)
    {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value))
        {
            MDC.put(key, value);
        }
    }
}
