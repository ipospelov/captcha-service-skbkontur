package com.nsu.pospelov.captchaserver.captcha_generator.filters.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Filter {

    void apply(BufferedImage image);

}
