package terrains;

public class Terrain {
	
	protected static final float SIZE = 800;

	protected float x;
	protected float z;
	
	public Terrain(float gridX, float gridZ) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
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
	
	public float getHeight(float worldX, float worldZ) {
		return Float.NEGATIVE_INFINITY;
	}
	
}
