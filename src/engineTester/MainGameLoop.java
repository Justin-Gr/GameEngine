package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
				
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		texture.setShineDamper(10.0f);
		texture.setReflectivity(1.0f);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0.0f, -4.0f, -25.0f), 0.0f, 180.0f, 0.0f, 1.0f);
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(200.0f, 200.0f, 100.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			entity.increasePosition(0.0f, 0.0f, 0.0f);
			entity.increaseRotation(0.0f, 0.0f, 0.0f);
			camera.move();
			
			renderer.processEntity(entity);
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
