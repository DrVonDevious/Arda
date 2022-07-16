package dev.devious.engine.graphics.camera;

public class CameraSettings {

	/**
	 * The total viewing angle of a Camera
	 */
	public final float fov;

	/**
	 * The minimum render distance for a Camera
	 */
	public final float nearPlane;

	/**
	 * The maximum render distance for a Camera
	 */
	public final float farPlane;

	/**
	 * The movement speed of a Camera
	 */
	public final float speed;

	/**
	 *
	 * @param fov
	 * @param nearPlane
	 * @param farPlane
	 */
	public CameraSettings(float fov, float nearPlane, float farPlane, float speed) {
		this.fov = (float) Math.toRadians(fov);
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.speed = speed;
	}
}
