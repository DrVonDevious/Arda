package dev.devious.engine.rendering;

import dev.devious.engine.rendering.shader.UniformManager;
import dev.devious.engine.utils.Transformation;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class TerrainRenderer {
	UniformManager uniformManager;

	public TerrainRenderer(UniformManager uniformManager) {
		this.uniformManager = uniformManager;
	}

	public void renderTerrains(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareModel(terrain.getModel());
			prepareInstance(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindModel();
		}
	}

	private void prepareModel(Model model) {
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
	}

	private void prepareInstance(Terrain terrain) {
		uniformManager.setUniform("transformationMatrix", Transformation.createTransformationTerrainMatrix(terrain));
		uniformManager.setUniform("shineDamper", terrain.getTexture().getShineDamper());
		uniformManager.setUniform("specularity", terrain.getTexture().getSpecularity());
		uniformManager.setUniform("skyColor", new Vector3f(0.4f, 0.8f, 1));
	}

	private void unbindModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
