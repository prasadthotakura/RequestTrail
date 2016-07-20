package org.kira.requesttrail.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

public class AuthUtils
{
    private AuthUtils()
    {
    }

    public static String getCurrentUser()
    {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        String name = null;
        if (auth != null && auth.getPrincipal() != null)
        {
            if (auth.getPrincipal() instanceof UserDetails)
            {
                name = ((UserDetails) auth.getPrincipal()).getUsername();
            } else
            {
                name = String.valueOf(auth.getPrincipal());
            }
        }
        return name;
    }
}
