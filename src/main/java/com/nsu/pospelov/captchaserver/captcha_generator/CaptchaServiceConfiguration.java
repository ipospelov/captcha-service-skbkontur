package com.nsu.pospelov.captchaserver.captcha_generator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaServiceConfiguration {

    @Bean
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }
}
