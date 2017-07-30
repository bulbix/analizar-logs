/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author acruzb
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = 
    {
	"classpath:/META-INF/resources/", 
        "classpath:/resources/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
		registry.addResourceHandler("/webjars/**").addResourceLocations(
				"classpath:/META-INF/resources/webjars/");
	}
	if (!registry.hasMappingForPattern("/**")) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
                registry.addResourceHandler("/css/**").addResourceLocations("/static/css/");
                registry.addResourceHandler("/js/**").addResourceLocations("/static/js/");
                registry.addResourceHandler("/images/**").addResourceLocations("/static/images/");
	}
    }
    
}
