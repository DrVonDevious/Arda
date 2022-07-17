package dev.devious.engine.rendering.camera;

import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Vector3f rotation;

	private CameraSettings cameraSettings;

	public Camera(CameraSettings cameraSettings) {
		this.position = new Vector3f(0, 2, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.cameraSettings = cameraSettings;
	}

	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public void move(float dx, float dy, float dz) {
		Vector3f destination = new Vector3f(
			dx * cameraSettings.speed,
			dy * cameraSettings.speed,
			dz * cameraSettings.speed
		);

		if (destination.z != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * destination.z;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * destination.z;
		}

		if (destination.x != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * destination.x;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * destination.x;
		}

		position.y += destination.y;
	}

	public void rotate(Vector3f rotation) {
		this.rotation.x += rotation.x;
		this.rotation.y += rotation.y;
		this.rotation.z += rotation.z;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public CameraSettings getSettings() {
		return cameraSettings;
	}
}
