package com.spring.grocery.utilities;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.grocery.dao.CustomerRepository;
import com.spring.grocery.dao.OrderRepository;
import com.spring.grocery.dao.ProductRepository;
import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Comment;
import com.spring.grocery.entity.Customer;
import com.spring.grocery.entity.OrderLine;
import com.spring.grocery.entity.Orders;
import com.spring.grocery.entity.Product;
import com.spring.grocery.entity.Role;
import com.spring.grocery.entity.Users;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {

	@Autowired
	private CustomerRepository repository;
	@Autowired
	private OrderRepository ordRepo;
	@Autowired
	private ProductRepository prodRepo;
		
	@Autowired
	private UserRepository userRepo;
	
	@Bean
	CommandLineRunner init() {
		log.info("Loading to database");
		Product p1 = new Product("Apples", 2.49, true);
		Product p2 = new Product("Oranges", 5.49, true);
		Product p3 = new Product("Grapes", 3.49, false);
		Product p4 = new Product("Spinach", 1.49, true);
		Product p5 = new Product("Avocados", 0.79, true);

		Comment c1 = new Comment("I'm so sad this product is no longer available!");
		Comment c2 = new Comment("When do you expect to have it back?");
		Comment c3 = new Comment("Very tasty! I'd definitely buy it again!");
		Comment c4 = new Comment("My kids love it!");
		Comment c5 = new Comment("Good, my basic breakfast cereal. Though maybe a bit in the sweet side...");
		Comment c6 = new Comment("I'm sorry to disagree, but IMO these are far too sweet");
		Comment c7 = new Comment("Made bread with this and it was great!");
		Comment c8 = new Comment("Awesome Spanish oil. Buy it now.");
		Comment c9 = new Comment("Great value!");
		Comment c10 = new Comment("My favourite :)");
		Comment c11 = new Comment( "Too hard! I would not buy again");
		
		p1.setComments(Arrays.asList(c1, c2, c3));
		p2.setComments(Arrays.asList(c4, c5));
		p3.setComments(Arrays.asList(c6));
		p4.setComments(Arrays.asList(c7,c8,c9));
		p5.setComments(Arrays.asList(c10 , c11));		
		
		Customer cus1 = new Customer("John Doe", 
				Calendar.getInstance(TimeZone.getTimeZone("PST")));
		Customer cus2 = new Customer("Mary Jane",
				Calendar.getInstance(TimeZone.getTimeZone("CST")));
		
		Customer cus3 = new Customer("Mary Blood",
				Calendar.getInstance(TimeZone.getTimeZone("UTC")));
		
		Orders ord1 = new Orders(Calendar.getInstance(TimeZone.getTimeZone("PST")), cus1);	
		Orders ord2 = new Orders(Calendar.getInstance(TimeZone.getTimeZone("PST")), cus1);
		Orders ord3 = new Orders(Calendar.getInstance(TimeZone.getTimeZone("PST")), cus2);
		Orders ord4 = new Orders(Calendar.getInstance(TimeZone.getTimeZone("PST")), cus3);

		OrderLine ordl1 = new OrderLine(p1, 2.49, 2.19);
		OrderLine ordl2 = new OrderLine(p2, 5.49, 5.19);
		OrderLine ordl3 = new OrderLine(p3, 3.49, 3.19);
		OrderLine ordl4 = new OrderLine(p4, 1.49, 1.10);
		OrderLine ordl5 = new OrderLine(p5, 0.79, 0.50);
		OrderLine ordl6 = new OrderLine(p3, 3.49, 3.19);
		
		Set<OrderLine> s1 = new HashSet<OrderLine>();
		s1.addAll(Arrays.asList(ordl1, ordl2));
		ord1.setOrderLine(s1);		
		
		Set<OrderLine> s2 = new HashSet<OrderLine>();
		s2.addAll(Arrays.asList( ordl3));
		ord2.setOrderLine(s2);

		Set<OrderLine> s3 = new HashSet<OrderLine>();
		s3.addAll(Arrays.asList(ordl4, ordl5));
		ord3.setOrderLine(s3);
		
		Set<OrderLine> s4 = new HashSet<OrderLine>();
		s3.addAll(Arrays.asList(ordl6));
		ord4.setOrderLine(s4);
		
		Users us1 = new Users(cus1, "foo", "foo", Role.ADMIN, "abcd@xyz.com");
		Users us2 = new Users(cus2, "manoj", "ppppp", Role.USER, "manoj@adh.com");
		
		return args -> {
			log.info("Proeloaing " + prodRepo.save(p1));
			log.info("Proeloaing " + prodRepo.save(p2));
			log.info("Proeloaing " + prodRepo.save(p3));
			log.info("Proeloaing " + prodRepo.save(p4));
			log.info("Proeloaing " + prodRepo.save(p5));
			
			log.info("Preloading " + repository.save(cus1));
			log.info("Preloading " + repository.save(cus2));
			log.info("Preloading " + repository.save(cus3));
			
			log.info("Preloading " + ordRepo.save(ord1));
			log.info("Preloading " + ordRepo.save(ord2));
			log.info("Preloading " + ordRepo.save(ord3));
			log.info("Preloading " + ordRepo.save(ord4));
			
			log.info("Preloading " + userRepo.save(us1));
			log.info("Preloading " + userRepo.save(us2));
		};
	}
}
