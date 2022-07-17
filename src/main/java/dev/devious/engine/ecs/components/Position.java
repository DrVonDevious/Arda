package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;
import org.joml.Vector3f;

public class Position extends Component {
	private float x;
	private float y;
	private float z;

	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Vector3f getXYZ() {
		return new Vector3f(x, y, z);
	}

	public void setXYZ(Vector3f xyz) {
		this.x = xyz.x;
		this.y = xyz.y;
		this.z = xyz.z;
	}
}
