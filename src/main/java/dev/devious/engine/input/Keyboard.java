package dev.devious.engine.input;

import dev.devious.engine.rendering.WindowManager;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class Keyboard {
	private WindowManager windowManager;

	public Keyboard(WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	public int getKeycode(String key) {
		return switch (key.toLowerCase(Locale.ROOT)) {
			case ("w") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_W);
			case ("s") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_S);
			case ("a") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_A);
			case ("d") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_D);
			case ("q") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_Q);
			case ("e") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_E);
			case ("f") -> GLFW.glfwGetKey(windowManager.getWindow(), GLFW.GLFW_KEY_F);
			default -> 0;
		};
	}

	public boolean isKeyPressed(String key) {
		int keycode = getKeycode(key);
		return keycode == GLFW.GLFW_PRESS;
	}
}
