package dev.devious.engine.graphics.shader;

import dev.devious.engine.graphics.WindowManager;
import dev.devious.engine.graphics.camera.Camera;
import dev.devious.engine.lighting.Light;
import dev.devious.engine.utils.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UniformManager {
	private final Map<String, Integer> uniforms;

	private ShaderManager shaderManager;
	private WindowManager windowManager;

	public UniformManager(ShaderManager shader, WindowManager window) {
		shaderManager = shader;
		windowManager = window;
		uniforms = new HashMap<>();
	}

	public void createUniform(String uniformName, int programId) throws Exception {
		int uniformLocation = GL20.glGetUniformLocation(programId, uniformName);
		if (uniformLocation < 0) {
			throw new Exception("Uniform not found: " + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}

	public void setUniform(String uniformName, Vector4f value) {
		GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniformName, Vector3f value) {
		GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, boolean value) {
		GL20.glUniform1f(uniforms.get(uniformName), value ? 1 : 0);
	}

	public void setUniform(String uniformName, int value) {
		GL20.glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		GL20.glUniform1f(uniforms.get(uniformName), value);
	}

	public void setAllUniforms(Camera mainCamera, Light light) {
		setUniform("textureSampler", 0);
		setUniform("projectionMatrix", windowManager.updateProjectionMatrix());
		setUniform("viewMatrix", Transformation.createViewMatrix(mainCamera));
		setUniform("lightPosition", light.getPosition());
		setUniform("lightColor", light.getColor());
	}

	public void createAllUniforms(int programId, String shaderType) throws Exception {
		createUniform("textureSampler", programId);
		createUniform("transformationMatrix", programId);
		createUniform("viewMatrix", programId);
		createUniform("lightPosition", programId);
		createUniform("lightColor", programId);
		createUniform("shineDamper", programId);
		createUniform("specularity", programId);
		createUniform("skyColor", programId);
		createUniform("projectionMatrix", programId);

		if (Objects.equals(shaderType, "entity")) {
			createUniform("useFakeLighting", programId);
		} else {
//			createUniform("projectionMatrix", programId);
		}
	}
}
