package com.spring.grocery.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecretService {

    private Map<String, String> secrets = new HashMap<>();

    @Autowired
    private SigningKeyResolver signingKeyResolver;
    
    @Bean
    public SigningKeyResolver getKeyResolver() {
    	return new SigningKeyResolverAdapter() {
        	@Override
            public byte[] resolveSigningKeyBytes(@SuppressWarnings("rawtypes") JwsHeader header, Claims claims) {
            	log.debug("getting signing key resolver in secretservice");
            	log.debug("secrets: " + secrets.get(header.getAlgorithm()));
            	log.debug("JwsHeader: " + header.toString());
                return TextCodec.BASE64.decode(secrets.get(header.getAlgorithm()));            
            }
        	
        	public String toString() {
        		return "SigningKeyResolver=[" + super.toString() +  "]";
        	}            
        };
    }

    @PostConstruct
    public void setup() {
    	log.debug("In: " + this.toString() + "::setup" + " calling refreshsecrets");
    	refreshSecrets();
    }

    public SigningKeyResolver getSigningKeyResolver() {
    	log.debug("In: " + this.toString() + "::getSigningKeyResolver");
        return signingKeyResolver;
    }

    public Map<String, String> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<String, String> secrets) {
        Assert.notNull(secrets);
    	log.debug("In: " + this.toString() + "::setSecrets and assigning secrets");
        Assert.hasText(secrets.get(SignatureAlgorithm.HS512.getValue()));

        this.secrets = secrets;
    }

    public byte[] getSecretBytes() {
        return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.HS512.getValue()));
    }

    public Map<String, String> refreshSecrets() {
    	log.debug("In: " + this.toString() + "::refreshSecrets");
        SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS512);
        secrets.put(SignatureAlgorithm.HS512.getValue(), TextCodec.BASE64.encode(key.getEncoded()));
        log.debug("Secrets key is: ");
        secrets.forEach((k, v) -> {
        	log.debug("key: " + k + " value: " + v);
        });
        return secrets;
    }

	@Override
	public String toString() {
		return "SecretService [secrets=" + secrets + ", signingKeyResolver=" + signingKeyResolver + "]";
	}
    
}
