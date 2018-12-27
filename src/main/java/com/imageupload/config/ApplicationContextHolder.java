package com.imageupload.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Its spring bean holder to get any spring bean class using name or Class
 */
@Component
public class ApplicationContextHolder {

    @Autowired
    private ApplicationContext applicationContext;

    private static ApplicationContextHolder _instance;

    @PostConstruct
    public void initialize(){
        _instance = this;
    }

    public static <T> T getBean(Class<T> inClass){
        return _instance.applicationContext.getBean(inClass);
    }

    public static Object getBean(String inBean){
        return _instance.applicationContext.getBean(inBean);
    }

}
