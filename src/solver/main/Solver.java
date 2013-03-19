package solver.main;

import java.util.Set;

import javax.swing.text.StyledEditorKit.StyledTextAction;

import solver.snake.Cube;
import solver.snake.Snake;
import solver.solutionpath.SolutionPath;
import solver.tree.logic.SearchLogic;

public class Solver {
	private final static Cube.Type END = Cube.Type.END;
	private final static Cube.Type STRAIGHT = Cube.Type.STRAIGHT;
	private final static Cube.Type CORNER = Cube.Type.CORNER;
	private final static Cube.Type BEGINNING = Cube.Type.BEGINNING;
	private static Cube.Type[] snakeTypes3x3 = {
			BEGINNING, 		STRAIGHT, 	CORNER, 	CORNER, 	CORNER, 	STRAIGHT, 
			CORNER, 	CORNER, 	STRAIGHT,	CORNER, 	CORNER, 	CORNER, 	
			STRAIGHT, 	CORNER, 	STRAIGHT, 	CORNER, 	CORNER, 	CORNER, 
			CORNER,		STRAIGHT, 	CORNER, 	STRAIGHT, 	CORNER, 	STRAIGHT, 
			CORNER, 	STRAIGHT, 	END
	};
	
	private static Cube.Type[] snakeTypes4x4 = {
		BEGINNING,
		STRAIGHT, CORNER,   CORNER,   STRAIGHT, CORNER,   CORNER,   CORNER,   STRAIGHT, 
		STRAIGHT, CORNER,   CORNER,   STRAIGHT, CORNER,   CORNER,   STRAIGHT, CORNER,   
		CORNER,   STRAIGHT, CORNER,   CORNER,   CORNER,   CORNER,   CORNER,   CORNER,   
		CORNER,   CORNER,   CORNER,   STRAIGHT, CORNER,   STRAIGHT, CORNER,   CORNER,   
		CORNER,   CORNER,   CORNER,   CORNER,   STRAIGHT, CORNER,   STRAIGHT, STRAIGHT,
		CORNER,   CORNER,   CORNER,   CORNER,   STRAIGHT, STRAIGHT, CORNER,   CORNER,   
		STRAIGHT, CORNER,   CORNER,   CORNER,   CORNER,   CORNER,   CORNER,   CORNER,   
		CORNER,   CORNER,   CORNER,   STRAIGHT, STRAIGHT, CORNER,   
		END
	};
	
	public static Set<SolutionPath> solve(int snakeType){
		Snake snake;
		if(snakeType == 4){
			snake = new Snake(snakeTypes4x4,4);
		} else if (snakeType == 3){
			snake = new Snake(snakeTypes3x3,3);			
		} else{
			throw new IllegalArgumentException("Support for only 4x4 or 3x3 type snake.");
		}
		
		System.out.println("Initial snake: " + snake.toString());
		
		SearchLogic logic = SearchLogic.getInstance();
		long startTime = System.currentTimeMillis();
		Set<SolutionPath> solutions = logic.solveSnake(snake);
		long stopTime = System.currentTimeMillis();
		System.out.println("It took " + (stopTime - startTime) + " ms to find the solution.");		
		return solutions;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		solve(3);
	}

}
