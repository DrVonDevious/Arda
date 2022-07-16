package dev.devious.engine.entity;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entity {
	private Model model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;

	public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void move(Vector3f destination) {
		this.position.x += destination.x;
		this.position.y += destination.y;
		this.position.z += destination.z;
	}

	public void rotate(Vector3f rotation) {
		this.rotation.x += rotation.x;
		this.rotation.y += rotation.y;
		this.rotation.z += rotation.z;
	}

	public static void processEntity(Map<Model, List<Entity>> entities, Entity entity) {
		Model model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}
}
