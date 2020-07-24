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
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(3000.0f, 3000.0f, 3000.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		MasterRenderer renderer = new MasterRenderer();
		
		//***************************ENTITIES***************************
		
		Random r = new Random();
		
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setShineDamper(15.0f);
		fernTexture.setReflectivity(0.3f);
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(false);
		TexturedModel texturedFernModel = new TexturedModel(fernModel, fernTexture);
		List<Entity> ferns = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			float x = r.nextFloat() * 1000 - 500;
			float z = r.nextFloat() * 1000 - 500;
			ferns.add(new Entity(texturedFernModel, new Vector3f(x, 0, z), 0.0f, 0.0f, 0.0f, 1.0f));
		}
		
		ModelData grassData = OBJFileLoader.loadOBJ("grass");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass"));
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel texturedGrassModel = new TexturedModel(grassModel, grassTexture);
		List<Entity> grasses = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			float x = r.nextFloat() * 1000 - 500;
			float z = r.nextFloat() * 1000 - 500;
			grasses.add(new Entity(texturedGrassModel, new Vector3f(x, 0, z), 0.0f, 0.0f, 0.0f, 2.0f));
		}
		
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
		treeTexture.setShineDamper(10.0f);
		treeTexture.setReflectivity(0.2f);
		treeTexture.setHasTransparency(false);
		treeTexture.setUseFakeLighting(false);
		TexturedModel texturedTreeModel = new TexturedModel(treeModel, treeTexture);
		List<Entity> trees = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			float x = r.nextFloat() * 1000 - 500;
			float z = r.nextFloat() * 1000 - 500;
			trees.add(new Entity(texturedTreeModel, new Vector3f(x, 0, z), 0.0f, 0.0f, 0.0f, 1.0f));
		}
		
		ModelData sphereData = OBJFileLoader.loadOBJ("sphere");
		RawModel sphereModel = loader.loadToVAO(sphereData.getVertices(), sphereData.getTextureCoords(), sphereData.getNormals(), sphereData.getIndices());
		ModelTexture flushedTexture = new ModelTexture(loader.loadTexture("flushed"));
		flushedTexture.setShineDamper(10.0f);
		flushedTexture.setReflectivity(1.0f);
		flushedTexture.setHasTransparency(false);
		flushedTexture.setUseFakeLighting(false);
		TexturedModel texturedFlushedModel = new TexturedModel(sphereModel, flushedTexture);
		Entity flushedEntity = new Entity(texturedFlushedModel, new Vector3f(0.0f, 10.0f, -50.0f), 0.0f, 0.0f, 0.0f, 5.0f);
		
		//***************************TERRAINS***************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain_grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain_mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain_flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain_path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain_blendMap"));
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain(-1, -1, loader, texturePack, blendMap));
		terrains.add(new Terrain( 0, -1, loader, texturePack, blendMap));
		terrains.add(new Terrain(-1,  0, loader, texturePack, blendMap));
		terrains.add(new Terrain( 0,  0, loader, texturePack, blendMap));
		
		//***************************GAME LOOP***************************
		
		long lastTime = System.nanoTime();
		long ellapsedTime = 0;
		long frameCount = 0;
		while(!Display.isCloseRequested()) {
			camera.move();

			for(Terrain terrain : terrains) {
				renderer.processTerrain(terrain);
			}
			
			for (Entity fern : ferns) {
				renderer.processEntity(fern);
			}
			for (Entity grass : grasses) {
				renderer.processEntity(grass);
			}
			for (Entity tree : trees) {
				renderer.processEntity(tree);
			}
			flushedEntity.increaseRotation(0.0f, 0.5f, 0.0f);
			renderer.processEntity(flushedEntity);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
			frameCount++;
			ellapsedTime -= lastTime - (lastTime = System.nanoTime());
			if (ellapsedTime >= 1e9) {
				long framerate = (long) (1e9 * frameCount / ellapsedTime);
				Display.setTitle("Game Engine - FPS : " + framerate);
				frameCount = 0;
				ellapsedTime = 0;
			}
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
