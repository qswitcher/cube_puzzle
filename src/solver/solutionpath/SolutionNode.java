package solver.solutionpath;

import solver.math.Point;
import solver.snake.Cube;
import solver.snake.Snake;

/**
 * This class encodes a possible solution chain starting from the beginning of the snake up 
 * to a given cube.
 * @author jrussom
 *
 */
public class SolutionNode {
	private SolutionPath path;
	private int direction = -1;
	private int depth = 0;		// distance from beginning of snake starting at 0
	private Cube.Type type;
	private Snake snake;
	
	public SolutionNode(Snake snake, SolutionPath path, int direction){
		this.depth = path.getPoints().size() - 1;
		this.type = snake.typeAtIndex(this.depth);
		this.path = path;
		this.snake = snake;
		this.direction = direction;
	}
	
	public SolutionPath getSolutionPath(){
		return path;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	public int getDepth(){
		return this.depth;
	}
	
	public Cube.Type getCubeType(){
		return this.type;
	}
	
	public Snake getSnake(){
		return snake;
	}

	public Point getPoint(){
		return this.path.getEnd();
	}
	
	@Override 
	public String toString(){
		return path.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof SolutionNode){
			SolutionNode other = (SolutionNode) o;
			return other.getSolutionPath().equals(this.getSolutionPath());
		}
		return false;
	}

}
