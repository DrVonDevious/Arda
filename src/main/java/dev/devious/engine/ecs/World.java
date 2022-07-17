package dev.devious.engine.ecs;

import dev.devious.engine.lighting.Light;
import dev.devious.engine.rendering.RenderManager;
import dev.devious.engine.rendering.camera.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class World {
	private List<Entity> entities;
	private List<System> systems;
	private RenderManager renderManager;
	private Camera camera;
	private Light light;

	public World(RenderManager renderManager, Camera camera, Light light) {
		this.entities = new ArrayList<>();
		this.systems = new ArrayList<>();
		this.renderManager = renderManager;
		this.camera = camera;
		this.light = light;
	}

	public Entity createEntity() {
		Entity entity = new Entity(generateEntityId());
		entities.add(entity);
		return entity;
	}

	private static UUID generateEntityId() {
		return UUID.randomUUID();
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void addSystem(System system) {
		systems.add(system);
	}

	public void process() {
		for (System system : systems) {
			system.process();
		}
	}
}
