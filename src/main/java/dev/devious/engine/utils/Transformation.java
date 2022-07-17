package dev.devious.engine.utils;

import dev.devious.engine.ecs.components.Position;
import dev.devious.engine.ecs.components.Renderable;
import dev.devious.engine.ecs.components.Rotation;
import dev.devious.engine.rendering.Terrain;
import dev.devious.engine.rendering.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	public static Matrix4f createTransformationMatrix(dev.devious.engine.ecs.Entity entity) {
		Position position = (Position) entity.getComponent(Position.class);
		Rotation rotation = (Rotation) entity.getComponent(Rotation.class);
		Renderable renderable = (Renderable) entity.getComponent(Renderable.class);
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(new Vector3f(position.getX(), position.getY(), position.getZ()));
		matrix.rotateX((float) Math.toRadians(rotation.getX()));
		matrix.rotateY((float) Math.toRadians(rotation.getY()));
		matrix.rotateZ((float) Math.toRadians(rotation.getZ()));
		matrix.scale(renderable.getScale());
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
