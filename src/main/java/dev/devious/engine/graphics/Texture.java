package dev.devious.engine.graphics;

public class Texture {
	public final int id;

	private float shineDamper = 1;
	private float specularity = 0;

	private boolean hasTransparency = false;
	private boolean hasFakeLighting = false;

	public Texture(int id) {
		this.id = id;
	}

	public boolean isHasFakeLighting() {
		return hasFakeLighting;
	}

	public void setHasFakeLighting(boolean hasFakeLighting) {
		this.hasFakeLighting = hasFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getId() {
		return id;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getSpecularity() {
		return specularity;
	}

	public void setSpecularity(float specularity) {
		this.specularity = specularity;
	}
}
