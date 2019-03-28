package com.nsu.pospelov.captchaserver.captcha_generator;

import com.nsu.pospelov.captchaserver.captcha_generator.util.Pair;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaServiceImpl implements CaptchaService {

    private CaptchaImageGenerator captchaImageGenerator;
    private Map<String, String> responses;

    public CaptchaServiceImpl() {
        captchaImageGenerator = new CaptchaImageGenerator(200, 100, 6);
        responses = new ConcurrentHashMap<>();
    }

    public synchronized CaptchaResponse generateResponse() {
        String requestID = UUID.randomUUID().toString();
        Pair<String, byte[]> captcha = captchaImageGenerator.generateCaptcha();
        responses.put(requestID, captcha.getFirst());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                responses.remove(requestID);
            }
        }, 5000); //через 5 секунд информация о запросе будет удалена из коллекции
        return new CaptchaResponse(captcha.getSecond(), captcha.getFirst(), requestID);
    }

    public synchronized ResponseType checkUserResponse(String requestID, String userResponse) {
        ResponseType responseType;
        if (!responses.containsKey(requestID))
            return ResponseType.ERROR;
        if (responses.get(requestID).equals(userResponse))
            responseType = ResponseType.CORRECT;
        else
            responseType = ResponseType.ERROR;
        responses.remove(requestID);
        return responseType;
    }
}
