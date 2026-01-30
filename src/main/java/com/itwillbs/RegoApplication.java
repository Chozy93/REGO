package com.itwillbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan({
    "com.itwillbs.mapper",
    "com.itwillbs.ai"
})
public class RegoApplication extends SpringBootServletInitializer{
	
	
	  @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        return builder.sources(RegoApplication.class);
	    }
	
	
    public static void main(String[] args) {
        SpringApplication.run(RegoApplication.class, args);
    }
}
