package aoc2020;

public class Point4D {
	public int x;
	public int y;
	public int z;
	public int w;
	
	Point4D(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public double distanceTo(Point4D p) {
		int dx = p.x - x, dy = p.y - y, dz = p.z - z, dw = p.w - w;
		return Math.sqrt(dx*dx + dy*dy + dz*dz + dw*dw);
	}
	
	public void add(int dx, int dy, int dz, int dw) {
		x += dx;
		y += dy;
		z += dz;
		w += dw;
	}
	
	public Point4D added(int dx, int dy, int dz, int dw) {
		return new Point4D(x + dx, y + dy, z + dz, w + dw);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + w;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point4D other = (Point4D) obj;
		if (w != other.w)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
}