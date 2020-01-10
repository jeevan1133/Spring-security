package com.spring.grocery.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected boolean enableHttpSessionEventPublisher() {
		log.debug("Enable http session event publisher");

		return true;
	}
}