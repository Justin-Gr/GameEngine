package terrains;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerrainMap {

	private Map<TerrainGridPos, Terrain> terrains = new HashMap<TerrainGridPos, Terrain>();
	
	public TerrainMap(List<ModeledTerrain> terrains) {
		for (ModeledTerrain terrain : terrains) {
			int gridX = (int) (terrain.getX() / Terrain.getSize());
			int gridZ = (int) (terrain.getZ() / Terrain.getSize());
			this.terrains.put(new TerrainGridPos(gridX, gridZ), terrain);
		}
	}
	
	public Terrain getTerrainFromPosition(float x, float z) {
		double gridX = Math.floor(x / Terrain.getSize());
		double gridZ = Math.floor(z / Terrain.getSize());
		TerrainGridPos gridPos = new TerrainGridPos((int) gridX, (int) gridZ);
		Terrain terrain = terrains.get(gridPos);
		if (terrain == null) {
			terrain = new Terrain(gridPos.getX(), gridPos.getZ());
			terrains.put(gridPos, terrain);
		}
		return terrain;
	}
	
}
