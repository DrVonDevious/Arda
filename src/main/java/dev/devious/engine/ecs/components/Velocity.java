package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;
import org.joml.Vector3f;

public class Velocity extends Component {
	private float x;
	private float y;
	private float z;
	private Vector3f xyz;

	public Velocity(float x, float y, float z, Vector3f xyz) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xyz = xyz;
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

	public Vector3f getXyz() {
		return xyz;
	}

	public void setXyz(Vector3f xyz) {
		this.xyz = xyz;
	}
}
