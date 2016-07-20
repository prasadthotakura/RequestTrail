package org.kira.requesttrail.logging;

import org.kira.requesttrail.utils.AuthUtils;
import org.springframework.stereotype.Component;

/**
 * @license Request Trail Sample Application v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
@Component
public class SampleAppLoggingContextProviderImpl implements ILoggingContextProvider
{
    @Override
    public LoggingContext getLoggingContext()
    {
        LoggingContext context = new LoggingContext();
        context.setApp("sampleApp");
        context.put("user", AuthUtils.getCurrentUser());
        return context;

    }
}
