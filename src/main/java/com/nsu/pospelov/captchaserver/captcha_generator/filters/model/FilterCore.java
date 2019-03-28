package com.nsu.pospelov.captchaserver.captcha_generator.filters.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FilterCore {

    private int[][] core;
    private int divValue;


    public FilterCore(int divValue, int[][] core) {
        this.core = core.clone();
        this.divValue = divValue;
    }

    private int getValue(int i, int j) {
        return core[i][j];
    }

    private int getWidth() {
        return core[0].length;
    }

    private int getHeight() {
        return core.length;
    }

    private int getDivValue() {
        return divValue;
    }

    public int[] getFilteredPixel(int x, int y, BufferedImage image) {
        int newGreenComponent = 0;
        int newRedComponent = 0;
        int newBlueComponent = 0;
        for (int i = -(getHeight() / 2); i < getHeight() - (getHeight() / 2); i++) {
            for (int j = -(getWidth() / 2); j < getWidth() - (getWidth() / 2); j++) {
                newRedComponent += getRedComponent(x, y, i, j, image);
                newGreenComponent += getGreenComponent(x, y, i, j, image);
                newBlueComponent += getBlueComponent(x, y, i, j, image);
            }
        }
        newRedComponent /= getDivValue();
        newGreenComponent /= getDivValue();
        newBlueComponent /= getDivValue();
        return new int[]{newRedComponent, newGreenComponent, newBlueComponent};
    }

    private int getGreenComponent(int x, int y, int i, int j, BufferedImage image) {
        if (x + i >= 0 && x + i < image.getWidth() && y + j >= 0 && y + j < image.getHeight()) {
            Color currentColor = new Color(image.getRGB(x + i, y + j));
            return getValue(i + getHeight() / 2, j + getWidth() / 2) * currentColor.getGreen();
        } else
            return 0;
    }

    private int getRedComponent(int x, int y, int i, int j, BufferedImage image) {
        if (x + i >= 0 && x + i < image.getWidth() && y + j >= 0 && y + j < image.getHeight()) {
            Color currentColor = new Color(image.getRGB(x + i, y + j));
            return getValue(i + getHeight() / 2, j + getWidth() / 2) * currentColor.getRed();
        } else
            return 0;
    }

    private int getBlueComponent(int x, int y, int i, int j, BufferedImage image) {
        if (x + i >= 0 && x + i < image.getWidth() && y + j >= 0 && y + j < image.getHeight()) {
            Color currentColor = new Color(image.getRGB(x + i, y + j));
            return getValue(i + getHeight() / 2, j + getWidth() / 2) * currentColor.getBlue();
        } else
            return 0;
    }
}
