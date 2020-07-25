package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	protected Vector3f position = new Vector3f(0, 10, 0);
	protected float pitch = 20;
	protected float yaw = 0;
	protected float roll = 0;
	
	public void move() {
		updatePitch();
		updateYaw();
		updateCameraPosition();
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
	
	private void updatePitch() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch += 0.5f;
		}
	}
	
	private void updateYaw() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw +=  0.5f;
		}
	}
	
	private void updateCameraPosition() {
		Vector3f direction = (Vector3f) calculateXZDirection().scale(1.5f);
		Vector3f sideDirection = (Vector3f) calculateSideDirection().scale(1.5f);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			Vector3f.add(position, direction, position);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Vector3f.sub(position, direction, position);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Vector3f.add(position, sideDirection, position);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			Vector3f.sub(position, sideDirection, position);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.7f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 0.7f;
		}
	}
	
	private Vector3f calculateDirection() {
		double r_pitch = Math.toRadians(pitch);
		double r_yaw = Math.toRadians(yaw);
		float x = (float)  (Math.cos(r_pitch) * Math.sin(r_yaw));
		float y = (float) -(Math.sin(r_pitch));
		float z = (float) -(Math.cos(r_pitch) * Math.cos(r_yaw));
		return new Vector3f(x, y, z);
	}
	
	private Vector3f calculateXZDirection() {
		double r_yaw = Math.toRadians(yaw);
		float x = (float)  Math.sin(r_yaw);
		float z = (float) -Math.cos(r_yaw);
		return new Vector3f(x, 0, z);
	}
	
	private Vector3f calculateSideDirection() {
		Vector3f direction = calculateDirection();
		return new Vector3f(-direction.z, 0, direction.x);
	}
	
}
