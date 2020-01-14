package com.spring.grocery.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import com.spring.grocery.entity.Users;
import com.spring.grocery.model.CustomUser;
import com.spring.grocery.service.SecurityService;
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
		log.info("GETTING HOME");		
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
			log.debug("User added is: " + userDetails.getCustomer());

		} catch (Exception e) {
			log.debug("No logged in user");
		}
		return "index";
	}

	@RequestMapping(value="/login", method = GET)
	public String getLogin(Model model) {
		log.info("GETTING /login page");				
		return "login";
	}
	
	@RequestMapping(value="/expired-jwt", method = RequestMethod.POST)
	public String sendExpiredLogin(Model model) {
		log.info("GETTING /expired-jwt page");				
		return "expired-jwt";
	}
	
	@RequestMapping(value="/registration", method=GET)
	public String registerUser(Model model) {
		log.debug("Getting registration page");
		model.addAttribute("userDto", new Users());
		return "register";
	}
	
	@PostMapping("/registration")
    public String registration(@ModelAttribute("userDto") Users userForm, BindingResult bindingResult, Model model) {	       
		userValidator.validate(userForm, bindingResult);
		
//        /* Check if email exists */
//        Users userExists = userRepo.findByEmail(userForm.getEmail());
//
//        if (userExists != null) {
//        	log.debug("User already exists");
//
//            bindingResult
//                    .rejectValue("email", "error.user",
//                            "There is already a user registered with the email provided");
//            return "register";
//        }

        if (bindingResult.hasErrors()) {
            return "register";
        } 
        
    	if (userForm.getCustomer() == null) {
    		Customer customer = new Customer(userForm.getFirstName(), userForm.getLastName(), Calendar.getInstance());
    		userForm.setCustomer(customer);
    		cusRepo.save(customer);
    	}        	
    	log.debug("User is: " + userForm);

    	userRepo.save(userForm);
    	log.debug("User successfully saved");
//    	model.addAttribute("successMessage", "User has been registered successfully");            
//    	model.addAttribute("user", new Users());
        
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
