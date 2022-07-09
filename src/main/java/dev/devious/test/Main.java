package dev.devious.test;

import dev.devious.engine.EngineManager;
import dev.devious.engine.WindowManager;
import dev.devious.engine.utils.Constants;

public class Main {

    private static WindowManager window;
    private static Game game;

    public static void main(String[] args) {
        window = new WindowManager(Constants.TITLE, 1600, 900, false);
        game = new Game();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static Game getGame() {
        return game;
    }
}
