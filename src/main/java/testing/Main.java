package testing;

import dev.devious.engine.EngineManager;
import dev.devious.engine.input.Keyboard;
import dev.devious.engine.rendering.WindowManager;
import dev.devious.engine.rendering.WindowSettings;
import dev.devious.engine.rendering.camera.Camera;
import dev.devious.engine.rendering.camera.CameraSettings;

public class Main {
	public static void main(String[] args) {
		Camera camera = new Camera(new CameraSettings(70, 0.01f, 1000f, 1.4f));
		WindowManager window = new WindowManager(
			new WindowSettings(1080, 720, "Arda", true, false),
				camera
		);
		Keyboard keyboard = new Keyboard(window);

		Game game;
		try {
			game = new Game(window, camera, keyboard);
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize game object: " + e);
		}

		EngineManager engine = new EngineManager(window, game);

		try {
			engine.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
