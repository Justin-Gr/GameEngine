package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	private static final String TITLE = "Game Engine";
	
	private static long lastFrameTime;
	private static float deltaTime;
	
	private static long lastFramerateUpdate;
	private static int frameCount;
	
	public static void createDisplay() {
		ContextAttribs attribs =  new ContextAttribs(3, 2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		lastFramerateUpdate = lastFrameTime;
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		deltaTime = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		updateFramerate();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static float getFrameTimeSeconds() {
		return deltaTime;
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution(); // time in millisec
	}
	
	private static void updateFramerate() {
		if (lastFrameTime - lastFramerateUpdate > 1000) {
			Display.setTitle(TITLE + " - FPS : " + frameCount);
			frameCount = 0;
			lastFramerateUpdate += 1000;
		}
		frameCount++;
	}
	
}
