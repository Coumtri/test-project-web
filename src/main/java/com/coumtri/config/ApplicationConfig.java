package com.coumtri.config;

import com.coumtri.Application;
import com.coumtri.account.UserService;
import com.coumtri.signpost.SignpostService;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackageClasses = Application.class, excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class}))
class ApplicationConfig {

	@Bean
	 public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
	}

	@Bean
	public SignpostService signpostService() {
		return new SignpostService();
	}
	
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new ClassPathResource("/persistence.properties"), new ClassPathResource("/appdirect.properties"));
		return ppc;
	}
	
}