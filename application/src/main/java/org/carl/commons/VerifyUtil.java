package org.carl.commons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * The returned array contains the generated verification code as the first parameter and the generated image as the second parameter.
 * Object[] objs = VerifyUtil.newBuilder().build().createImage();
 * Customize the settings according to your needs to achieve personalization.
 * The returned array contains the generated verification code as the first parameter and the generated image as the second parameter.
 * Object[] objs = VerifyUtil.newBuilder()
 *         .setWidth(120)   // Set the width of the image
 *         .setHeight(35)   // Set the height of the image
 *         .setSize(6)      // Set the number of characters
 *         .setLines(10)    // Set the number of interference lines
 *         .setFontSize(25) // Set the font size
 *         .setTilt(true)   // Set whether tilting is required
 *         .setBackgroundColor(Color.WHITE) // Set the background color of the verification code
 *         .build()         // Build the VerifyUtil project
 *         .createImage();  // Generate the image
 */

public class VerifyUtil {
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final Integer SIZE;
    private final int LINES;
    private final int WIDTH;
    private final int HEIGHT;
    private final int FONT_SIZE;
    private final boolean TILT;

    private final Color BACKGROUND_COLOR;

    private VerifyUtil(Builder builder) {
        SIZE = builder.size;
        LINES = builder.lines;
        WIDTH = builder.width;
        HEIGHT = builder.height;
        FONT_SIZE = builder.fontSize;
        TILT = builder.tilt;
        BACKGROUND_COLOR = builder.backgroundColor;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Object[] createImage() {
        StringBuilder sb = new StringBuilder();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image.createGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.setColor(BACKGROUND_COLOR);
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        Random ran = new Random();

        //graphic.setBackground(Color.WHITE);

        int codeWidth = WIDTH / (SIZE + 1);
        int y = HEIGHT * 3 / 4;

        for (int i = 0; i < SIZE; i++) {
            graphic.setColor(getRandomColor());
            Font font = new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE);

            if (TILT) {
                int theta = ran.nextInt(45);
                theta = (ran.nextBoolean()) ? theta : -theta;
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.rotate(Math.toRadians(theta), 0, 0);
                font = font.deriveFont(affineTransform);
            }
            graphic.setFont(font);

            int x = (i * codeWidth) + (codeWidth / 2);

            int n = ran.nextInt(chars.length);
            String code = String.valueOf(chars[n]);
            graphic.drawString(code, x, y);

            sb.append(code);
        }
        for (int i = 0; i < LINES; i++) {
            graphic.setColor(getRandomColor());
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        return new Object[]{sb.toString(), image};
    }

    private Color getRandomColor() {
        Random ran = new Random();
      return new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    }

    public static class Builder {
        private int size = 6;
        private int lines = 10;
        private int width = 80;
        private int height = 35;
        private int fontSize = 25;
        private boolean tilt = true;
        private Color backgroundColor = Color.LIGHT_GRAY;

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setLines(int lines) {
            this.lines = lines;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder setTilt(boolean tilt) {
            this.tilt = tilt;
            return this;
        }

        public Builder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public VerifyUtil build() {
            return new VerifyUtil(this);
        }
    }
}
