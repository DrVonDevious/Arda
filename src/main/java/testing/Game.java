package testing;

import dev.devious.engine.ILogic;
import dev.devious.engine.ObjectLoader;
import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.*;
import dev.devious.engine.ecs.systems.Physics;
import dev.devious.engine.ecs.systems.PlayerInput;
import dev.devious.engine.input.Keyboard;
import dev.devious.engine.rendering.Model;
import dev.devious.engine.rendering.Terrain;
import dev.devious.engine.rendering.RenderManager;
import dev.devious.engine.rendering.Texture;
import dev.devious.engine.rendering.WindowManager;
import dev.devious.engine.rendering.camera.Camera;
import dev.devious.engine.input.Mouse;
import dev.devious.engine.lighting.Light;
import dev.devious.engine.rendering.scenes.Scene;
import dev.devious.engine.rendering.scenes.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Game implements ILogic {
	private final RenderManager renderer;
	private final SceneManager sceneManager;
	private final WindowManager window;
	private final ObjectLoader objectLoader;
	private final World world;

	private Camera camera;
	private Keyboard keyboard;
	private Light light;

	public Game(WindowManager window, Camera camera, Keyboard keyboard) {
		objectLoader = new ObjectLoader();
		this.camera = camera;
		this.keyboard = keyboard;
		this.window = window;
		renderer = new RenderManager(window, camera);
		sceneManager = new SceneManager();
		light = new Light(new Vector3f(100, 100, 0), new Vector3f(1f, 1f, 1f));
		world = new World(renderer, camera, light);
	}

	@Override
	public void init() throws Exception {
		renderer.init();

		world.addSystem(new PlayerInput(world));

		Scene gameScene = new Scene(window, camera);
		sceneManager.setActiveScene(gameScene);

		Entity player = world.createEntity();
		player.addComponent(new PlayerController(keyboard, camera));
		player.addComponent(new Rigidbody());
		player.addComponent(new GodMode(false));
		player.addComponent(new Position(0, 0, 0));
		player.addComponent(new Velocity(0, 0, 0));
		player.addComponent(new Rotation(0, 0, 0));

		Model model = objectLoader.loadOBJModel("/models/billboard.obj");
		model.setTexture(new Texture(objectLoader.loadTexture("src/main/resources/textures/pine_tree.png")));
		model.getTexture().setHasTransparency(true);
		model.getTexture().setHasFakeLighting(true);
		model.getTexture().setShineDamper(10);
		model.getTexture().setSpecularity(0);

		Terrain terrain = new Terrain(
				-400,
				-400,
				objectLoader,
				new Texture(objectLoader.loadTexture("src/main/resources/textures/grass.png")),
				"heightmap"
		);

		Random rand = new Random();
		for (int i = 0; i < 6000; i++) {
			int randX = rand.nextInt(800) - 400;
			int randZ = rand.nextInt(800) - 400;
			int randScale = rand.nextInt(2) + 10;
			dev.devious.engine.ecs.Entity entity = world.createEntity();
			entity.addComponent(new Position(randX, terrain.getVertexHeight(randX, randZ) + 9.5f, randZ));
			entity.addComponent(new Rotation(90, 0, 140));
			entity.addComponent(new Renderable(model, randScale));
			gameScene.addEntity(entity);
		}

		gameScene.addLight(light);
		gameScene.addTerrain(terrain);

		world.addSystem(new Physics(world, terrain));
	}

	@Override
	public void update(Mouse mouse) {
		world.process();

		float mouseSensitivity = 0.2f;

		if (mouse.isRightButtonPressed()) {
			Vector2f rotationVector = mouse.getDisplayVector();
			camera.rotate(new Vector3f(rotationVector.x * mouseSensitivity, rotationVector.y * mouseSensitivity, 0));
		}
	}

	@Override
	public void render() {
		if (window.isResized()) {
			GL11.glViewport(0, 0, window.getWindowWidth(), window.getWindowHeight());
			window.setIsResized(true);
		}

		sceneManager.getActiveScene().render(renderer);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		objectLoader.cleanup();
	}
}
