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
import org.springframework.security.web.util.UrlUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JWTCsrfTokenRepository implements CsrfTokenRepository {
	private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = CSRFConfig.class.getName()
			.concat(".CSRF_TOKEN");

	private byte[] secret;


	public JWTCsrfTokenRepository(byte[] secret) {
		log.debug("C-tor: " + secret);
		this.secret = secret;
	}

	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		log.debug("In: " + this.toString() + ":: " + "generateToken");
		String id = UUID.randomUUID()
				.toString()
				.replace("-", "");

		Date now = new Date();
		Date exp = new Date(System.currentTimeMillis() + (1000 * 300)); // 300 seconds
		log.debug("id for csrf is: " + id);        
		log.debug("secret is: " + secret);
		String token = Jwts.builder()
				.setId(id)
				.setIssuedAt(now)
				.setNotBefore(now)
				.setExpiration(exp)
				.setSubject("try")
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();       
		log.debug("Token is: " + token );

		DefaultCsrfToken newToken =  new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
		log.debug("newToken is: " + newToken.getToken());
		String sameString = newToken.getToken() == token ? "YES" : "NO";
		log.debug("Token is same: " + sameString);
		return newToken;
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
			log.debug("DEFAULT_CSRF_TOKEN_ATTR_NAME: " + DEFAULT_CSRF_TOKEN_ATTR_NAME);
			log.debug("Token is: " + token.getToken());
			String actualToken = request.getHeader(token.getHeaderName());
			if(actualToken == null) {
				actualToken = request.getParameter(token.getParameterName());
			}
			//if (!actualToken.equals(anObject))
			if(!token.getToken().equals(actualToken)) {
				if(log.isDebugEnabled()) {
					log.debug("Invalid CSRF token found for in save token"
							+ UrlUtils.buildFullRequestUrl(request));
				}
				String sameToken = actualToken == token.getToken() ? "YES"  : "NO";
				log.debug("token's equal : " + sameToken);
				log.debug("actualToken: " + actualToken + " ::provided Token:: " + token.getToken());
			}
			session.setAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME, token);
		}
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		log.debug("In: " + this.toString() + ":: " + "loadToken");
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
