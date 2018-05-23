package com.baz.mx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan(basePackages = {"com.bancoazteca","com.baz.mx"})
public class AnalizadorLogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalizadorLogsApplication.class, args);
	}
}
