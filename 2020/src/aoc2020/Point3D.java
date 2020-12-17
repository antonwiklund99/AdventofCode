package aoc2020;

public class Point3D {
	public double x;
	public double y;
	public double z;
	
	Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distanceTo(Point3D p) {
		double dx = p.x - x, dy = p.y - y, dz = p.z -z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public void add(double dx, double dy, double dz) {
		x += dx;
		y += dy;
		z += dz;
	}
	
	public Point3D added(double dx, double dy, double dz) {
		return new Point3D(x + dx, y + dy, z + dz);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Point3D other = (Point3D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
	public String toString() {
		return "Point(" + x + ", " + y + ", " + z + ")";
	}
}
