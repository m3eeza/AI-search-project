public class State {
	public char[][] grid;
	public int damage;

	public State(char[][] grid, int damage) {
		this.grid = grid;
		this.damage = damage;
	}

	public String getValue() {
		// Use this to serialize the state (for handling repeated states)
		String s = "";
		for (byte i = 0; i < this.grid.length; i++) {
			for (byte j = 0; j < this.grid.length; j++) {
				s += grid[i][j];
			}
		}
		// s += ";";
		// s += damage;
		return s;
	}

}
