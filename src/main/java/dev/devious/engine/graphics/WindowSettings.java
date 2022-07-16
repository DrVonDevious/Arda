package dev.devious.engine.graphics;

public class WindowSettings {
	private int defaultWidth;
	private int defaultHeight;
	private String title;
	private boolean enableVSync;
	private boolean enableResize;

	public WindowSettings(int defaultWidth, int defaultHeight, String title, boolean enableVSync, boolean enableResize) {
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.title = title;
		this.enableVSync = enableVSync;
		this.enableResize = enableResize;
	}

	public WindowSettings() {

	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public int getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(int defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEnableVSync() {
		return enableVSync;
	}

	public void setEnableVSync(boolean enableVSync) {
		this.enableVSync = enableVSync;
	}

	public boolean isEnableResize() {
		return enableResize;
	}

	public void setEnableResize(boolean enableResize) {
		this.enableResize = enableResize;
	}

	public WindowSettings DefaultWindowSettings() {
		return new WindowSettings(800, 500, "Game Window", true, false);
	}
}
