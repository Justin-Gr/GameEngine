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
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position.y += 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.y -= 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.05f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.x -= 0.05f;
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
