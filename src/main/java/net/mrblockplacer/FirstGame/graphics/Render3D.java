package net.mrblockplacer.FirstGame.graphics;

import java.util.Random;

import net.mrblockplacer.FirstGame.Game;
import net.mrblockplacer.FirstGame.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private double renderDistance = 5000;
	public double right, forward, up, cosine, sine;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void ground(Game game) {

		double floorPos = 8;
		double ceilingPos = 8;
		forward = game.controls.z;// game.time % 100 / 20.0;
		right = game.controls.x;
		up = game.controls.y;// Math.sin(game.time / 10.0) * 2;
		double walkingAnim = Math.sin(game.time / 6.0) * 0.5;
		double rotation = game.controls.rotation;// Math.sin(game.time / 40.0) *
													// 0.5;
		// / 80);
		// Math.sin(game.time % 1000.0 / 80.0);//
		// game.controls.rotation;//
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y + -height / 2.0) / height;

			double z = (floorPos + up) / ceiling;
			if (Controller.isWalking) {
				z = (floorPos + up + walkingAnim) / ceiling;

			}

			if (ceiling < 0) {
				z = (ceilingPos - up) / -ceiling;
				if (Controller.isWalking) {
					z = (ceilingPos - up - walkingAnim) / -ceiling;

				}
			}

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Textures.ground.pixels[(xPix & 7) | (yPix & 7) * 8];

				if (z > 500) {
					pixels[x + y * width] = 0;
				}

			}
		}

	}

	public void renderWalls(double xLeft, double xRight, double zDistance, double yHeight) {
		double xcLeft = ((xLeft) - right) * 2;
		double zcLeft = ((zDistance) - forward) * 2;
		double rotLeftSideX = xcLeft * cosine - xcLeft * sine;
		double yCornerTL = ((-yHeight) - up) * 2;
		double yCornerBL = ((+0.5 - yHeight) - up) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		double xcRight = ((xRight) - right) * 2;
		double zcRight = ((zDistance) - forward) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - up) * 2;
		double yCornerBR = ((+0.5 - yHeight) - up) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		if (xPixelLeft >= xPixelRight) {
			return;
		}

		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}

		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}

		double yPixelLeftTop = yCornerTL / rotLeftSideZ * height + height / 2;
		double yPixelLeftBottom = yCornerBL / rotLeftSideZ * height + height / 2;
		double yPixelRightTop = yCornerTR / rotRightSideZ * height + height / 2;
		double yPixelRightBottom = yCornerBR / rotRightSideZ * height + height / 2;

		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = 0 / rotLeftSideZ;
		double tex4 = 8 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / tex1 + (tex2 - tex1) * pixelRotation);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}

			if (yPixelBottomInt > width) {
				yPixelBottomInt = width;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				try {
					pixels[x + y * width] = xTexture * 100;
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					continue;
				}

				zBuffer[x + y * width] = 0;
			}

		}
	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0) {
				brightness = 0;
			}
			if (brightness > 255) {
				brightness = 255;
			}
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;

		}
	}

//	public void walls() {
//		Random random = new Random(100);
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble();
//			double yy = random.nextDouble();
//			double zz = 1.5 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble() - 1;
//			double yy = random.nextDouble();
//			double zz = 1.5 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble() - 1;
//			double yy = random.nextDouble() - 1;
//			double zz = 1.5 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble();
//			double yy = random.nextDouble() - 1;
//			double zz = 1.5 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble();
//			double yy = random.nextDouble();
//			double zz = 2 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble() - 1;
//			double yy = random.nextDouble();
//			double zz = 2 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble() - 1;
//			double yy = random.nextDouble() - 1;
//			double zz = 2 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//		for (int i = 0; i < 20000; i++) {
//			double xx = random.nextDouble();
//			double yy = random.nextDouble() - 1;
//			double zz = 2 - forward / 16;
//
//			int xPixel = (int) (xx / zz * height / 2 + width / 2);
//			int yPixel = (int) (yy / zz * height / 2 + height / 2);
//			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
//				pixels[xPixel + yPixel * width] = 0xfffff;
//
//			}
//		}
//
//	}
}
