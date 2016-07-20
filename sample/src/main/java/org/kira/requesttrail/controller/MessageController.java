package org.kira.requesttrail.controller;

import org.kira.requesttrail.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @license Request Trail Sample Application v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */

@Controller
public class MessageController
{
    private Logger LOGGER = LoggerFactory.getLogger(MessageController.class);


    @RequestMapping("/")
    public String greeting(Map<String, Object> model)
    {
        String name = AuthUtils.getCurrentUser();
        LOGGER.info("Saying greeting to " + name);
        model.put("message", "Hello " + name + "!");
        return "message";
    }


}
