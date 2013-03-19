package solver.snake;

public class Cube {
	/**
	 * Represents an individual cube on a snake puzzle. Each cube can be one of 4 different types: 
	 * <code>BEGINNING, STRAIGHT, CORNER, </code> or <code>END</code>. Each cube also contains it's
	 * location in (x,y,z) space for drawing purposes. 
	 * 
	 * @author jrussom
	 */
	
	private int x;
	private int y;
	private int z;
	private Type type;
	
	/**
	 * Type enum
	 * @author jrussom
	 *
	 */
	public enum Type {
		BEGINNING("Beginning"),
		STRAIGHT("Straight"),
		CORNER("Corner"),
		END("End");
		private final String displayName;
		Type(String displayName) { this.displayName = displayName;}
		
		@Override
		public String toString(){ return displayName;}
	}
	
	/**
	 * Constructs a new Cube of a given type at a specific location.
	 * @param x
	 * @param y
	 * @param z
	 * @param type
	 */
	public Cube(int x, int y, int z, Type type){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + "," + z + "," + type + ")";
	}
}
