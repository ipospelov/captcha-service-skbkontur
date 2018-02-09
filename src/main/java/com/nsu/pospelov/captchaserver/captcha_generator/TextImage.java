package com.nsu.pospelov.captchaserver.captcha_generator;

import com.nsu.pospelov.captchaserver.captcha_generator.filters.model.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextImage {

    private BufferedImage image;

    public TextImage(int width, int height, String text) {

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        imageInit(text);

    }

    public TextImage(int width, int height) {

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public void imageInit(String text) {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        drawText(text);

    }

    private void drawText(String text) {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setPaint(Color.BLACK);
        graphics.setFont(new Font("Monospace", Font.BOLD, 45));
        FontMetrics fm = graphics.getFontMetrics();
        int x = (image.getWidth() - fm.stringWidth(text)) / 2;
        int y = (image.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        graphics.drawString(text, x, y);
    }

    public BufferedImage getImage() {
        return image;
    }


    public void applyFilter(Filter... filter) {
        for (int i = 0; i < filter.length; i++) {
            filter[i].apply(image);
        }

    }
}
