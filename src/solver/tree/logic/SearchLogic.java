package solver.tree.logic;

import java.util.HashSet;
import java.util.Set;

import solver.snake.Cube;
import solver.snake.Snake;
import solver.solutionpath.SolutionNode;
import solver.solutionpath.SolutionPath;
import solver.math.Point;

public class SearchLogic {

	public static volatile SearchLogic instance = null;
	
	private SearchLogic(){}
	
	public static SearchLogic getInstance(){
		if(instance == null){
			synchronized (SearchLogic.class){
				if(instance == null){
					instance = new SearchLogic();
				}
			}
		}
		return instance;
		
	}
	
	/**
	 * Computes the children nodes of a given node which correspond to the 
	 * next possible moves. Returns a Set containing the children.
	 * @param node - current node in the path being explored
	 * @param snake - snake being solved
	 * @return next possible moves
	 */
	public Set<SolutionNode> getNextNodes(SolutionNode node, Snake snake){
		Set<SolutionNode> nextNodes = new HashSet<SolutionNode>();
		int direction = node.getDirection();
		Cube.Type type = node.getCubeType();
		SolutionPath path = node.getSolutionPath();
		int curX = path.getEnd().getX();
		int curY = path.getEnd().getY();
		int curZ = path.getEnd().getZ();
		Set<SolutionNode> candidateNodes = new HashSet<SolutionNode>();
		
		switch(direction){
		case 0:	// x
			switch(type){
			case CORNER:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ+1)), 2));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY+1, curZ  )), 1));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY-1, curZ  )), 1));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ-1)), 2));
				break;
			case END:
				break;
			default:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX+1, curY  , curZ  )), 0));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX-1, curY  , curZ  )), 0));
				break;
			}
			break;
		case 1: // y
			switch(type){
			case CORNER:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ+1)), 2));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX+1, curY  , curZ  )), 0));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX-1, curY  , curZ  )), 0));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ-1)), 2));
				break;
			case END:
				break;
			default:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY+1, curZ  )), 1));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY-1, curZ  )), 1));
				break;
			}
			break;
		case 2: // z
			switch(type){
			case CORNER:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY+1, curZ  )), 1));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX+1, curY  , curZ  )), 0));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX-1, curY  , curZ  )), 0));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY-1, curZ  )), 1));
				break;
			case END:
				break;
			default:
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ+1)), 2));
				candidateNodes.add(new SolutionNode(node.getSnake(), new SolutionPath(path, new Point(curX  , curY  , curZ-1)), 2));
				break;
			}
			break;
		}
		
		// remove points that are physically impossible
		for(SolutionNode candidateNode: candidateNodes){
			if(!path.contains(candidateNode.getPoint()) && isInBox(candidateNode.getPoint(), snake.getBoxSize())){
				nextNodes.add(candidateNode);
			}
		}
		return nextNodes;
	}
	
	/**
	 * Attempts to find solutions to a given snake.
	 * @param snake
	 */
	public Set<SolutionPath> solveSnake(Snake snake){
		// starting point
		int startX = 0;
		int startY = 0;
		int startZ = 0;
		int startDirection = 0;
		SolutionPath solutionPath = new SolutionPath(new Point(startX,startY,startZ));
		
		// generate start node
		SolutionNode startNode = new SolutionNode(snake, solutionPath, startDirection);
				
		// Find solutions
		System.out.println("Calculating solutions...");
		Set<SolutionPath> solutions = DFS_VISIT(startNode,snake);
		System.out.println("The solutions are: " + System.getProperty("line.separator"));
		for(SolutionPath solution: solutions){
			System.out.println(solution.toString());
		}
		return solutions;
		
	}
	
	/**
	 * DFS search 
	 * @param node
	 * @param snake
	 * @return set of all solutions
	 */
	private Set<SolutionPath> DFS_VISIT(SolutionNode node, Snake snake){
		Set<SolutionPath> solutions = new HashSet<SolutionPath>();
		
		Set<SolutionNode> neighbors = getNextNodes(node,snake);
		for(SolutionNode v: neighbors){
			if(v.getDepth() == snake.getCubeList().size() - 1){
				System.err.println("FOUND!");
				System.err.println(v.getSolutionPath().toString());
				solutions.add(v.getSolutionPath());
			}
			Set<SolutionPath> result = DFS_VISIT(v, snake);
			solutions.addAll(result);
		}
		return solutions;
	}
	
	/**
	 * Checks if a point is inside of the constraint box.
	 * @param point
	 * @param size
	 * @return
	 */
	private boolean isInBox(Point point, int size){
		return point.getX() >= 0 && point.getX() <  size &&
				point.getY() >= 0 && point.getY() < size &&
				 point.getZ() >= 0 && point.getZ() < size;
	}
	
}
