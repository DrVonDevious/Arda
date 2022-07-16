package dev.devious.engine.input;

import dev.devious.engine.graphics.WindowManager;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Mouse {
	private WindowManager window;
	private final Vector2d previousPosition;
	private final Vector2d currentPosition;

	private final Vector2f displayVector;

	private boolean isInWindow = false;
	private boolean isLeftButtonPressed = false;
	private boolean isRightButtonPressed = false;

	public Mouse(WindowManager window) {
		this.window = window;
		previousPosition = new Vector2d(-1, -1);
		currentPosition = new Vector2d(0, 0);
		displayVector = new Vector2f();
	}

	public void init() {
		GLFW.glfwSetCursorPosCallback(window.getWindow(), (window, xPosition, yPosition) -> {
			currentPosition.x = xPosition;
			currentPosition.y = yPosition;
		});

		GLFW.glfwSetCursorEnterCallback(window.getWindow(), (window, isEntered) -> {
			isInWindow = isEntered;
		});

		GLFW.glfwSetMouseButtonCallback(window.getWindow(), (window, button, action, mods) -> {
			isLeftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
			isRightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
		});
	}

	public void input() {
		displayVector.x = 0;
		displayVector.y = 0;

		if (previousPosition.x > 0 && previousPosition.y > 0 && isInWindow) {
			double x = currentPosition.x - previousPosition.x;
			double y = currentPosition.y - previousPosition.y;

			boolean rotateX = x != 0;
			boolean rotateY = y != 0;

			if (rotateX) displayVector.y = (float) x;
			if (rotateY) displayVector.x = (float) y;
		}

		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}

	public boolean isLeftButtonPressed() {
		return isLeftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return isRightButtonPressed;
	}

	public Vector2f getDisplayVector() {
		return displayVector;
	}
}
