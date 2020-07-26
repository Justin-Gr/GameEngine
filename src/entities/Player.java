package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	private static final float JUMP_SPEED = 30;
	private static final float GRAVITY = -50;
	
	private float currentRunSpeed = 0;
	private float currentUpwardsSpeed = 0;
	private float currentTurnSpeed = 0;
	
	private boolean onGround = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentRunSpeed * DisplayManager.getFrameTimeSeconds();
		currentUpwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dy = (float) (currentUpwardsSpeed * DisplayManager.getFrameTimeSeconds());
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, dy, dz);
		
		float terrainHeight = terrain.getHeight(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			currentUpwardsSpeed = 0;
			onGround = true;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			currentRunSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentRunSpeed = -RUN_SPEED;
		} else {
			currentRunSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			currentTurnSpeed = TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = -TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	private void jump() {
		if (onGround) {
			currentUpwardsSpeed = JUMP_SPEED;
			onGround = false;
		}
	}
	
}
