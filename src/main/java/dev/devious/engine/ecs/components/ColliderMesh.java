package dev.devious.engine.ecs.components;

import dev.devious.engine.rendering.Model;

public class ColliderMesh {
	private Model model;
	private int xSize;
	private int ySize;
	private int zSize;

	public ColliderMesh(Model model, int xSize, int ySize, int zSize) {
		this.model = model;
		this.xSize = xSize == 0 ? model.getId() : xSize;
		this.ySize = ySize == 0 ? model.getId() : ySize;
		this.zSize = zSize == 0 ? model.getId() : zSize;
	}
}
