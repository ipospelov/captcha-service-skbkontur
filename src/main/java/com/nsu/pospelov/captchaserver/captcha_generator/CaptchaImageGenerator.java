package com.nsu.pospelov.captchaserver.captcha_generator;

import com.nsu.pospelov.captchaserver.captcha_generator.filters.BlurFilter;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.DistortionFilter;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.NoisyFilter;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.Filter;
import com.nsu.pospelov.captchaserver.captcha_generator.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaImageGenerator {

    private TextImage captchaImage;
    private RandomStringGenerator stringGenerator;

    private Filter blurFilter;
    private Filter distortionFilter;
    private Filter noisyFilter;


    private int stringLength;

    public CaptchaImageGenerator(int imageWidth, int imageHeight, int stringLength) {
        this.stringLength = stringLength;
        captchaImage = new TextImage(imageWidth, imageHeight);
        stringGenerator = new RandomStringGenerator();
        blurFilter = new BlurFilter();
        distortionFilter = new DistortionFilter();
        noisyFilter = new NoisyFilter();
    }

    public Pair<String, byte[]> generateCaptcha() {
        String captchaString = stringGenerator.generateString(stringLength);
        captchaImage.imageInit(captchaString);
        captchaImage.applyFilter(distortionFilter, noisyFilter, blurFilter, blurFilter, blurFilter);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            ImageIO.write(captchaImage.getImage(), "jpg", bao);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(captchaString, bao.toByteArray());
    }

    public BufferedImage getImage() {
        return captchaImage.getImage();
    }
}
