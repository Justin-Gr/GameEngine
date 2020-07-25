package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 10, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {
		
	}
	
	public void move() {
		checkInputs();
	}
	
	public void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch += 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw +=  0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position = Vector3f.add(position, (Vector3f) getXZDirection().scale(1.5f), null);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position = Vector3f.sub(position, (Vector3f) getXZDirection().scale(1.5f), null);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position = Vector3f.add(position, (Vector3f) getSideDirection().scale(1.5f), null);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position = Vector3f.sub(position, (Vector3f) getSideDirection().scale(1.5f), null);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.7f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 0.7f;
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
	
	public Vector3f getDirection() {
		double r_pitch = Math.toRadians(pitch);
		double r_yaw = Math.toRadians(yaw);
		float x = (float) -(Math.cos(r_pitch) * Math.sin(-r_yaw));
		float y = (float) -(Math.sin(r_pitch));
		float z = (float) -(Math.cos(r_pitch) * Math.cos(-r_yaw));
		return new Vector3f(x, y, z);
	}
	
	public Vector3f getXZDirection() {
		double r_yaw = Math.toRadians(yaw);
		float x = (float) -Math.sin(-r_yaw);
		float z = (float) -Math.cos(-r_yaw);
		return new Vector3f(x, 0, z);
	}
	
	public Vector3f getSideDirection() {
		Vector3f direction = getDirection();
		return new Vector3f(-direction.z, 0, direction.x);
	}
	
}
