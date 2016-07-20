package org.kira.requesttrail.aspect;

import org.kira.requesttrail.RequestTrailPropertyHolder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

@Component
public class RequestTrailAspectCondition implements Condition
{

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        return Boolean.valueOf(conditionContext.getEnvironment().getProperty(RequestTrailPropertyHolder.REQUEST_TRAIL_ASPECT_ENABLE));
    }
}
