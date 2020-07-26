package terrains;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerrainMap {

	private Map<TerrainGridPos, Terrain> terrains = new HashMap<TerrainGridPos, Terrain>();
	private Terrain defaultTerrain;
	
	public TerrainMap(List<Terrain> terrains, Terrain defaultTerrain) {
		this.defaultTerrain = defaultTerrain;
		for(Terrain terrain : terrains) {
			int gridX = (int) (terrain.getX() / Terrain.getSize());
			int gridZ = (int) (terrain.getZ() / Terrain.getSize());
			this.terrains.put(new TerrainGridPos(gridX, gridZ), terrain);
		}
	}
	
	public Terrain getTerrainFromPosition(float x, float z) {
		double gridX = Math.floor(x / Terrain.getSize());
		double gridZ = Math.floor(z / Terrain.getSize());
		TerrainGridPos gridPos = new TerrainGridPos((int) gridX, (int) gridZ);
		return terrains.getOrDefault(gridPos, defaultTerrain);
	}
	
}
