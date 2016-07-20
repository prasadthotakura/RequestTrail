package org.kira.requesttrail.header;

import org.apache.commons.lang.Validate;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class TrailHeaderStrategy
{

    private String correlationHeader;
    private final String DEFAULT_CORRELATION_HEADER = "KIRA-CORRELATION-ID";
    private final String DEFAULT_APP_CALL_STACK_HEADER = "KIRA-APP-CALL-STACK";
    private final String DEFAULT_HEADER_STRATEGY = HeaderStrategy.HEADER.getHeader();
    private String headerStrategy;
    private String callStackHeader;

    public String getCorrelationHeader()
    {
        if (StringUtils.isEmpty(correlationHeader))
        {
            return DEFAULT_CORRELATION_HEADER;
        }
        return correlationHeader;
    }

    public void setCorrelationHeader(String correlationHeader)
    {
        this.correlationHeader = correlationHeader;
    }

    public String getHeaderStrategy()
    {
        if (StringUtils.isEmpty(headerStrategy))
        {
            return DEFAULT_HEADER_STRATEGY;
        }
        return headerStrategy;
    }

    public void setHeaderStrategy(String headerStrategy)
    {
        Validate.isTrue(HeaderStrategy.isValidHeaderStrategy(headerStrategy), MessageFormat.format("Invalid header strategy  {0} '.' "
                + " Valid values are {0}, {1} ", headerStrategy, HeaderStrategy.HEADER.getHeader(), HeaderStrategy.COOKIE.getHeader()));

        this.headerStrategy = headerStrategy;
    }

    public String getCallStackHeader()
    {
        if (StringUtils.isEmpty(callStackHeader))
        {
            return DEFAULT_APP_CALL_STACK_HEADER;
        }
        return callStackHeader;
    }

    public void setCallStackHeader(String callStackHeader)
    {
        this.callStackHeader = callStackHeader;
    }
}
