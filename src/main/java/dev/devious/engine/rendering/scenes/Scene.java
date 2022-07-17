package dev.devious.engine.rendering.scenes;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.World;
import dev.devious.engine.rendering.Terrain;
import dev.devious.engine.lighting.Light;
import dev.devious.engine.rendering.RenderManager;
import dev.devious.engine.rendering.WindowManager;
import dev.devious.engine.rendering.camera.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	private Camera camera;
	private WindowManager window;
	private List<Entity> entities;
	private List<Terrain> terrains;
	private World world;
	private List<Light> lights;

	public Scene(WindowManager window, Camera camera) {
		this.entities = new ArrayList<>();
		this.terrains = new ArrayList<>();
		this.lights = new ArrayList<>();
		this.camera = camera;
		this.window = window;
	}

	public void render(RenderManager renderManager) {
		renderManager.render(entities, terrains, camera, lights);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void addLight(Light light) {
		lights.add(light);
	}
}
