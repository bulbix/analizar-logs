package com.baz.mx.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");
        return model;
    }

    @GetMapping("/error")
    public String error() {
        return "error/error";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }

    @GetMapping("/404")
    public String error404() {
        return "error/404";
    }

    @GetMapping("/405")
    public String error405() {
        return "error/405";
    }
    
    @GetMapping("/invalidSession")
    public String invalidSession() {
        return "error/invalidSession";
    }
}
