package objects;

import java.util.ArrayList;

public class Point {
	private int x;
	private int y;

	private Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Point create(int x, int y) {
		return new Point(x,y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public ArrayList<Point> getSiblings(TestObject obj, char[][] obj_2D){
		ArrayList<Point> siblings = new ArrayList<>();
		for(int i = -1 ; i < 2 ; i++) {
			for(int j = -1; j <2 ; j++) {
				int x = this.getX()+ j;
				int y = this.getY()+ i;
				if(obj.inbounds(y, x)) {
					Point p = Point.create(x, y);
					siblings.add(p);
				}
			}
		}
		siblings.remove(this);
		return siblings;

	}

	public int distance(Point p) {
		return (int)Math.sqrt(Math.pow(this.getX() - p.getX(), 2) + Math.pow(this.getY() - p.getY(), 2));
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Point) {
			Point p = (Point)o;
			if(this.getX() == p.getX() && this.getY() == p.getY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y +"]";
	}
}
