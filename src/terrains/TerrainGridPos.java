package terrains;

public class TerrainGridPos {

	private final int x;
	private final int z;
	
	public TerrainGridPos(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}
	
	@Override
	public int hashCode() {
		return x + z;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof TerrainGridPos) {
			result = ((TerrainGridPos) obj).getX() == this.x && ((TerrainGridPos) obj).getZ() == this.z;
		}
		return result;
	}
	
}
