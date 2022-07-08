package dev.devious.test;

import dev.devious.engine.WindowManager;

public class Main {
    public static void main(String[] args) {
        WindowManager window = new WindowManager("Arda", 1600, 900, false);
        window.init();

        while (!window.shouldWindowClose()) window.update();

        window.cleanup();
    }
}
