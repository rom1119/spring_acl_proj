package com.example.demo.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {



    @Bean
    public InternalResourceViewResolver viewResolver() {
        System.out.println("viewResolver");
//        System.exit(0);
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/jsp/");
        bean.setSuffix(".jsp");
        return bean;
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp();
    }

    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
//



}