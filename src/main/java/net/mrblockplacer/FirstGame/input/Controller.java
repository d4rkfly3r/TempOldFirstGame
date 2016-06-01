package net.mrblockplacer.FirstGame.input;

public class Controller {

	public double x, y, z, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean isWalking = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
		double rotationSpeed = 0.01555;
		double walkSpeed = 0.4555;
		double xMove = 0;
		double zMove = 0;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;

		if (forward) {
			zMove++;
			isWalking = true;
		}

		if (jump) {
			y += jumpHeight;
			// sprint = false;
		}
		if (crouch) {
			y -= crouchHeight;
			sprint = false;
			walkSpeed = 0.2;
		}
		if (back) {
			zMove--;
			isWalking = true;
		}

		if (left) {
			xMove--;
			isWalking = true;

		}

		if (right) {
			xMove++;
			isWalking = true;

		}

		if (turnLeft) {
			rotationa -= rotationSpeed;
		}

		if (turnRight) {
			rotationa += rotationSpeed;
		}

		if (sprint) {
			walkSpeed = 1;
			isWalking = true;
		}
		if (!forward && !back && !left && !right) {
			isWalking = false;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		y *= 0.9;
		x += xa;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.5;

	}

}
