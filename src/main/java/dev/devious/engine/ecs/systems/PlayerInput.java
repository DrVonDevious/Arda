package dev.devious.engine.ecs.systems;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.System;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.PlayerController;
import dev.devious.engine.ecs.components.Velocity;
import dev.devious.engine.input.Keyboard;
import dev.devious.engine.rendering.camera.Camera;
import org.joml.Vector3f;

import java.util.List;

public class PlayerInput extends System {
	private World world;

	public PlayerInput(World world) {
		this.world = world;
	}

	public void process() {
		List<Entity> entities = world.getEntities();
		for (Entity entity : entities) {
			if (entity.hasComponent(PlayerController.class)) {
				PlayerController controller = (PlayerController) entity.getComponent(PlayerController.class);
				Keyboard keyboard = controller.getKeyboard();
				Camera camera = controller.getCamera();

				if (keyboard.isKeyPressed("w")) {
					camera.move(0, 0, -1);
				}

				if (keyboard.isKeyPressed("s")) {
					camera.move(0, 0, 1);
				}

				if (keyboard.isKeyPressed("a")) {
					camera.move(-1, 0, 0);
				}

				if (keyboard.isKeyPressed("d")) {
					camera.move(1, 0, 0);
				}

				if (keyboard.isKeyPressed("q")) {
					camera.move(0, -1, 0);
				}

				if (keyboard.isKeyPressed("e")) {
					camera.move(0, 1, 0);
				}
			}
		}
	}
}
