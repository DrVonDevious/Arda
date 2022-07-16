package dev.devious.engine.utils;

public class Color {
	public float r;
	public float g;
	public float b;
	public float a;

	public Color() {}

	public Color(float r, float g, float b) {
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = 1.0f;
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = a;
	}

	public float r() {
		return this.r;
	}

	public float g() {
		return this.r;
	}

	public float b() {
		return this.r;
	}

	public float a() {
		return this.r;
	}
}
