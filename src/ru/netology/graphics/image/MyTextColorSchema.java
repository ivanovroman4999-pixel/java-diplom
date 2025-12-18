package ru.netology.graphics.image;

public class MyTextColorSchema implements TextColorSchema {
    // Набор символов от самого темного (плотного) к самому светлому
    private static final char[] SYMBOLS = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        // Делим весь диапазон 256 на количество наших символов
        return SYMBOLS[color * SYMBOLS.length / 256];
    }
}