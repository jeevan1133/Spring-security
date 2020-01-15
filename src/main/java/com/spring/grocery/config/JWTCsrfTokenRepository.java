package com.spring.grocery.config;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JWTCsrfTokenRepository implements CsrfTokenRepository {
	private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
	private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";

	private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = CSRFConfig.class.getName()
			.concat(".CSRF_TOKEN");

	private byte[] secret;

	public JWTCsrfTokenRepository(byte[] secret) {
		log.debug("C-tor: " + secret);
		this.secret = secret;
	}
	
	private String createNewToken() {
		String id = UUID.randomUUID()
				.toString()
				.replace("-", "");

		Date now = new Date();
		Date exp = new Date(System.currentTimeMillis() + (1000 * 500)); // 5 minutes
		log.debug("id for csrf is: " + id);        
		log.debug("secret is: " + secret);
		String token = Jwts.builder()
				.setId(id)
				.setIssuedAt(now)
				.setNotBefore(now)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();       
		return token;
	}

	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		log.debug("In: " + this.toString() + ":: " + "generateToken");
		String token = createNewToken();
		return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, 
									DEFAULT_CSRF_PARAMETER_NAME,
									token);	
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		log.debug("In: " + JWTCsrfTokenRepository.class.toString() + ":: " + "saveToken");
		if (token == null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				log.debug("Session is null in saveToken");
				session.removeAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
			}
		} else {
			HttpSession session = request.getSession();
			log.debug("session attribute set::saveToken");
			session.setAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME, token);
		}
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		log.debug("In: " + this.toString() + ":: " + "loadToken");
		/* figure out the should be token */
		HttpSession session = request.getSession(false);
		if (session == null || "GET".equals(request.getMethod())) {
			log.debug("SESSION IS NULL ::loadToken");
			return null;
		}    	
		log.debug("Token: " + ((CsrfToken) session.getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME)).getToken() + "::loadToken");
		return (CsrfToken) session.getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
	}

	@Override
	public String toString() {
		return "JWTCsrfTokenRepository [secret=" + Arrays.toString(secret) + "]";
	}

}
