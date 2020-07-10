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
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
				
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("light_yellow"));
		texture.setShineDamper(10.0f);
		texture.setReflectivity(1.0f);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0.0f, -4.0f, -25.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(200.0f, 200.0f, 100.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		while(!Display.isCloseRequested()) {
			entity.increasePosition(0.0f, 0.0f, 0.0f);
			entity.increaseRotation(0.0f, 0.7f, 0.0f);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			shader.loadLight(light);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
