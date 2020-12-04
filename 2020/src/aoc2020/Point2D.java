package aoc2020;

public class Point2D {
	private double x;
	private double y;
	
	Point2D(int x, int y) {
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
}