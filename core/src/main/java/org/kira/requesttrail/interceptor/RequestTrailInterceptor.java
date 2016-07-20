package org.kira.requesttrail.interceptor;

import org.kira.requesttrail.context.RequestTrailContext;
import org.kira.requesttrail.context.RequestTrailContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
public class RequestTrailInterceptor implements ClientHttpRequestInterceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrailInterceptor.class);

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        RequestTrailContext correlationCtx = RequestTrailContextHolder.getRequestCorrelationContext();
        HttpHeaders headers = request.getHeaders();
        LOGGER.info(MessageFormat.format("Adding correlation header {0}={1} to outgoing request {2}", correlationCtx.getCorrelationHeader(), correlationCtx.getCorrelationId(), request.getURI().getPath()));
        headers.add(correlationCtx.getCorrelationHeader(), correlationCtx.getCorrelationId());
        LOGGER.info(MessageFormat.format(" Request CallStack header {0}:{1}", correlationCtx.getRequestStack().getHeader(), String.valueOf(correlationCtx.getRequestStack())));
        headers.add(correlationCtx.getRequestStack().getHeader(), String.valueOf(correlationCtx.getRequestStack()));
        return execution.execute(request, body);
    }
}
