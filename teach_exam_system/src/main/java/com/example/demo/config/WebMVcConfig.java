package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMVcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//映射图片保存地址
        //registry.addResourceHandler("/images/**").addResourceLocations("file:showpic/");
    	registry.addResourceHandler("/images/**").addResourceLocations("file:G:\\exam\\");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}