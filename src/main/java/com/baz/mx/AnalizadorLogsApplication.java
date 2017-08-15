package com.baz.mx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class AnalizadorLogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalizadorLogsApplication.class, args);
	}
}
