package solver.main;

import java.util.Set;

import solver.solutionpath.SolutionPath;
import solver.ui.SolverGraphic;

public class CubeSnakePuzzle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int snakeType = 3;
		Set<SolutionPath> solutions = Solver.solve(snakeType);
		
		SolverGraphic solverUI = new SolverGraphic(snakeType);
		solverUI.setSolutions(solutions);
		solverUI.setVisible(true);
		solverUI.start();
	}

}
