package com.healthcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(Application.class);
	    }

	    public static void main(String[] args) throws Exception {
	        SpringApplication.run(Application.class, args);
	    }
	    
	    @Configuration
	    public static class PathMatchingConfigurationAdapter extends WebMvcConfigurerAdapter {

	    	@Override
	        public void configurePathMatch(PathMatchConfigurer configurer) {
	            configurer.setUseSuffixPatternMatch(false);
	        }
	    	
	        @Override
	        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	            configurer.favorPathExtension(false);
	        }
	    }
}
