import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static char[][] gridParser(String grid) {
		int ns = grid.split(";")[3].split(",").length; // number of stones
		int nw = grid.split(";")[4].split(",").length; // number of warriors
		Scanner sc = new Scanner(grid);
		sc.useDelimiter(";|,");
		int m = sc.nextInt();
		int n = sc.nextInt();
		int ix = sc.nextInt();
		int iy = sc.nextInt();
		int tx = sc.nextInt();
		int ty = sc.nextInt();
		char[][] map = new char[m][n];
		for (char[] row : map)
			Arrays.fill(row, 'E');
		map[ix][iy] = 'I';
		map[tx][ty] = 'T';
		for (int i = 0; i < ns; i += 2) {
			int six = sc.nextInt();
			int siy = sc.nextInt();
			map[six][siy] = 'S';
		}
		for (int i = 0; i < nw; i += 2) {
			int wix = sc.nextInt();
			int wiy = sc.nextInt();
			map[wix][wiy] = 'W';
		}
		sc.close();
		return map;
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		char[][] map = gridParser(grid);
		EndGame e = new EndGame(new State(map, 0));
		SearchAlgorithm algorithm = null;
		switch (strategy) {
		case "BFS":
			algorithm = new BFS();
			break;
		case "DFS":
			algorithm = new DFS();
		default:
			break;
		}
		Node solution = algorithm.solve(e);
		if (visualize) {
			System.out.println(solution.pathToString());
			State state = (State) (solution.getState());
			System.out.println("=======FINAL MAP=======");
			System.out.println("Took damage: " + state.damage);
			for (int i = 0; i < state.grid.length; i++) {
				for (int j = 0; j < state.grid.length; j++) {
					System.out.print(state.grid[i][j] + " ");
				}
				System.out.println("");
			}
			System.out.println("Total nodes: " + algorithm.getNumberNodesExpanded());
		}
		return "plan;cost;nodes";

	}

	public static void main(String[] args) {
		String grid = "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		solve(grid, "BFS", true);
	}

}
