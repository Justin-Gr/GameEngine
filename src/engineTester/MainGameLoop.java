package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// OpenGL expects vertices to be defined counter clockwise by default
		float[] vertices = {
			// Bottom left triangle
			-0.5f,  0.5f, 0f,
			-0.5f, -0.5f, 0f,
			 0.5f, -0.5f, 0f,
			
			// Top right triangle
			 0.5f, -0.5f, 0f,
			 0.5f,  0.5f, 0f,
			-0.5f,  0.5f, 0f
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			//game logic
			renderer.render(model);
			DisplayManager.updateDisplay();
			
		}
		
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
