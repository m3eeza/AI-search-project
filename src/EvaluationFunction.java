import java.util.Comparator;

public abstract class EvaluationFunction implements Comparator<Node> {

	@Override
	public abstract int compare(Node o1, Node o2);

}

class DamageFunction extends EvaluationFunction {

	@Override
	public int compare(Node o1, Node o2) {
		return o1.getPathCost() - o2.getPathCost();
	}

}

class H1 extends EvaluationFunction {

	@Override
	public int compare(Node o1, Node o2) {

		return distanceToGoal(o1.getState()) - distanceToGoal(o2.getState());
	}

	public byte distanceToGoal(Object state) {
		char[][] grid = ((State) state).grid;
		byte distance = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 'S')
					distance+=3;
			}
		}
		return distance;
	}

}

class H2 extends EvaluationFunction {

	@Override
	public int compare(Node o1, Node o2) {

		return distanceToGoal(o1.getState()) - distanceToGoal(o2.getState());
	}

	public static boolean isValid(int x, int y, int m, int n) {
		// m is number of rows (height)
		// n is number of cols (width)
		return (x >= 0 && x < m && y >= 0 && y < n);
	}

	public byte distanceToGoal(Object state) {
		char[][] grid = ((State) state).grid;
		int m = grid.length;
		int n = grid[0].length;
		byte distance = 0;
		byte[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
		for (byte i = 0; i < grid.length; i++) {
			for (byte j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 'S') {
					distance+=3;
					for (byte k = 0; k < directions.length; k++) {
						byte x = (byte) (i + directions[k][0]);
						byte y = (byte) (j + directions[k][1]);
						if (isValid(x, y, m, n) && grid[x][y] == 'W') {
							distance++;
						}
					}
				}
			}
		}
		return distance;
	}

}

class compoundEvalFunc extends EvaluationFunction {

	EvaluationFunction e1, e2;

	public compoundEvalFunc(EvaluationFunction e1, EvaluationFunction e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public int compare(Node o1, Node o2) {

		return e1.compare(o1, o2) + e2.compare(o1, o2);
	}

}