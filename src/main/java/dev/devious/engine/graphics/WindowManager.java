package dev.devious.engine.graphics;

import dev.devious.engine.graphics.camera.Camera;
import dev.devious.engine.graphics.camera.CameraSettings;
import dev.devious.engine.utils.Color;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {
	private WindowSettings windowSettings;
	private CameraSettings cameraSettings;
	private int windowWidth;
	private int windowHeight;
	private boolean isResized;
	private String title;
	private boolean isVSyncEnabled;
	private long window;
	private Matrix4f projectionMatrix;

	public WindowManager(WindowSettings windowSettings, Camera camera) {
		this.windowSettings = windowSettings;
		this.cameraSettings = camera.getSettings();
		windowWidth = this.windowSettings.getDefaultWidth();
		windowHeight = this.windowSettings.getDefaultHeight();
		title = this.windowSettings.getTitle();
		projectionMatrix = new Matrix4f();
		isVSyncEnabled = this.windowSettings.isEnableVSync();
	}

	public void setIsResized(boolean resized) {
		isResized = resized;
	}

	public boolean isResized() {
		return isResized;
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

		boolean isMaximized = false;
		if (windowWidth == 0 || windowHeight == 0) {
			windowWidth = 100;
			windowHeight = 100;
			GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
			isMaximized = true;
		}

		window = GLFW.glfwCreateWindow(
			windowWidth,
			windowHeight,
			windowSettings.getTitle(),
			MemoryUtil.NULL,
			MemoryUtil.NULL
		);

		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("Unable to create GLFW window");
		}

		GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.windowWidth = width;
			this.windowHeight = height;
			this.setIsResized(true);
		});

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});

		if (isMaximized) {
			GLFW.glfwMaximizeWindow(window);
		} else {
			GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(
				window,
				(videoMode.width() - windowWidth) / 2,
				(videoMode.height() - windowHeight) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(window);

		if (isVSyncEnabled) {
			GLFW.glfwSwapInterval(1);
		}

		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
		GL11.glClearColor(0.4f, 0.8f, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
	}

	public void update() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}

	public void cleanup() {
		GLFW.glfwDestroyWindow(window);
	}

	public void setClearColor(Color color) {
		GL11.glClearColor(color.r, color.g, color.b, color.a);
	}

	public boolean isKeyPressed(int keycode) {
		return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
	}

	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public boolean isVSyncEnabled() {
		return isVSyncEnabled;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public String getTitle() {
		return title;
	}

	public long getWindow() {
		return window;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f updateProjectionMatrix() {
		float aspectRatio = (float) windowWidth / windowHeight;
		return projectionMatrix.setPerspective(
			cameraSettings.fov,
			aspectRatio,
			cameraSettings.nearPlane,
			cameraSettings.farPlane
		);
	}

	public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
		float aspectRatio = (float) width / height;
		return matrix.setPerspective(
				cameraSettings.fov,
				aspectRatio,
				cameraSettings.nearPlane,
				cameraSettings.farPlane
		);
	}
}
