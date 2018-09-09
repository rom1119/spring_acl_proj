package com.example.demo.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@PropertySource("classpath:application.properties")
public class MvcConfigurer implements WebMvcConfigurer {

    @Value( "${app.fileUpload.folder}" )
    private String UPLOADED_FOLDER;

    @Value( "${app.fileUpload.pattern}" )
    private String RESOURCE_PATTERN;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        System.out.println("uploadsHandler");
        registry.addResourceHandler(RESOURCE_PATTERN).addResourceLocations("file:" + UPLOADED_FOLDER);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://domain2.com")
//                .allowedMethods("PUT", "DELETE")
//                .allowedHeaders("header1", "header2", "header3")
//                .exposedHeaders("header1", "header2")
//                .allowCredentials(true).maxAge(3600);
//
//        // Add more mappings...
//    }
}