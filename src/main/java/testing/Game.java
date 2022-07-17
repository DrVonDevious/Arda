package testing;

import dev.devious.engine.ILogic;
import dev.devious.engine.ObjectLoader;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.Position;
import dev.devious.engine.ecs.components.Renderable;
import dev.devious.engine.ecs.components.Rotation;
import dev.devious.engine.ecs.systems.Physics;
import dev.devious.engine.entity.Entity;
import dev.devious.engine.entity.Model;
import dev.devious.engine.entity.terrain.Terrain;
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
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Game implements ILogic {
	private final RenderManager renderer;
	private final SceneManager sceneManager;
	private final WindowManager window;
	private final ObjectLoader objectLoader;
	private final World world;

	private Map<Model, List<Entity>> entities = new HashMap<>();
	private Map<Model, List<Terrain>> allTerrains = new HashMap<>();
	private List<Entity> allCubes = new ArrayList<>();
	private List<Terrain> terrains = new ArrayList<>();
	private Camera camera;
	private Light light;

	private Vector3f cameraMovement;

	public Game(WindowManager window, Camera camera) {
		objectLoader = new ObjectLoader();
		this.camera = camera;
		this.window = window;
		renderer = new RenderManager(window, camera);
		sceneManager = new SceneManager();
		light = new Light(new Vector3f(0, 20, -10), new Vector3f(1f, 1f, 1f));
		world = new World(renderer, camera, light);
	}

	@Override
	public void init() throws Exception {
		renderer.init();

		world.addSystem(new Physics(world));

		Scene gameScene = new Scene(window, camera);
		sceneManager.setActiveScene(gameScene);

		Model model = objectLoader.loadOBJModel("/models/billboard.obj");
		model.setTexture(new Texture(objectLoader.loadTexture("src/main/resources/textures/pine_tree.png")));
		model.getTexture().setHasTransparency(true);
		model.getTexture().setHasFakeLighting(true);
		model.getTexture().setShineDamper(10);
		model.getTexture().setSpecularity(0);

		Random rand = new Random();
		for (int i = 0; i < 6000; i++) {
			int randX = rand.nextInt(800) - 400;
			int randZ = rand.nextInt(800) - 400;
			int randScale = rand.nextInt(2) + 10;
			dev.devious.engine.ecs.Entity entity = world.createEntity();
			entity.addComponent(new Position(randX, 10, randZ));
			entity.addComponent(new Rotation(90, 0, 140));
			entity.addComponent(new Renderable(model, randScale));
			gameScene.addEntity(entity);
		}

		gameScene.addLight(light);
		gameScene.addTerrain(new Terrain(
			-400,
			-400,
			objectLoader,
			new Texture(objectLoader.loadTexture("src/main/resources/textures/grass.png"))
		));

		cameraMovement = new Vector3f(0, 0, 0);
	}

	@Override
	public void input() {
		if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
			cameraMovement.z = -1;
		}

		if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
			cameraMovement.z = 1;
		}

		if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
			cameraMovement.x = -1;
		}

		if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
			cameraMovement.x = 1;
		}

		if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			cameraMovement.y = -1;
		}

		if (window.isKeyPressed(GLFW.GLFW_KEY_E)) {
			cameraMovement.y = 1;
		}
	}

	@Override
	public void update(Mouse mouse) {
		world.process();

		float mouseSensitivity = 0.2f;

		camera.move(cameraMovement.x, cameraMovement.y, cameraMovement.z);

		if (mouse.isRightButtonPressed()) {
			Vector2f rotationVector = mouse.getDisplayVector();
			camera.rotate(new Vector3f(rotationVector.x * mouseSensitivity, rotationVector.y * mouseSensitivity, 0));
		}

		for (Entity entity : allCubes) {
			Entity.processEntity(entities, entity);
		}

		for (Terrain terrain : terrains) {
			Terrain.processTerrain(allTerrains, terrain);
		}

		cameraMovement = new Vector3f(0, 0, 0);
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
