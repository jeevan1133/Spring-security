package com.spring.grocery.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.grocery.dao.CustomerRepository;
import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Customer;
import com.spring.grocery.entity.Role;
import com.spring.grocery.entity.Users;
import com.spring.grocery.model.CustomUser;
import com.spring.grocery.model.UserDTO;
import com.spring.grocery.security.PasswordEncrypter;
import com.spring.grocery.service.SecurityService;
import com.spring.grocery.service.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CustomerRepository cusRepo;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private PasswordEncrypter userRepoImpl;

	@RequestMapping(value="/")
	public String index() {
		return "redirect:/index";
	}

	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String getIndex(Model model)  { /* , HttpServletRequest request) { */
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		model.addAttribute("today", dateFormat.format(cal.getTime()));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			CustomUser userDetails = (CustomUser)auth.getPrincipal();
			model.addAttribute("user", userDetails.getCustomer());
			log.debug("Adding attribute user: " + userDetails);

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
		return "error";
	}

	@RequestMapping(value="/registration", method=GET)
	public String registerUser(Model model) {
		log.debug("Getting registration page");
		model.addAttribute("userDto", new UserDTO());
		return "register";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userDto") @Valid UserDTO userForm, BindingResult bindingResult, Model model) {	
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
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
			return "register";
		}
		log.debug("Auto loggin user");
		cusRepo.save(customer);
		userRepo.save(userRepoImpl.encryptPassword(user));
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = null;
		HttpServletResponse response = null;
	    if (requestAttributes instanceof ServletRequestAttributes) {
	        request = ((ServletRequestAttributes)requestAttributes).getRequest();
	        response = ((ServletRequestAttributes) requestAttributes).getResponse();	        
	    }
		securityService.autoLogin(request, response, userForm.getUserName(), userForm.getPassword());
		log.debug("Successfully auto logged in and returingn index");
		Collection<GrantedAuthority> authorities = ((CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
		var wrapper = new Object() { 
				String value = "userprofile";
		};
		
		authorities.stream().forEach(x -> {			
			if((x.getAuthority()).equals("USER")) {
				wrapper.value = "index";
			}
		});
		log.debug("Returning from controller : " + wrapper.value);

		return wrapper.value;
	}

	@RequestMapping(value="/userprofile", method=GET)
	public String getProfile(Model model) throws Exception  {
		log.info("GETTING USER PROFILE");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUser userDetails = (CustomUser)auth.getPrincipal();
		model.addAttribute("user", userDetails.getCustomer());
		return "userprofile";
		
	}
	
	@GetMapping("/favicon.ico")
    public void returnNoFavicon() {
		log.debug("Returning no favico.ico");
		//return "redirect:/index";
    }
}
