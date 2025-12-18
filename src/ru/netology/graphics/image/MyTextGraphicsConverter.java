package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class MyTextGraphicsConverter implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema schema = new MyTextColorSchema();

    @Override
    public String convert(String url) throws BadImageSizeException, IOException {
        // 1. Скачиваем изображение
        BufferedImage img = ImageIO.read(new URL(url));

        // 2. Проверка максимально допустимого соотношения сторон
        double imgRatio = (double) img.getWidth() / img.getHeight();
        if (maxRatio != 0 && imgRatio > maxRatio) {
            throw new BadImageSizeException(imgRatio, maxRatio);
        }

        // 3. Расчет новых размеров (масштабирование)
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        // Если картинка больше заданных лимитов, вычисляем коэффициент уменьшения
        double widthRatio = (width != 0) ? (double) img.getWidth() / width : 1;
        double heightRatio = (height != 0) ? (double) img.getHeight() / height : 1;
        double ratio = Math.max(widthRatio, heightRatio);

        if (ratio > 1) {
            newWidth = (int) (img.getWidth() / ratio);
            newHeight = (int) (img.getHeight() / ratio);
        }

        // 4. Ресайз изображения
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        // 5. Конвертация в текстовые символы
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder sb = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                // Добавляем символ дважды, чтобы картинка не была слишком узкой (текстовые символы обычно выше, чем шире)
                sb.append(c).append(c);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}