package dev.devious.engine;

import dev.devious.engine.input.Mouse;

public interface ILogic {
	void init() throws Exception;

	void input();

	void update(Mouse mouse);

	void render();

	void cleanup();
}
