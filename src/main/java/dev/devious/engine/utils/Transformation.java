package dev.devious.engine.utils;

import dev.devious.engine.entity.Entity;
import dev.devious.engine.entity.terrain.Terrain;
import dev.devious.engine.graphics.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	public static Matrix4f createTransformationMatrix(Entity entity) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(entity.getPosition());
		matrix.rotateX((float) Math.toRadians(entity.getRotation().x));
		matrix.rotateY((float) Math.toRadians(entity.getRotation().y));
		matrix.rotateZ((float) Math.toRadians(entity.getRotation().z));
		matrix.scale(entity.getScale());
		return matrix;
	}

	public static Matrix4f createTransformationTerrainMatrix(Terrain terrain) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(new Vector3f(terrain.getxSize(), 0, terrain.getzSize()));
		matrix.rotateX((float) Math.toRadians(0));
		matrix.rotateY((float) Math.toRadians(0));
		matrix.rotateZ((float) Math.toRadians(0));
		matrix.scale(1);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Vector3f position = camera.getPosition();
		Vector3f rotation = camera.getRotation();
		Matrix4f matrix = new Matrix4f();

		matrix.identity();
		matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
		matrix.translate(-position.x, -position.y, -position.z);
		return matrix;
	}
}
