package net.mrblockplacer.FirstGame.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Textures {
	public static Render ground = loadBitmap("/images/groundImage.png");

	public static Render loadBitmap(String string) {
		try {
			BufferedImage img = ImageIO.read(Textures.class.getResource(string));
			int width = img.getWidth();
			int height = img.getHeight();
			Render result = new Render(width, height);
			img.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Oh no! Some images couldn't be loaded!", "ERROR!", JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException(e);
		}
	}
}
