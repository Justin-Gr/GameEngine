package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {

	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightmap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightmap);
	}
	
	public static float getSize() {
		return SIZE;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / (float) (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX < 0 || gridX >= heights.length - 1 || gridZ < 0 || gridZ >= heights.length - 1) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float result;
		if (xCoord <= (1 - zCoord)) {
			result = Maths.barryCentric(new Vector3f(0, heights[gridZ][gridX], 0),
					new Vector3f(1, heights[gridZ + 1][gridX], 0), 
					new Vector3f(0, heights[gridZ][gridX + 1], 1), 
					new Vector2f(zCoord, xCoord));
		} else {
			result = Maths.barryCentric(new Vector3f(1, heights[gridZ + 1][gridX], 0), 
					new Vector3f(1, heights[gridZ + 1][gridX + 1], 1), 
					new Vector3f(0, heights[gridZ][gridX + 1], 1),
					new Vector2f(zCoord, xCoord));
		}
		return result;
	}

	private RawModel generateTerrain(Loader loader, String filename){
		
		BufferedImage heightmap = null;
		try {
			heightmap = ImageIO.read(new File("res/" + filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int vertexCount = heightmap.getHeight();
		heights = new float[vertexCount][vertexCount];
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; i++){
			for (int j = 0; j < vertexCount; j++){
				float height = getHeight(j, i, heightmap);
				heights[i][j] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, heightmap);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++){
			for (int gx = 0; gx < vertexCount - 1; gx++){
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private float getHeight(int x, int z, BufferedImage heightmap) {
		if (x < 0 || x >= heightmap.getWidth() || z < 0 || z >= heightmap.getHeight()) {
			return 0;
		}
		float height = heightmap.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage heightmap) {
		int sizeX = heightmap.getWidth();
		int sizeZ = heightmap.getHeight();
		float heightLeft = getHeight(Math.max(x - 1, 0), z, heightmap);
		float heightRight = getHeight(Math.min(x + 1, sizeX - 1), z, heightmap);
		float heightUp = getHeight(x, Math.min(z + 1, sizeZ - 1), heightmap);
		float heightDown = getHeight(x, Math.max(z - 1, 0), heightmap);
		Vector3f normal = new Vector3f(heightLeft - heightRight, 2f, heightDown - heightUp);
		normal.normalise();
		return normal;
	}
	
}
