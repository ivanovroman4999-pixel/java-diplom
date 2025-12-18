package ru.netology.graphics;

import ru.netology.graphics.image.MyTextGraphicsConverter;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
     public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new MyTextGraphicsConverter(); // Создаем ваш объект

        GServer server = new GServer(converter); // Передаем серверу
        server.start(); // Запускаем
    }
}

