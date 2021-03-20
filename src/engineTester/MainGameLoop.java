package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.ThirdPersonCamera;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.GuiRenderer;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import terrains.TerrainMap;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		//***************************TERRAINS***************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain_grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain_mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain_flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain_path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain_blendMap"));
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap_valley"));
		terrains.add(new Terrain( 0, -1, loader, texturePack, blendMap, "heightmap_valley"));
		terrains.add(new Terrain(-1,  0, loader, texturePack, blendMap, "heightmap_valley"));
		terrains.add(new Terrain( 0,  0, loader, texturePack, blendMap, "heightmap_valley"));
		
		TerrainMap map = new TerrainMap(terrains);
		
		//***************************ENTITIES***************************
		
		Random r = new Random();
		
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setShineDamper(15.0f);
		fernTextureAtlas.setReflectivity(0.3f);
		fernTextureAtlas.setHasTransparency(true);
		fernTextureAtlas.setUseFakeLighting(false);
		fernTextureAtlas.setRowsNumber(2);
		TexturedModel texturedFernModel = new TexturedModel(fernModel, fernTextureAtlas);
		List<Entity> ferns = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			float x = r.nextFloat() * 1600 - 800;
			float z = r.nextFloat() * 1600 - 800;
			Optional<Terrain> fernTerrain = map.getTerrainFromPosition(x, z);
			float y = fernTerrain.map(terrain -> terrain.getHeight(x, z)).orElse(0F);
			int textureIndex = r.nextInt(4);
			ferns.add(new Entity(texturedFernModel, textureIndex, new Vector3f(x, y, z), 0.0f, 0.0f, 0.0f, 1.0f));
		}
		
		ModelData grassData = OBJFileLoader.loadOBJ("grass");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("flower"));
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel texturedGrassModel = new TexturedModel(grassModel, grassTexture);
		List<Entity> grasses = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			float x = r.nextFloat() * 1600 - 800;
			float z = r.nextFloat() * 1600 - 800;
			Optional<Terrain> grassTerrain = map.getTerrainFromPosition(x, z);
			float y = grassTerrain.map(terrain -> terrain.getHeight(x, z)).orElse(0F);
			grasses.add(new Entity(texturedGrassModel, new Vector3f(x, y, z), 0.0f, 0.0f, 0.0f, 2.0f));
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
			float x = r.nextFloat() * 1600 - 800;
			float z = r.nextFloat() * 1600 - 800;
			Optional<Terrain> treeTerrain = map.getTerrainFromPosition(x, z);
			float y = treeTerrain.map(terrain -> terrain.getHeight(x, z)).orElse(0F);
			trees.add(new Entity(texturedTreeModel, new Vector3f(x, y, z), 0.0f, 0.0f, 0.0f, 1.0f));
		}
		
		ModelData sphereData = OBJFileLoader.loadOBJ("sphere");
		RawModel sphereModel = loader.loadToVAO(sphereData.getVertices(), sphereData.getTextureCoords(), sphereData.getNormals(), sphereData.getIndices());
		ModelTexture flushedTexture = new ModelTexture(loader.loadTexture("flushed"));
		flushedTexture.setShineDamper(10.0f);
		flushedTexture.setReflectivity(1.0f);
		flushedTexture.setHasTransparency(false);
		flushedTexture.setUseFakeLighting(false);
		TexturedModel texturedFlushedModel = new TexturedModel(sphereModel, flushedTexture);
		Player player = new Player(texturedFlushedModel, new Vector3f(0.0f, 100.0f, 0.0f), 0.0f, 180.0f, 0.0f, 3.0f);
		
		//***************************GUIS***************************
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture flushedMeter = new GuiTexture(loader.loadTexture("gui_flushedMeter"), new Vector2f(-0.75f, -0.35f), new Vector2f(0.15f, 0.85f));
		guis.add(flushedMeter);
		
		//***************************GAME LOOP***************************
		
//		Camera camera = new Camera();
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

			Optional<Terrain> currentPlayerTerrain = map.getTerrainFromPosition(player.getPosition().x, player.getPosition().z);
			if (currentPlayerTerrain.isPresent()) {
				player.moveOnTerrain(currentPlayerTerrain.get());
			} else {
				player.move();
			}
			renderer.processEntity(player);

			camera.move();
			
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
			
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
