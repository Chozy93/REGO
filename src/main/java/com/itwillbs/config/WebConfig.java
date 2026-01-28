package com.itwillbs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.itwillbs.interceptor.InfoCheckInterceptor;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("./upload");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadPath + "/");
        
        System.out.println("UPLOAD PATH = " + uploadPath);

    }
    
    
    @Autowired
    private InfoCheckInterceptor infoCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(infoCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/complete-info", "/auth/**", "/logout",
                    "/css/**", "/js/**", "/img/**", "/favicon.ico", "/"
                );
    }
    
}