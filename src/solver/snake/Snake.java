package solver.snake;

import java.util.ArrayList;
import java.util.List;

import solver.snake.Cube.Type;

public class Snake {
	/**
	 * This class represents the snake puzzle itself. It contains the list of Cube objects representing
	 * the individual cubes, their types, and locations.
	 * 
	 *  @author jrussom
	 */

	private List<Cube> cubes;
	private int boxSize;
	
	/**
	 * Creates a new snake which solves to form a box of size <code>boxSize</code>.
	 * @param snakeTypes - array of block types
	 * @param snakeLength
	 * @param boxSize
	 */
	public Snake(Type[] snakeTypes, int boxSize){
		// check that the length makes sense
		if(snakeTypes.length != boxSize*boxSize*boxSize){
			throw new IllegalArgumentException("The snake length must be equal to boxSize^3!");
		}
		
		// do initialization
		this.boxSize = boxSize;
		int x = 0;
		int y = 0;
		int z = 0;
		cubes = new ArrayList<Cube>();

		// initialize Cube objects and their locations.
		boolean goingStraight = true;
		for(Type type: snakeTypes){
			Cube cube = new Cube(x,y,z,type);
			cubes.add(cube);
			if(type.equals(Type.CORNER)){
				boolean temp = !goingStraight;
				goingStraight = temp;
			} 
			if(goingStraight){
				x++;
			}else{
				y++;
			}
		}
		
	}
	
	public List<Cube> getCubeList(){
		return cubes;
	}
	
	public void addCube(Cube cube){
		cubes.add(cube);
	}
	
	public Cube.Type typeAtIndex(int i){
		return cubes.get(i).getType();
	}

	public int getBoxSize() {
		return boxSize;
	}
	
	@Override
	public String toString(){
		String result = "Snake: ";
		for(Cube cube: cubes){
			if(cubes.indexOf(cube) != 0){
				result += ", ";
			}
			result += cube.toString();
		}
		return result;
	}
}
