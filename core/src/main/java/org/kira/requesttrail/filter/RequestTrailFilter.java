package org.kira.requesttrail.filter;

import org.kira.requesttrail.context.RequestCallStack;
import org.kira.requesttrail.logging.ILoggingContextProvider;
import org.kira.requesttrail.context.RequestTrailContext;
import org.kira.requesttrail.context.RequestTrailContextHolder;
import org.kira.requesttrail.header.TrailHeaderStrategy;
import org.kira.requesttrail.header.HeaderStrategy;
import org.kira.requesttrail.logging.MdcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class RequestTrailFilter extends OncePerRequestFilter
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrailFilter.class);

    @Autowired
    private TrailHeaderStrategy headerStrategy;

    @Autowired
    private ILoggingContextProvider logCtxProvider;

    @Autowired
    private MdcUtils mdcUtils;

    @Override
    protected void initFilterBean() throws ServletException
    {
        LOGGER.info("Initializing RequestTrailFilter..");
        super.initFilterBean();
    }

    protected void doFilterInternal(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, javax.servlet.FilterChain filterChain) throws ServletException, IOException
    {

        String correlationId = null;
        String callerContextStr = null;
        if (HeaderStrategy.HEADER.getHeader().equals(headerStrategy.getHeaderStrategy()))
        {
            correlationId = getCorrelationId(httpServletRequest, httpServletResponse);
            callerContextStr = httpServletRequest.getHeader(headerStrategy.getCallStackHeader());
        } else if (HeaderStrategy.COOKIE.getHeader().equals(headerStrategy.getHeaderStrategy()))
        {
            correlationId = getCorrelationIdFromCookie(httpServletRequest, httpServletResponse);
            Cookie cookie = WebUtils.getCookie(httpServletRequest, headerStrategy.getCallStackHeader());
            if (cookie != null && !StringUtils.isEmpty(cookie.getValue()))
            {
                callerContextStr = cookie.getValue();
            }
        }
        RequestCallStack callerCtx;
        if (!StringUtils.isEmpty(callerContextStr))
        {
            callerCtx = new RequestCallStack(headerStrategy.getCallStackHeader(), callerContextStr);

        } else
        {
            callerCtx = new RequestCallStack(headerStrategy.getCallStackHeader());
            callerCtx.setOrigin(logCtxProvider.getLoggingContext().getApp());
        }
        callerCtx.addCaller(logCtxProvider.getLoggingContext().getApp());
        httpServletResponse.addHeader(headerStrategy.getCallStackHeader(), String.valueOf(callerCtx));
        RequestTrailContext reqCorrCtx = new RequestTrailContext(headerStrategy.getCorrelationHeader(), correlationId, logCtxProvider.getLoggingContext(), callerCtx);
        RequestTrailContextHolder.setRequestCorrelationContext(reqCorrCtx);
        mdcUtils.putRequestCorrelationConextToMdc();
        LOGGER.info(MessageFormat.format("Correlation id:{0} uri:{1} callStack: {2}", correlationId, String.valueOf(httpServletRequest.getRequestURI()), String.valueOf(callerCtx)));
        try
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        finally
        {
            mdcUtils.clearMdc();
            RequestTrailContextHolder.clear();
        }
    }

    private String getCorrelationId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        String correlationId;
        correlationId = httpServletRequest.getHeader(headerStrategy.getCorrelationHeader());
        if (StringUtils.isEmpty(correlationId))
        {
            correlationId = UUID.randomUUID().toString();
            httpServletRequest.setAttribute(headerStrategy.getCorrelationHeader(), correlationId);
        }
        httpServletResponse.setHeader(headerStrategy.getCorrelationHeader(), correlationId);
        return correlationId;
    }

    private String getCorrelationIdFromCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        String correlationId = null;
        Cookie cookie = WebUtils.getCookie(httpServletRequest, headerStrategy.getCorrelationHeader());
        if (cookie == null || StringUtils.isEmpty(cookie.getValue()))
        {
            correlationId = UUID.randomUUID().toString();
            cookie = new Cookie(headerStrategy.getCorrelationHeader(), correlationId);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }
        correlationId = cookie.getValue();
        return correlationId;
    }


    public TrailHeaderStrategy getHeaderStrategy()
    {
        return headerStrategy;
    }

    public void setHeaderStrategy(TrailHeaderStrategy headerStrategy)
    {
        this.headerStrategy = headerStrategy;
    }
}
