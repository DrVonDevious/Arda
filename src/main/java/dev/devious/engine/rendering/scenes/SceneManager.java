package dev.devious.engine.rendering.scenes;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
	private List<Scene> scenes;
	private Scene activeScene;

	public SceneManager() {
		this.scenes = new ArrayList<>();
	}

	public void setActiveScene(Scene scene) {
		this.activeScene = scene;
	}

	public Scene getActiveScene() {
		return this.activeScene;
	}
}
