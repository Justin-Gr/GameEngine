package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
				
		RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
		RawModel cubeModel = OBJLoader.loadObjModel("cube", loader);
		
		ModelTexture yellowTexture = new ModelTexture(loader.loadTexture("light_yellow"));
		yellowTexture.setShineDamper(10.0f);
		yellowTexture.setReflectivity(1.0f);
		ModelTexture rainbowTexture = new ModelTexture(loader.loadTexture("rainbow"));
		rainbowTexture.setShineDamper(10.0f);
		rainbowTexture.setReflectivity(1.0f);
		
		TexturedModel texturedDragonModel = new TexturedModel(dragonModel, yellowTexture);
		Entity dragonEntity = new Entity(texturedDragonModel, new Vector3f(0.0f, -4.0f, -25.0f), 0.0f, 180.0f, 0.0f, 1.0f);
				
		TexturedModel texturedCubeModel = new TexturedModel(cubeModel, rainbowTexture);
		
		List<Entity> cubes = new ArrayList<Entity>();
		Random r = new Random();
		
		for (int i = 0; i < 2000; i++) {
			float x = r.nextFloat() * 100 - 50;
			float y = r.nextFloat() * 100 - 50;
			float z = r.nextFloat() * -300;
			float rotX = r.nextFloat() * 180.0f;
			float rotY = r.nextFloat() * 180.0f;
			float rotZ = r.nextFloat() * 180.0f;
			cubes.add(new Entity(texturedCubeModel, new Vector3f(x, y, z), rotX, rotY, rotZ, 2.0f));
		}
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(3000.0f, 2000.0f, 3000.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			camera.move();

			dragonEntity.increasePosition(0.0f, 0.0f, 0.0f);
			dragonEntity.increaseRotation(0.0f, 0.5f, 0.0f);
			renderer.processEntity(dragonEntity);
			
			for (Entity cube : cubes) {
				cube.increaseRotation(0.2f, 0.2f, 0.0f);
				renderer.processEntity(cube);
			}
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
