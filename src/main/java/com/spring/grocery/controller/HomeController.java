package com.spring.grocery.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.grocery.dao.CustomerRepository;
import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Customer;
import com.spring.grocery.entity.Role;
import com.spring.grocery.entity.Users;
import com.spring.grocery.model.CustomUser;
import com.spring.grocery.model.UserDTO;
import com.spring.grocery.service.SecurityService;
//import com.spring.grocery.service.UserValidator;
import com.spring.grocery.service.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
//
//	@Autowired
//    private MessageSource messageSource;
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CustomerRepository cusRepo;
	
	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value="/")
	public String index() {
		return "redirect:/index";
	}

	@RequestMapping(value="/index")
	public String getIndex(Model model)  { /* , HttpServletRequest request) { */
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		model.addAttribute("today", dateFormat.format(cal.getTime()));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			CustomUser userDetails = (CustomUser)auth.getPrincipal();
			model.addAttribute("user", userDetails.getCustomer());
			//log.debug("User added is: " + userDetails.getCustomer());

		} catch (Exception e) {
			log.debug("No logged in user");
		}
		return "index";
	}

	@RequestMapping(value="/login", method = GET)
	public String getLogin(Model model) {
		return "login";
	}
	
	@RequestMapping(value="/expired-jwt", method = RequestMethod.POST)
	public String sendExpiredLogin(Model model) {
		log.info("GETTING expired-jwt page");				
		return "expired-jwt";
	}
	
	@RequestMapping(value="/registration", method=GET)
	public String registerUser(Model model) {
		log.debug("Getting registration page");
		model.addAttribute("userDto", new UserDTO());
		return "register";
	}
	
	/*
	@PostMapping("/registration")
    public String registration(@ModelAttribute("userDto") Users userForm, BindingResult bindingResult, Model model) {	
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.debug("Validating user form");

				userValidator.validate(userForm, bindingResult);
			}
		});
		t1.start();			

        Customer customer = null;
    	if (userForm.getCustomer() == null) {
    		customer = new Customer(userForm.getFirstName(), userForm.getLastName(), Calendar.getInstance());
    		userForm.setCustomer(customer);
    		log.debug("Setting new customer on userForm: " + userForm);
    		
    	}        	
    	log.debug("User is: " + userForm);    	    
       	try {    		
			t1.join();			
			log.debug("Thread joined successfully");
			if (bindingResult.hasErrors()) {
				log.debug("Result has errors");
				bindingResult.getAllErrors().stream().forEach(x -> {
								log.debug(x.toString());						
				});
	            return "register";
	        } 
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return "register";
		}
    	log.debug("Auto loggin user");
    	cusRepo.save(customer);
    	userRepo.save(userForm);
		securityService.autoLogin(userForm.getUserName(), userForm.getPassword());
		
        return "redirect:/userprofile";
    }
    */
	
	
	@PostMapping("/registration")
    public String registration(@ModelAttribute("userDto") @Valid UserDTO userForm, BindingResult bindingResult, Model model) {	
		log.debug("User FORM IS: " + userForm);

		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.debug("Validating user form");
				userValidator.validate(userForm, bindingResult);
			}
		});
		t1.start();			

        Customer customer = new Customer(userForm.getFirstName(), userForm.getLastName(), Calendar.getInstance());
        Users user = new Users(customer,
        						userForm.getUserName(),
        						userForm.getPassword(), 
        						Role.USER, 
        						userForm.getEmail());
        cusRepo.save(customer);
    	log.debug("User is: " + userForm);    	    
       	try {    		
			t1.join();			
			log.debug("Thread joined successfully");
			if (bindingResult.hasErrors()) {
				log.debug("Result has errors");
				bindingResult.getAllErrors().stream().forEach(x -> {
								log.debug(x.toString());						
				});
				cusRepo.delete(customer);
	            return "register";
	        } 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return "register";
		}
    	log.debug("Auto loggin user");
    	cusRepo.save(customer);
    	userRepo.save(user);
		securityService.autoLogin(userForm.getUserName(), userForm.getPassword());
		
        return "redirect:/userprofile";
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
