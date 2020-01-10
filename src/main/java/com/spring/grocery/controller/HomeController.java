package com.spring.grocery.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.grocery.model.CustomUser;

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
	public String getIndex(Model model) {
		//System.out.println("Request attribute: " + request.getAttribute("today"));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			CustomUser userDetails = (CustomUser)auth.getPrincipal();
			model.addAttribute("user", userDetails.getCustomer());
		} catch (Exception e) {
			// TODO: handle exception
			log.debug("No logged in user");
		}
		return "index";
	}

	@RequestMapping(value="/login", method = GET)
	public String getLogin(Model model) {
		log.info("GETTING /login page");				
		return "login";
	}

	@RequestMapping(value="/userprofile", method=GET)
	public String getProfile(Model model) throws Exception  {
		log.info("GETTING USER PROFILE");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("authentication: " + auth.getName());
		log.debug("auth: " + auth.toString());

		CustomUser userDetails = (CustomUser)auth.getPrincipal();
		log.debug("customer is: " + userDetails.getCustomer());
		
		model.addAttribute("user", userDetails.getCustomer());
		return "userprofile";
	}
}
