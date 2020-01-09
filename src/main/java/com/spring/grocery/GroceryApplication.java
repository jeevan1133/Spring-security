package com.spring.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//@SpringBootApplication(scanBasePackageClasses = {LoadDatabase.class, CSRFConfig.class, 
//		 WebSecurityConfig.class, SecretService.class, HomeController.class, CustomerController.class, 
//		 BaseController.class}) // MyAccessDeniedHandler.class})
@SpringBootApplication
@EnableWebSecurity
public class GroceryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryApplication.class, args);
	}
}


//TODO
//1. get customer object in /userprofile
//2. display that customer using thymeleaf