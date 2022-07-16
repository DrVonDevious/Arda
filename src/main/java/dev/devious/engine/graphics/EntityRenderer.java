package dev.devious.engine.graphics;

import dev.devious.engine.entity.Entity;
import dev.devious.engine.entity.Model;
import dev.devious.engine.graphics.camera.Camera;
import dev.devious.engine.graphics.shader.UniformManager;
import dev.devious.engine.utils.Transformation;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class EntityRenderer {
	UniformManager uniformManager;
	Camera camera;

	public EntityRenderer(UniformManager uniformManager, Camera camera) {
		this.uniformManager = uniformManager;
		this.camera = camera;
	}

	public void renderEntities(Map<Model, List<Entity>> entities) {
		for (Model model : entities.keySet()) {
			prepareModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				rotateBillboard(camera, entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindModel();
		}
	}

	private void rotateBillboard(Camera camera, Entity entity) {
		Vector3f cameraRotation = camera.getRotation();
		entity.setRotation(new Vector3f(entity.getRotation().x, entity.getRotation().y, cameraRotation.y));
	}

	private void prepareModel(Model model) {
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		if (model.getTexture().isHasTransparency()) {
			RenderManager.disableCulling();
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
	}

	private void prepareInstance(Entity entity) {
		uniformManager.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
		uniformManager.setUniform("shineDamper", entity.getModel().getTexture().getShineDamper());
		uniformManager.setUniform("specularity", entity.getModel().getTexture().getSpecularity());
		uniformManager.setUniform("useFakeLighting", entity.getModel().getTexture().isHasFakeLighting());
		uniformManager.setUniform("skyColor", new Vector3f(0.4f, 0.8f, 1));
	}

	private void unbindModel() {
		RenderManager.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
