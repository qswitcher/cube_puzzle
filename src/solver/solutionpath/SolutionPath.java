package solver.solutionpath;

import java.util.ArrayList;
import java.util.List;

import solver.math.Point;

/**
 * Class which encodes the path taken to get to a certain point in the solution starting
 * from the beginning of the snake.
 * @author jrussom
 *
 */
public class SolutionPath {

	private List<Point> rest;	// Solution points starting from the beginning of the snake
										// leading up to "end".
	private Point end;			// Point at the end of the path
	
	public SolutionPath(){
		rest = new ArrayList<Point>();
		end = null;
	}
	
	public SolutionPath(SolutionPath path, Point point){
		this.rest = path.getRest();
		this.rest.add(path.getEnd());
		this.end = point;
	}
	
	public SolutionPath(Point point){
		this.rest = new ArrayList<Point>();
		this.end = point;
	}
	
	/**
	 * Return a copy to avoid overwriting internal values
	 * @return
	 */
	public List<Point> getRest(){
		ArrayList<Point> result = new ArrayList<Point>();
		result.addAll(this.rest);
		return result;
	}
	
	public Point getEnd(){
		return end;
	}
	
	public List<Point> getPoints(){
		List<Point> result = new ArrayList<Point>();
		result.addAll(this.rest);
		result.add(end);
		return result;
	}
	
	public boolean contains(Point point){
		return rest.contains(point) || end.equals(point);
	}
	
	@Override
	public String toString(){
		String result =  "Solution path length(" + (rest.size() + 1) + "): ";
		for(Point point : rest){
			result += point.toString() + "->";
		}
		result += end.toString();
		return result;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof SolutionPath){
			SolutionPath path = (SolutionPath) o;
			return path.getRest().equals(this.rest) && path.getEnd().equals(this.end);
		}else{
			return false;
		}
	}
}
