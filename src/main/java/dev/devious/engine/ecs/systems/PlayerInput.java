package dev.devious.engine.ecs.systems;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.System;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.*;
import dev.devious.engine.input.Keyboard;
import dev.devious.engine.rendering.camera.Camera;
import org.joml.Vector3f;

import java.util.List;

public class PlayerInput extends System {
	private final World world;

	public PlayerInput(World world) {
		this.world = world;
	}

	@Override
	public void process() {
		List<Entity> entities = world.getEntities();
		for (Entity entity : entities) {
			if (
				entity.hasComponent(PlayerController.class) &&
				entity.hasComponent(Velocity.class) &&
				entity.hasComponent(Position.class)
			) {
				PlayerController controller = (PlayerController) entity.getComponent(PlayerController.class);
				GodMode godMode = (GodMode) entity.getComponent(GodMode.class);
				Velocity velocity = (Velocity) entity.getComponent(Velocity.class);
				Position position = (Position) entity.getComponent(Position.class);
				Rotation rotation = (Rotation) entity.getComponent(Rotation.class);
				Keyboard keyboard = controller.getKeyboard();
				Camera camera = controller.getCamera();

				camera.setPosition(new Vector3f(position.getX(), position.getY() + 2, position.getZ()));
				rotation.setXYZ(new Vector3f(camera.getRotation().x, camera.getRotation().y, camera.getRotation().z));

				if (keyboard.isKeyPressed("w")) {
					velocity.setZ(-0.1f);
				}

				if (keyboard.isKeyPressed("s")) {
					velocity.setZ(0.1f);
				}

				if (keyboard.isKeyPressed("a")) {
					velocity.setX(-0.1f);
				}

				if (keyboard.isKeyPressed("d")) {
					velocity.setX(0.1f);
				}

				if (keyboard.isKeyPressed("q")) {
					velocity.setY(-0.1f);
				}

				if (keyboard.isKeyPressed("e")) {
					velocity.setY(0.1f);
				}

				if (keyboard.isKeyPressed("f")) {
					godMode.setActive(!godMode.isActive());
				}
			}
		}
	}
}
