package org.kira.requesttrail.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
public class LoggingContext
{
    private String app;

    public String getApp()
    {
        return app;
    }

    public void setApp(String app)
    {
        this.app = app;
    }

    private Map<String, String> paramMap = new HashMap<String, String>();

    public Map<String, String> getParams()
    {
        return paramMap;
    }

    public void setParams(Map<String, String> paramMap)
    {
        this.paramMap = paramMap;
    }

    public void put(String key, String value)
    {
        paramMap.put(key, value);
    }

    public String get(String key)
    {
        return paramMap.get(key);
    }
}
