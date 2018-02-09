package com.nsu.pospelov.captchaserver.captcha_generator;

public class CaptchaResponse {

    private byte[] image;
    private String captchaKey;
    private String requestID;

    public CaptchaResponse(byte[] image, String captchaKey, String requestID) {
        this.image = image;
        this.captchaKey = captchaKey;
        this.requestID = requestID;
    }

    public byte[] getImage() {
        return image;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public String getRequestID() {
        return requestID;
    }
}
