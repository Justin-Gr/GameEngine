package entities;

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
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateYaw();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
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
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		distanceFromPlayer = Math.max(MIN_DISTANCE, Math.min(MAX_DISTANCE, distanceFromPlayer));
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			pitch = Math.max(0, Math.min(90, pitch));
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	private void calculateYaw() {
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
}
