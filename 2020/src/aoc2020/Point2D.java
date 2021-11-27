package aoc2020;

public class Point2D {
	public double x;
	public double y;
	
	Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceTo(Point2D p) {
		double dx = p.x - x, dy = p.y - y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double angleTo(Point2D p) {
		double angle = Math.toDegrees(Math.atan2(p.x -x, p.y - y));
		return angle + Math.ceil(-angle/360)*360;
	}
	
	public void rotate(double deg) {
		double theta = Math.toRadians(deg);
		double newX = x*Math.cos(theta) - y*Math.sin(theta);
		y = Math.round(y*Math.cos(theta) + x*Math.sin(theta));
		x = Math.round(newX);
	}
	
	public void add(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public Point2D added(double dx, double dy) {
		return new Point2D(x + dx, y + dy);
	}
	
	public String toString() {
		return "Point(" + x + ", " + y + ")";
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
		Point2D other = (Point2D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
}