package com.southern.cmps.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.southern.cmps")
@ImportResource(locations= {"classpath:cmps-service.xml"})
@PropertySource("classpath:application.properties")
@SpringBootApplication
public class CmpsApplication extends SpringBootServletInitializer{
	
	
	  @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(CmpsApplication.class);
	    }

	    public static void main(String[] args) throws Exception {
	        SpringApplication.run(CmpsApplication.class, args);
	    }	
	    
	    @Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/student/getStudentDetail").allowedOrigins("http://localhost:4200").
					allowedHeaders("Requestor-Type", "Access-Control-Allow-Origin").exposedHeaders("X-Get-Header", "Access-Control-Allow-Origin");
				}
			};
		}
}
