package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;
import dev.devious.engine.input.Keyboard;
import dev.devious.engine.rendering.camera.Camera;

public class PlayerController extends Component {
	private Keyboard keyboard;
	private Camera camera;

	public PlayerController(Keyboard keyboard, Camera camera) {
		this.keyboard = keyboard;
		this.camera = camera;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
