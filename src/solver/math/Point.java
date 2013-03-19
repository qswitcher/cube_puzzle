package solver.math;

public class Point {
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	public Point(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}

	public int getZ() {
		return z;
	}
	
	public void setZ(int z){
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Point){
			Point other = (Point) o;
			return x == other.getX() && y == other.getY() && z == other.getZ();
		}
		return false;
	}

	@Override
	public String toString(){
		return "(" + x + "," + y + "," + z + ")";
	}
	
	public String getDisplayString(){
		return x + "" + y + "" + z;
	}
	
	
}
