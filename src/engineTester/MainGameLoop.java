package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Light;
import entities.Player;
import entities.ThirdPersonCamera;
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
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("flower"));
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
		Player player = new Player(texturedFlushedModel, new Vector3f(0.0f, 50.0f, -50.0f), 0.0f, 0.0f, 0.0f, 3.0f);
		
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
		
		ThirdPersonCamera camera = new ThirdPersonCamera(player);
		Light light = new Light(new Vector3f(3000.0f, 3000.0f, 3000.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		while(!Display.isCloseRequested()) {
			
			for (Entity fern : ferns) {
				renderer.processEntity(fern);
			}
			for (Entity grass : grasses) {
				renderer.processEntity(grass);
			}
			for (Entity tree : trees) {
				renderer.processEntity(tree);
			}
			
			for(Terrain terrain : terrains) {
				renderer.processTerrain(terrain);
			}
			
			player.move();
			renderer.processEntity(player);

			camera.move();
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
