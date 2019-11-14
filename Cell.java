package objects;

public class Cell {
	private Point point;
	public int data;
	public int count;
	

	public Cell(int x, int y, int data){
		point = Point.create(x, y);
		this.data = data;
	}

	public Point getPoint(){
		return point;
	}

	public int getData(){
		return data;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Cell){
			Cell c = (Cell)o;
			return c.getPoint().equals(this.getPoint());
		}

		return false;
	}

}
