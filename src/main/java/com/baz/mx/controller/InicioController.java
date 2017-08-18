/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author acruzb
 */
@Controller
public class InicioController {
    
    // inject via application.properties
    @Value("${welcome.message:test}")
    private final String message = "Hello World";

    @RequestMapping(value = {""})
    public String welcome(Map<String, Object> model, Principal principal) {
        model.put("message", this.message);
        model.put("usuario", principal.getName());
        return "welcome";
    }
    
}
