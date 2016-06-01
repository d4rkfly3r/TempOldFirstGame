package net.mrblockplacer.FirstGame.graphics;

import java.util.Random;

import net.mrblockplacer.FirstGame.Game;

public class Screen extends Render {

	private Render test;
	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		Random random = new Random();
		test = new Render(256, 256);
		render = new Render3D(width, height);
		for (int i = 0; i < 256 * 256; i++) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}

	public void render(Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		render.ground(game);
//		render.walls(game);
		render.renderDistanceLimiter();
//		 render.renderWalls(0, 0.5, 1.5, 0);
		draw(render, 0, 0);
	}
}
