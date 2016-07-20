package org.kira.requesttrail.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.kira.requesttrail.context.RequestTrailContext;
import org.kira.requesttrail.context.RequestTrailContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

import java.text.MessageFormat;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

@Aspect
public class RequestTrailAspect
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrailAspect.class);

    @Before("execution(* org.springframework.web.client.RequestCallback.doWithRequest(..)")
    public void addCorrelationHeader(JoinPoint point)
    {
        ClientHttpRequest request = (ClientHttpRequest) point.getArgs()[0];
        RequestTrailContext correlationCtx = RequestTrailContextHolder.getRequestCorrelationContext();
        LOGGER.info(MessageFormat.format("Adding correlation header {0}={1} to outgoing request {2}", correlationCtx.getCorrelationHeader(), correlationCtx.getCorrelationId(), request.getURI().getPath()));
        HttpHeaders headers = request.getHeaders();
        headers.add(correlationCtx.getCorrelationHeader(), correlationCtx.getCorrelationId());
        LOGGER.info(MessageFormat.format(" Request CallStack header {0}:{1}", correlationCtx.getRequestStack().getHeader(), String.valueOf(correlationCtx.getRequestStack())));
        headers.add(correlationCtx.getRequestStack().getHeader(), String.valueOf(correlationCtx.getRequestStack()));
            }
}
