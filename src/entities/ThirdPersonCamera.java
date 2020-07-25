package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ThirdPersonCamera extends Camera {
	
	private final float HEIGHT_OFFSET = 3;
	private final float MIN_DISTANCE = 10;
	private final float MAX_DISTANCE = 120;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Player player;
	
	public ThirdPersonCamera(Player player) {
		this.player = player;
	}
	
	@Override
	public void move() {
		updateZoom();
		updatePitch();
		updateAngleAroundPlayer();
		updateYaw();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		updateCameraPosition(horizontalDistance, verticalDistance);
	}
	
	private void updateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + HEIGHT_OFFSET + verticalDistance;
		position.z = player.getPosition().z - offsetZ;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void updateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		distanceFromPlayer = Math.max(MIN_DISTANCE, Math.min(MAX_DISTANCE, distanceFromPlayer));
	}
	
	private void updatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			pitch = Math.max(0, Math.min(90, pitch));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch += 1.0f;
			pitch = Math.min(90, pitch);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch -= 1.0f;
			pitch = Math.max(0, pitch);
		}
	}
	
	private void updateAngleAroundPlayer() {
		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			angleAroundPlayer -= 1.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			angleAroundPlayer += 1.0f;
		}
	}
	
	private void updateYaw() {
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
}
