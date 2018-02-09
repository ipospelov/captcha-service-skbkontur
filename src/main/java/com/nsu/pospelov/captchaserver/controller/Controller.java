package com.nsu.pospelov.captchaserver.controller;

import com.nsu.pospelov.captchaserver.captcha_generator.CaptchaResponse;
import com.nsu.pospelov.captchaserver.captcha_generator.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @Autowired
    private CaptchaService captchaService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getCaptchaImage() {

        CaptchaResponse captchaResponse = captchaService.generateResponse();

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.set("request_id", captchaResponse.getRequestID());
        responseHeaders.set("captcha_key", captchaResponse.getCaptchaKey());

        ResponseEntity responseEntity = ResponseEntity.ok().headers(responseHeaders).body(captchaResponse.getImage());

        return responseEntity;

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> checkUserResponse(@RequestParam(name = "request_id") String requestID,
                                                    @RequestParam(name = "captcha_key") String captchaKey) {

        switch (captchaService.checkUserResponse(requestID, captchaKey)) {
            case ERROR:
                return ResponseEntity.status(418).body("Error");
            case CORRECT:
                return ResponseEntity.ok().body("Success");
            default:
                return ResponseEntity.status(418).body("Error");
        }

    }


}
