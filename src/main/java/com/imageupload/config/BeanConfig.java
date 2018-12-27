package com.imageupload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring bean configuration
 */
@Configuration
public class BeanConfig {

    /**
     * It creates a restTemplate singleton object
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
