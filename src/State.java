public class State {
	public char[][] grid;
	public int damage;

	public State(char[][] grid, int damage) {
		this.grid = grid;
		this.damage = damage;
	}

	// public String toString() {
	// String str = "";
	// for (int i = 0; i < this.grid.length; i++) {
	// for (int j = 0; j < this.grid.length; j++) {
	// str += (this.grid[i][j] + " ");
	// }
	// str += "\n";
	// }
	// return str;
	// }
}
