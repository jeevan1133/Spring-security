package com.spring.grocery.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityService {
	void autoLogin(HttpServletRequest request, HttpServletResponse response, String username, String password);
}
