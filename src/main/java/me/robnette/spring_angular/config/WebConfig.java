package me.robnette.spring_angular.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@ComponentScan
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/angular").setViewName("forward:/angular.html");
        registry.addViewController("/vue").setViewName("forward:/vue.html");
    }
}
