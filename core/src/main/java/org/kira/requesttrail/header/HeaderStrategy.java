package org.kira.requesttrail.header;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
public enum HeaderStrategy
{
    HEADER("header"), COOKIE("cookie");
    private final String header;

    HeaderStrategy(String header)
    {
        this.header = header;
    }

    public String getHeader()
    {
        return header;
    }

    public static boolean isValidHeaderStrategy(String header)
    {
        return HEADER.getHeader().equals(header) ||
                COOKIE.getHeader().equals(header);
    }
}
