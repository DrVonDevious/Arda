package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;
import org.joml.Vector3f;

public class Velocity extends Component {
	private float x;
	private float y;
	private float z;

	public Velocity(float x, float y, float z) {
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
		return new Vector3f(getX(), getY(), getZ());
	}

	public void setXYZ(Vector3f xyz) {
		this.x = xyz.x;
		this.y = xyz.y;
		this.z = xyz.z;
	}

	public void reduceVelocity() {
		if (x > 0) {
			x -= 0.1f;
		} else if (x < 0) {
			x += 0.1f;
		}

		if (z > 0) {
			z -= 0.1f;
		} else if (z < 0) {
			z += 0.1f;
		}
	}
}
