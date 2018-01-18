/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.controller;

import com.baz.mx.utils.Constantes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author acruzb
 */
@Controller
@RequestMapping(path = "/pruebas")
public class UserController {
    
    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    
    @GetMapping(path = {"/", ""})
    public String inicio(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model){
        LOGGER.info("-----------------------------------------");
        return "Hol mundo";
    }
}
