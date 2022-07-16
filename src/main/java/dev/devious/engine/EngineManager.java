package dev.devious.engine;

import dev.devious.engine.graphics.WindowManager;
import dev.devious.engine.input.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
	public static final long NANOSECOND = 1000000000L;
	public static final float FRAMERATE = 1000;

	private static int fps;
	private static float frametime = 1.0f / FRAMERATE;

	private boolean isRunning;

	private WindowManager window;
	private Mouse mouse;
	private GLFWErrorCallback errorCallback;
	private ILogic gameLogic;

	public EngineManager(WindowManager window, ILogic gameLogic) {
		this.window = window;
		this.gameLogic = gameLogic;
	}

	private void init() throws Exception {
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		window.init();
		gameLogic.init();
		mouse = new Mouse(window);
		mouse.init();
	}

	public void start() throws Exception {
		init();
		if (isRunning) return;
		run();
	}

	public void run() {
		this.isRunning = true;
		int frames = 0;
		long frameCounter = 0;
		long lastTime = System.nanoTime();
		double unprocessedTime = 0;

		while (isRunning) {
			boolean render = false;
			long startTime = System.nanoTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) NANOSECOND;
			frameCounter += passedTime;

			input();

			while (unprocessedTime > frametime) {
				render = true;
				unprocessedTime -= frametime;

				if (window.windowShouldClose()) {
					stop();
				}

				if (frameCounter >= NANOSECOND) {
					setFps(frames);
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render) {
				update();
				render();
				frames++;
			}
		}

		cleanup();
	}

	private void stop() {
		if (!isRunning) return;
		isRunning = false;
	}

	private void input() {
		mouse.input();
		gameLogic.input();
	}

	private void render() {
		gameLogic.render();
		window.update();
	}

	private void update() {
		gameLogic.update(mouse);
	}

	private void cleanup() {
		window.cleanup();
		gameLogic.cleanup();
		errorCallback.free();
		GLFW.glfwTerminate();
	}

	public static int getFps() {
		return fps;
	}

	public static void setFps(int fps) {
		EngineManager.fps = fps;
	}
}
