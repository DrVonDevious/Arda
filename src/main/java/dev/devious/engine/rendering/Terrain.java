package dev.devious.engine.rendering;

import dev.devious.engine.ObjectLoader;
import dev.devious.engine.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Terrain {
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private float xSize;
	private float zSize;
	private Model model;
	private Texture texture;

	private float[][] vertexHeights;

	public Terrain(int xSize, int zSize, ObjectLoader loader, Texture texture, String heightMap) {
		this.texture = texture;
		this.xSize = xSize;
		this.zSize = zSize;
		this.model = generateTerrain(loader, heightMap);
		model.setTexture(texture);
	}

	private Model generateTerrain(ObjectLoader loader, String heightMap){
		BufferedImage image;
		try {
			image = ImageIO.read(new File("src/main/resources/heightmaps/" + heightMap + ".png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		int VERTEX_COUNT = image.getHeight();
		vertexHeights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = j / ((float)VERTEX_COUNT - 1) * SIZE;
				float vertexHeight = getHeight(j, i, image);
				vertexHeights[j][i] = vertexHeight;
				vertices[vertexPointer*3+1] = vertexHeight;
				vertices[vertexPointer*3+2] = i / ((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
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

	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightU = getHeight(x, z - 1, image);
		float heightD = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalize();
		return normal;
	}

	public float getVertexHeight(float x, float z) {
		x = x - this.xSize;
		z = z - this.zSize;
		float gridSize = SIZE / ((float) vertexHeights.length - 1);
		int gridX = (int) Math.floor(x / gridSize);
		int gridZ = (int) Math.floor(z / gridSize);

		if (gridX >= vertexHeights.length - 1 || gridZ >= vertexHeights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (x % gridSize) / gridSize;
		float zCoord = (z % gridSize) / gridSize;
		float vertexHeight;

		if (xCoord <= (1 - zCoord)) {
			vertexHeight = Utils.getBarrycentric(
				new Vector3f(0, vertexHeights[gridX][gridZ], 0),
				new Vector3f(1, vertexHeights[gridX + 1][gridZ], 0),
				new Vector3f(0, vertexHeights[gridX][gridZ + 1], 1),
				new Vector2f(xCoord, zCoord)
			);
		} else {
			vertexHeight = Utils.getBarrycentric(
				new Vector3f(1, vertexHeights[gridX + 1][gridZ], 0),
				new Vector3f(1, vertexHeights[gridX + 1][gridZ + 1], 1),
				new Vector3f(0, vertexHeights[gridX][gridZ + 1], 1),
				new Vector2f(xCoord, zCoord)
			);
		}

		return vertexHeight;
	}

	private float getHeight(int x, int y, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
			return 0;
		}

		float height = image.getRGB(x, y);

		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;

		return height;
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
