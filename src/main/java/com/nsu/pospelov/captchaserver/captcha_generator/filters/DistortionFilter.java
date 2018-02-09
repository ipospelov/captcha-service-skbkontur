package com.nsu.pospelov.captchaserver.captcha_generator.filters;
import com.nsu.pospelov.captchaserver.captcha_generator.TextImage;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.Filter;
import com.nsu.pospelov.captchaserver.captcha_generator.util.Pair;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class DistortionFilter implements Filter {

    private final double rotationAmountMin = 0.3;
    private final double rotationAmountMax = 0.6;

    private final double effectSizeMin = 30;
    private final double effectSizeMax = 60;

    private final int distortAmount = 4;


    @Override
    public void apply(BufferedImage image) {

        double rotationAmount;
        double effectSize;

        for(int i = 0; i < distortAmount; i++){
            rotationAmount = ThreadLocalRandom.current().nextDouble(rotationAmountMin, rotationAmountMax);
            effectSize = ThreadLocalRandom.current().nextDouble(effectSizeMin, effectSizeMax);
            applyDistort(image, (i + 1) * image.getWidth()/(distortAmount +1), image.getHeight()/2, rotationAmount, effectSize);
        }

    }

    private void applyDistort(BufferedImage image ,int swirlX, int swirlY, double rotationAmount, double effectSize){

        BufferedImage srcImage = TextImage.copyImage(image);
        int u,v;
        Pair<Integer, Integer> distortedPosition;

        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){

                distortedPosition = getDistortedPosition(i, j, swirlX, swirlY, rotationAmount, effectSize);
                u = distortedPosition.getFirst();
                v = distortedPosition.getSecond();
                if(u < 0) u = 0;
                if(v < 0) v = 0;
                if(u >= image.getWidth()) u = image.getWidth() - 1;
                if(v >= image.getHeight()) v = image.getHeight() - 1;

                image.setRGB(i, j, srcImage.getRGB(u, v));
            }
        }

    }

    private Pair getDistortedPosition(int inputX, int inputY, int swirlX, int swirlY, double rotationAmount, double effectSize){

        int u, v;
        int x = inputX - swirlX;
        int y = inputY - swirlY;
        double angle = rotationAmount * Math.exp(-(x * x + y * y)/(effectSize * effectSize));
        u = (int)(Math.cos(angle) * x + Math.sin(angle) * y);
        v = (int)(-Math.sin(angle) * x + Math.cos(angle) * y);
        u += swirlX;
        v += swirlY;

        return new Pair(u, v);


    }

}
