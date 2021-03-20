package terrains;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TerrainMap {

	private Map<TerrainGridPos, Terrain> terrains = new HashMap<TerrainGridPos, Terrain>();
	
	public TerrainMap(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			int gridX = (int) (terrain.getX() / Terrain.getSize());
			int gridZ = (int) (terrain.getZ() / Terrain.getSize());
			this.terrains.put(new TerrainGridPos(gridX, gridZ), terrain);
		}
	}
	
	public Optional<Terrain> getTerrainFromPosition(float x, float z) {
		double gridX = Math.floor(x / Terrain.getSize());
		double gridZ = Math.floor(z / Terrain.getSize());
		TerrainGridPos gridPos = new TerrainGridPos((int) gridX, (int) gridZ);
		return Optional.ofNullable(terrains.get(gridPos));
	}
	
}
