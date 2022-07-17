package dev.devious.engine.rendering;

import dev.devious.engine.rendering.camera.Camera;
import dev.devious.engine.rendering.shader.ShaderManager;
import dev.devious.engine.rendering.shader.UniformManager;
import dev.devious.engine.lighting.Light;
import dev.devious.engine.utils.Utils;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderManager {
	private final WindowManager window;
	private final Camera camera;

	public ShaderManager entityShader;
	public ShaderManager terrainShader;
	public EntityRenderer entityRenderer;
	public TerrainRenderer terrainRenderer;
	public UniformManager uniformManager;
	public UniformManager uniformManagerTerrain;

	public RenderManager(WindowManager window, Camera camera) {
		this.window = window;
		this.camera = camera;
	}

	public void init() throws Exception {
		entityShader = new ShaderManager();
		terrainShader = new ShaderManager();

		entityShader.createVertexShader(Utils.loadResource("/shaders/vertexEntityShader.glsl"));
		entityShader.createFragmentShader(Utils.loadResource("/shaders/fragmentEntityShader.glsl"));
		terrainShader.createVertexShader(Utils.loadResource("/shaders/vertexTerrainShader.glsl"));
		terrainShader.createFragmentShader(Utils.loadResource("/shaders/fragmentTerrainShader.glsl"));

		entityShader.link();
		terrainShader.link();

		uniformManager = new UniformManager(entityShader, window);
		uniformManagerTerrain = new UniformManager(terrainShader, window);
		entityRenderer = new EntityRenderer(uniformManager, camera);
		terrainRenderer = new TerrainRenderer(uniformManagerTerrain);

		uniformManager.createAllUniforms(entityShader.getProgramId(), "entity");
		uniformManagerTerrain.createAllUniforms(terrainShader.getProgramId(), "terrain");
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<dev.devious.engine.ecs.Entity> entities, List<Terrain> terrains, Camera camera, List<Light> lights) {
		clear();

		entityShader.bind();
		uniformManager.setAllUniforms(camera, lights.get(0));
		entityRenderer.renderEntities(entities);
		entityShader.unbind();

		terrainShader.bind();
		uniformManagerTerrain.setAllUniforms(camera, lights.get(0));
		terrainRenderer.renderTerrains(terrains);
		terrainShader.unbind();
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void cleanup() {
		entityShader.cleanup();
		terrainShader.cleanup();
	}
}
