package dev.devious.engine;

import dev.devious.engine.rendering.Model;
import dev.devious.engine.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();

	public Model loadOBJModel(String filename) {
		List<String> lines = Utils.readAllLines(filename);

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3i> faces = new ArrayList<>();

		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
				case "v":
					Vector3f verticesVec = new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])
					);
					vertices.add(verticesVec);
					break;
				case "vt":
					Vector2f texturesVec = new Vector2f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2])
					);
					textures.add(texturesVec);
					break;
				case "vn":
					Vector3f normalsVec = new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])
					);
					normals.add(normalsVec);
					break;
				case "f":
					processFace(tokens[1], faces);
					processFace(tokens[2], faces);
					processFace(tokens[3], faces);
					break;
				default:
					break;
			}
		}

		List<Integer> indices = new ArrayList<>();
		float[] verticesArray = new float[vertices.size() * 3];
		int i = 0;
		for (Vector3f pos : vertices) {
			verticesArray[i * 3] = pos.x;
			verticesArray[i * 3 + 1] = pos.y;
			verticesArray[i * 3 + 2] = pos.z;
			i++;
		}

		float[] textCoordArr = new float[vertices.size() * 2];
		float[] normalArr = new float[vertices.size() * 3];

		for (Vector3i face : faces) {
			processVertex(face.x, face.y, face.z, textures, normals, indices, textCoordArr, normalArr);
		}

		int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		float[] lightData = new float[2];

		return loadModel(verticesArray, textCoordArr, normalArr, indicesArr, lightData);
	}

	private static void processVertex(
			int pos,
			int texCoord,
			int normal,
			List<Vector2f> texCoordList,
			List<Vector3f> normalList,
			List<Integer> indicesList,
			float[] texCoordArr,
			float[] normalArr
	) {
		indicesList.add(pos);

		if (texCoord >= 0) {
			Vector2f texCoordVec = texCoordList.get(texCoord);
			texCoordArr[pos * 2] = texCoordVec.x;
			texCoordArr[pos * 2 + 1] = 1 - texCoordVec.y;
		}

		if (normal >= 0) {
			Vector3f normalVec = normalList.get(normal);
			normalArr[pos * 3] = normalVec.x;
			normalArr[pos * 3 + 1] = normalVec.y;
			normalArr[pos * 3 + 2] = normalVec.z;
		}
	}

	private static void processFace(String token, List<Vector3i> faces) {
		String[] lineToken = token.split("/");
		int length = lineToken.length;
		int pos, coords = -1, normal = -1;
		pos = Integer.parseInt(lineToken[0]) - 1;

		if (length > 1) {
			String textCoord = lineToken[1];
			coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
			if (length > 2) normal = Integer.parseInt(lineToken[2]) - 1;
		}

		Vector3i facesVec = new Vector3i(pos, coords, normal);
		faces.add(facesVec);
	}

	public Model loadModel(
		float[] vertices,
		float[] textureCoords,
		float[] normals,
		int[] indices,
		float[] lightData
	) {
		int id = createVAO();
		storeIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 2, lightData);
		unbind();
		return new Model(id, indices.length, new Vector3i());
	}

	public int loadTexture(String filename) throws Exception {
		int width;
		int height;

		ByteBuffer buffer;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);

			buffer = STBImage.stbi_load(filename, w, h, c, 4);
			if (buffer == null) {
				throw new Exception("Texture image " + filename + " was not loaded. Error: " + STBImage.stbi_failure_reason());
			}

			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.4f);

			width = w.get();
			height = h.get();
		}

		int id = GL11.glGenTextures();
		textures.add(id);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		STBImage.stbi_image_free(buffer);

		return id;
}

	public int createVAO() {
		int id = GL30.glGenVertexArrays();
		vaos.add(id);
		GL30.glBindVertexArray(id);
		return id;
	}

	public void storeIndicesBuffer(int[] indices) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	public void storeDataInAttributeList(int index, int vertexCount, float[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		var buffer = Utils.storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, vertexCount, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbind() {
		GL30.glBindVertexArray(0);
	}

	public void cleanup() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}

		for (int vbo : vbos) {
			GL30.glDeleteBuffers(vbo);
		}

		for (int texture : textures) {
			GL30.glDeleteTextures(texture);
		}
	}
}
