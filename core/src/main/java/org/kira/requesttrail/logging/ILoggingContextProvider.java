package org.kira.requesttrail.logging;

/**
 * Provides LoggingContext, composed of key, value pair for request.
 * for example; email to email address of the user who initiated the request.
 */
public interface ILoggingContextProvider
{
    LoggingContext getLoggingContext();
}
