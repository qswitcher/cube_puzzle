package solver.math;

import solver.snake.Cube;

public class MathUtils {
	private final static int[][] Rx = {
			{1, 0, 0},
			{0, 0, -1},
			{0, 1, 0}
	};
	
	private final static int[][] Ry = {
			{0, 0, 1},
			{0, 1, 0},
			{-1, 0, 0}
	};
	
	private final static int[][] Rz = {
			{0, -1, 0},
			{1, 0, 0},
			{0, 0, 1}
	};
	
	/**
	 * Rotate a point 
	 * @param point
	 * @param origin
	 * @param axis
	 * @return
	 */
	public static Point rotate90(Point point, Point origin, int axis){
		Point pointRel = new Point(point.getX() - origin.getX(),
									point.getY() - origin.getY(),
									point.getZ() - origin.getZ());
		Point result = new Point(0,0,0);
		int[][][] matrix = {Rx, Ry, Rz};
		if(axis > 2 || axis < 0){
			throw new IllegalArgumentException("axis must be 0, 1, or 2");
		}
		
		// do rotation
		result.setX(matrix[axis][0][0]*pointRel.getX() + matrix[axis][0][1]*pointRel.getY() + matrix[axis][0][2]*pointRel.getZ());
		result.setY(matrix[axis][1][0]*pointRel.getX() + matrix[axis][1][1]*pointRel.getY() + matrix[axis][1][2]*pointRel.getZ());
		result.setZ(matrix[axis][2][0]*pointRel.getX() + matrix[axis][2][1]*pointRel.getY() + matrix[axis][2][2]*pointRel.getZ());
		return result;
	}

}
