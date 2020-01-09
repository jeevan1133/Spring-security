package com.spring.grocery.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@RequestMapping(value="/")
	public String index(HttpServletRequest request) {
		log.info("GETTING HOME");
		request.setAttribute("name", "Jeevan");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		request.setAttribute("today", dateFormat.format(cal.getTime()));
		return "redirect:/index";
	}
	
	@RequestMapping(value="/index")
	public String getIndex(HttpServletRequest request) {
		System.out.println("Request attribute: " + request.getAttribute("today"));
		return "index";
	}
	
//	@RequestMapping(value="/error")
//	public String getIndex() {
//		return "redirect:/index";
//	}
	
	@RequestMapping(value="/login", method = GET)
	public String getLogin(Model model) {
		log.info("GETTING /login page");		
		return "login";
	}
	
	@RequestMapping(value="/userprofile", method=GET)
	public String getProfile(Model model) {
		log.info("GETTING USER PROFILE");
		//log.info("User is: " + user);
		//model.addAttribute("user", user);
		return "userprofile";
	}
}
