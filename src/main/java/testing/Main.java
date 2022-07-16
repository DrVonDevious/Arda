package testing;

import dev.devious.engine.EngineManager;
import dev.devious.engine.graphics.WindowManager;
import dev.devious.engine.graphics.WindowSettings;
import dev.devious.engine.graphics.camera.Camera;
import dev.devious.engine.graphics.camera.CameraSettings;

public class Main {
	public static void main(String[] args) {
		Camera camera = new Camera(new CameraSettings(70, 0.01f, 1000f, 0.1f));
		WindowManager window = new WindowManager(
			new WindowSettings(1080, 720, "Arda", true, false),
				camera
		);

		Game game;
		try {
			game = new Game(window, camera);
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
