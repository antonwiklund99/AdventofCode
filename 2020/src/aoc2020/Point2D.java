package aoc2020;

public class Point2D {
	public double x;
	public double y;
	
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
	
	public String toString() {
		return "Point(" + x + ", " + y + ")";
	}
}