package com.nsu.pospelov.captchaserver.captcha_generator.filters;

import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class NoisyFilter implements Filter {

    private final int noiseAmount = 300;
    private final int noiseEnthropySize = 10;

    @Override
    public void apply(BufferedImage image) {

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        int x1;
        int y1;
        int x2;
        int y2;

        graphics.setColor(Color.BLACK);

        for(int i = 0; i < noiseAmount; i++){
            x1 = ThreadLocalRandom.current().nextInt(0, image.getWidth());
            y1 = ThreadLocalRandom.current().nextInt(0, image.getHeight());

            x2 = x1 + ThreadLocalRandom.current().nextInt(-noiseEnthropySize, noiseEnthropySize);
            y2 = y1 + ThreadLocalRandom.current().nextInt(-noiseEnthropySize, noiseEnthropySize);

            graphics.drawLine(x1, y1, x2, y2);

        }

    }

}
