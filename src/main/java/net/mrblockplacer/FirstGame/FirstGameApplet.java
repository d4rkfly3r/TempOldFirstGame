package net.mrblockplacer.FirstGame;

import java.applet.Applet;
import java.awt.BorderLayout;

public class FirstGameApplet extends Applet {

	private static final long serialVersionUID = 1L;

	private Display game = new Display();

	public void init() {
		setLayout(new BorderLayout());
		add(game);
	}

	public void start() {
		game.start();
	}

	public void stop() {
		game.stop();
	}
}
