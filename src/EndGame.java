import java.util.ArrayList;
import java.util.Collection;

public class EndGame implements SearchProblem {
	Object initialState;

	public EndGame(Object initialState) {
		this.initialState = initialState;
	}

	public Object getInitialState() {
		return this.initialState;
	}

	public static boolean isValid(int x, int y, int m, int n) {
		// m is number of rows (height)
		// n is number of cols (width)
		return (x >= 0 && x < m && y >= 0 && y < n);
	}

	public static Pair getIronManLocation(char[][] grid) {
		for (byte i = 0; i < grid.length; i++) {
			for (byte j = 0; j < grid[i].length; j++) {
				// '*' a cell that has both Iron Man and a stone
				// '+' a cell that has both Iron Man and Thanos
				if (grid[i][j] == 'I' || grid[i][j] == '*' || grid[i][j] == '+')
					return new Pair(i, j);
			}
		}
		return null;
	}

	private static byte getNumberOfCollectedStones(char[][] grid) {
		byte existingStones = 0;
		for (byte i = 0; i < grid.length; i++) {
			for (byte j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 'S' || grid[i][j] == '*')
					existingStones++;
			}
		}
		return (byte) (6 - existingStones);
	}

	private static char[][] cloneGrid(char[][] a) {
		char[][] b = new char[a.length][];
		for (byte i = 0; i < a.length; i++) {
			b[i] = new char[a[i].length];
			for (byte j = 0; j < a[i].length; j++)
				b[i][j] = a[i][j];
		}
		return b;
	}
	// ============================================================ //

	public boolean isGoal(Object state) {
		int damage = ((State) state).damage;
		char[][] grid = ((State) state).grid;

		// if there are more stones to collect or Thanos is not killed then this
		// is not our goal
		for (byte i = 0; i < grid.length; i++) {
			for (byte j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 'T' || grid[i][j] == 'S') {
					return false;
				}
			}
		}
		return damage < 100;
	}

	public Collection<String> getAllowedActions(Object state) {
		// Get a list of all possible next actions
		int damage = ((State) state).damage;
		ArrayList<String> actions = new ArrayList<String>();

		if (damage >= 100)
			return actions;

		char[][] grid = ((State) state).grid;
		Pair ironManLocation = getIronManLocation(grid);
		byte numCollectedStones = getNumberOfCollectedStones(grid);
		if (grid[ironManLocation.x][ironManLocation.y] == '*') {
			actions.add("collect");
		}
		if (grid[ironManLocation.x][ironManLocation.y] == '+') {
			actions.add("snap");
		}
		byte m = (byte) grid.length;
		byte n = (byte) grid[0].length;

		if (isValid(ironManLocation.x - 1, ironManLocation.y, m, n)) {
			char cellContent = grid[ironManLocation.x - 1][ironManLocation.y];
			if (cellContent == 'W') {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if (cellContent != 'T' || numCollectedStones == 6) {
				actions.add("up");
			}
		}

		if (isValid(ironManLocation.x + 1, ironManLocation.y, m, n)) {
			char cellContent = grid[ironManLocation.x + 1][ironManLocation.y];
			if (cellContent == 'W') {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if (cellContent != 'T' || numCollectedStones == 6) {
				actions.add("down");
			}
		}

		if (isValid(ironManLocation.x, ironManLocation.y - 1, m, n)) {
			char cellContent = grid[ironManLocation.x][ironManLocation.y - 1];
			if (cellContent == 'W') {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if (cellContent != 'T' || numCollectedStones == 6) {
				actions.add("left");
			}
		}

		if (isValid(ironManLocation.x, ironManLocation.y + 1, m, n)) {
			char cellContent = grid[ironManLocation.x][ironManLocation.y + 1];
			if (cellContent == 'W') {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if (cellContent != 'T' || numCollectedStones == 6) {
				actions.add("right");
			}
		}

		return actions;
	}

	public Object getNextState(Object state, String action) {
		// We're sure that the given action is valid already, since it was handled in
		// the `getAllowedActions`
		char[][] grid = cloneGrid(((State) state).grid);
		byte m = (byte) grid.length;
		byte n = (byte) grid[0].length;
		int damage = ((State) state).damage;
		Pair ironManLocation = getIronManLocation(grid);

		byte newX = ironManLocation.x;
		byte newY = ironManLocation.y;
		byte extraDamage = 0;

		if (action == "up" || action == "down" || action == "left" || action == "right") {
			// Empty the current location.
			if (grid[ironManLocation.x][ironManLocation.y] == '*') {
				grid[ironManLocation.x][ironManLocation.y] = 'S';
			} else {
			grid[ironManLocation.x][ironManLocation.y] = 'E';
			}

			if (action == "up") {
				newX--;
			} else if (action == "down") {
				newX++;
			} else if (action == "left") {
				newY--;
			} else if (action == "right") {
				newY++;
			}

			// Update the grid
			if (grid[newX][newY] == 'T') {
				grid[newX][newY] = '+';
			} else if (grid[newX][newY] == 'S') {
				grid[newX][newY] = '*';
			} else {
				grid[newX][newY] = 'I';
			}

		} else {
			if (action == "collect") {
				// Clear stone cell
				// Causes 3 damage
				grid[ironManLocation.x][ironManLocation.y] = 'I';
				extraDamage += 3;
			} else if (action == "kill") {
				// TODO: Remove this shit
				// Remove adjacent warriors
				// Causes 2 x Num of warriors damage

				if (isValid(newX - 1, newY, m, n)) {
					if (grid[newX - 1][newY] == 'W') {
						extraDamage += 2;
						grid[newX - 1][newY] = 'E';
					}
				}

				if (isValid(newX + 1, newY, m, n)) {
					if (grid[newX + 1][newY] == 'W') {
						extraDamage += 2;
						grid[newX + 1][newY] = 'E';
					}
				}

				if (isValid(newX, newY - 1, m, n)) {
					if (grid[newX][newY - 1] == 'W') {
						extraDamage += 2;
						grid[newX][newY - 1] = 'E';
					}
				}

				if (isValid(newX, newY + 1, m, n)) {
					if (grid[newX][newY + 1] == 'W') {
						extraDamage += 2;
						grid[newX][newY + 1] = 'E';
					}
				}
			} else if (action == "snap") {
				grid[ironManLocation.x][ironManLocation.y] = 'I';
			}
		}

		// Update the damage
		// For each adjacent warrior, inflect 1 damage
		// For adjacent Thanos, inflect an extra 5 damage
		if (isValid(newX - 1, newY, m, n)) {
			char cellContent = grid[newX - 1][newY];
			if (cellContent == 'W') {
				extraDamage++;
			} else if (cellContent == 'T') {
				extraDamage += 5;
			}
		}

		if (isValid(newX + 1, newY, m, n)) {
			char cellContent = grid[newX + 1][newY];
			if (cellContent == 'W') {
				extraDamage++;
			} else if (cellContent == 'T') {
				extraDamage += 5;
			}
		}

		if (isValid(newX, newY - 1, m, n)) {
			char cellContent = grid[newX][newY - 1];
			if (cellContent == 'W') {
				extraDamage++;
			} else if (cellContent == 'T') {
				extraDamage += 5;
			}
		}

		if (isValid(newX, newY + 1, m, n)) {
			char cellContent = grid[newX][newY + 1];
			if (cellContent == 'W') {
				extraDamage++;
			} else if (cellContent == 'T') {
				extraDamage += 5;
			}
		}

		// Return new state

		return new State(grid, damage + extraDamage);
	}

	public int getStepCost(Object start, String action, Object dest) {
		int startDamage = ((State) start).damage;
		int destDamage = ((State) dest).damage;
		return destDamage - startDamage;
	}

}

class Pair {
	public byte x;
	public byte y;

	public Pair(byte x, byte y) {
		this.x = x;
		this.y = y;
	}
}
