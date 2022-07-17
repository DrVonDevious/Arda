package dev.devious.engine.rendering;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.components.Renderable;
import dev.devious.engine.ecs.components.Rotation;
import dev.devious.engine.rendering.camera.Camera;
import dev.devious.engine.rendering.shader.UniformManager;
import dev.devious.engine.utils.Transformation;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class EntityRenderer {
	UniformManager uniformManager;
	Camera camera;

	public EntityRenderer(UniformManager uniformManager, Camera camera) {
		this.uniformManager = uniformManager;
		this.camera = camera;
	}

	public void renderEntities(List<dev.devious.engine.ecs.Entity> entities) {
		for (Entity entity : entities) {
			if (entity.hasComponent(Renderable.class)) {
				Renderable renderable = (Renderable) entity.getComponent(Renderable.class);
				Model model = renderable.getModel();
				prepareModel(model);
				prepareInstance(entity);
				rotateBillboard(camera, entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				unbindModel();
			}
		}
	}

	private void rotateBillboard(Camera camera, Entity entity) {
		Vector3f cameraRotation = camera.getRotation();
		Rotation rotation = (Rotation) entity.getComponent(Rotation.class);
		rotation.setXYZ(new Vector3f(rotation.getX(), rotation.getY(), cameraRotation.y));
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
		Renderable renderable = (Renderable) entity.getComponent(Renderable.class);
		uniformManager.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
		uniformManager.setUniform("shineDamper", renderable.getModel().getTexture().getShineDamper());
		uniformManager.setUniform("specularity", renderable.getModel().getTexture().getSpecularity());
		uniformManager.setUniform("useFakeLighting", renderable.getModel().getTexture().isHasFakeLighting());
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
