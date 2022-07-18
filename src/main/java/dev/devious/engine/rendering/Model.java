package dev.devious.engine.rendering;

import org.joml.Vector3i;

public class Model {
	private int id;
	private int vertexCount;
	private Vector3i size;
	private Texture texture;

	public Model(int id, int vertexCount, Vector3i size) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.size = size;
	}

	public Model(int id, int vertexCount, Texture texture) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.texture = texture;
	}

	public Model(Model model, Texture texture) {
		this.texture = texture;
		this.vertexCount = model.getVertexCount();
		this.id = model.getId();
	}

	public int getId() {
		return id;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vector3i getSize() {
		return size;
	}
}
