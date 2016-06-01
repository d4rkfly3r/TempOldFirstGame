package net.mrblockplacer.FirstGame;

import java.awt.event.KeyEvent;

import net.mrblockplacer.FirstGame.input.Controller;

public class Game {

	public int time;
	public Controller controls;

	public Game() {
		controls = new Controller();
	}

	public void tick(boolean[] key) {
		time++;
		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean right = key[KeyEvent.VK_D];
		boolean left = key[KeyEvent.VK_A];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_SHIFT];
		boolean sprint = key[KeyEvent.VK_R];

		controls.tick(forward, back, left, right, jump, crouch, sprint);
	}
}
