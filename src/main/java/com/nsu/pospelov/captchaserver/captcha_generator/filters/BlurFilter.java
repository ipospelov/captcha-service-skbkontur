package com.nsu.pospelov.captchaserver.captcha_generator.filters;

import com.nsu.pospelov.captchaserver.captcha_generator.TextImage;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.Filter;
import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.FilterCore;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurFilter implements Filter {
    @Override
    public void apply(BufferedImage image) {
        FilterCore core = new FilterCore(6, new int[][]{{0, 1, 0}, {1, 2, 1}, {0, 1, 0}});
        BufferedImage srcImage = TextImage.copyImage(image);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, valueToColor(core.getFilteredPixel(x, y, srcImage)).getRGB());
            }
        }
    }

    private Color valueToColor(int[] value) {
        int red = clamp8bit(value[0]);
        int green = clamp8bit(value[1]);
        int blue = clamp8bit(value[2]);
        return new Color(red, green, blue);
    }

    private int clamp8bit(int colorValue) {
        return Math.min(Math.max(colorValue, 0), 255);
    }
}
