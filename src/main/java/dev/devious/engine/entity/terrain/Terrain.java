package dev.devious.engine.entity.terrain;

import dev.devious.engine.ObjectLoader;
import dev.devious.engine.entity.Entity;
import dev.devious.engine.entity.Model;
import dev.devious.engine.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Terrain {
	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;

	private float xSize;
	private float zSize;
	private Model model;
	private Texture texture;

	public Terrain(int xSize, int zSize, ObjectLoader loader, Texture texture) {
		this.texture = texture;
		this.xSize = xSize;
		this.zSize = zSize;
		this.model = generateTerrain(loader);
		model.setTexture(texture);
	}

	private Model generateTerrain(ObjectLoader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = j / ((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = i / ((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = j / ((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = i / ((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadModel(vertices, textureCoords, normals, indices, new float[2]);
	}

	public static void processTerrain(Map<Model, List<Terrain>> terrains, Terrain terrain) {
		Model model = terrain.getModel();
		List<Terrain> batch = terrains.get(model);
		if (batch != null) {
			batch.add(terrain);
		} else {
			List<Terrain> newBatch = new ArrayList<>();
			newBatch.add(terrain);
			terrains.put(model, newBatch);
		}
	}

	public float getxSize() {
		return xSize;
	}

	public void setxSize(float xSize) {
		this.xSize = xSize;
	}

	public float getzSize() {
		return zSize;
	}

	public void setzSize(float zSize) {
		this.zSize = zSize;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
