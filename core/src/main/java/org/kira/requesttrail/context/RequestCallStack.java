package org.kira.requesttrail.context;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class RequestCallStack
{
    private String header;
    private String origin;
    private String callStack;

    public RequestCallStack(String header)
    {
        this.header = header;
    }

    public RequestCallStack(String header, String caller)
    {
        this.header = header;
        Matcher matcher = Pattern.compile("^(origin=(.*?))(?:;|$)(callStack=(.*))?").matcher(caller);
        if (matcher.find())
        {
            this.origin = matcher.group(2);
            this.callStack = matcher.group(4);
        }
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getCallStack()
    {
        return callStack;
    }

    public void addCaller(String caller)
    {
        if (this.callStack == null)
        {
            this.callStack = caller;
        } else
        {
            this.callStack = this.callStack + "," + caller;
        }
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isEmpty(origin))
        {
            builder.append("origin=")
                    .append(origin)
                    .append(";");
        }
        if (!StringUtils.isEmpty(callStack))
        {
            builder.append("callStack=")
                    .append(callStack);
        }
        return "origin=" + origin + ";callStack=" + callStack;
    }
}
