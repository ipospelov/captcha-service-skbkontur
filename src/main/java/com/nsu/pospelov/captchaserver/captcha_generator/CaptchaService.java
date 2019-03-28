package com.nsu.pospelov.captchaserver.captcha_generator;

public interface CaptchaService {

    CaptchaResponse generateResponse();

    ResponseType checkUserResponse(String requestID, String userResponse);
}
