package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {
		
	}
	
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.15f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 0.15f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.x -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position.z -= 0.3f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 0.3f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch -= 0.3f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch += 0.3f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= 0.3f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw +=  0.3f;
		}
		if (Mouse.hasWheel()) {
			position.z -= Mouse.getDWheel() * 0.01f;
		}
		if (Mouse.isButtonDown(0)) {
			position.x -= Mouse.getDX() * 0.01f;
			position.y -= Mouse.getDY() * 0.01f;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
}
