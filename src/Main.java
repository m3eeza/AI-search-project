import java.util.Arrays;
import java.util.Collection;
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
		case "BF":
			algorithm = new BFS();
			break;
		case "DF":
			algorithm = new DFS();
			break;
		case "UC":
			algorithm = new UCS();
			break;
		case "ID":
			algorithm = new IDS();
			break;
		case "GR1":
			algorithm = new BestFirstSearch(new H1((State) e.initialState));
			break;
		case "GR2":
			// algorithm = new IDS();
			return "";
		case "AS1":
			algorithm = new BestFirstSearch(new H1Damage((State) e.initialState));
			break;
		case "AS2":
			// algorithm = new IDS();
			return "";
		default:
			break;
		}
		Node solution = algorithm.solve(e);
		if (solution == null)
			return "There is no solution";
		State solutionState = (State) (solution.getState());
		if (visualize) {
			System.out.println(solution.pathToString());
			System.out.println();
			System.out.println("=======FINAL MAP=======");
			System.out.println("Took damage: " + solutionState.damage);
			for (int i = 0; i < solutionState.grid.length; i++) {
				for (int j = 0; j < solutionState.grid.length; j++) {
					System.out.print(solutionState.grid[i][j] + " ");
				}
				System.out.println("");
			}
			System.out.println("Total nodes: " + algorithm.getNumberNodesExpanded());
		}
		return (solution.actionsToString() + ";" + solutionState.damage + ";" + algorithm.getNumberNodesExpanded());

	}

	public static void main(String[] args) {
		String grid = "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		String grid5 = "5,5;2,2;4,2;4,0,1,2,3,0,2,1,4,1,2,4;3,2,0,0,3,4,4,3,4,4";
		String grid6 = "6,6;5,3;0,1;4,3,2,1,3,0,1,2,4,5,1,1;1,3,3,3,1,0,1,4,2,2";
		String grid7 = "7,7;5,4;0,1;5,0,5,6,3,1,4,3,1,2,6,3;2,5,2,6,1,0,5,5,6,5";
		String grid8 = "8,8;7,2;2,2;7,6,2,3,3,0,0,1,6,0,5,5;7,3,4,4,1,6,2,4,2,6";
		String grid9 = "9,9;2,5;3,3;6,2,5,1,3,0,2,8,8,3,0,5;5,4,5,5,1,6,6,3,4,8";
		String grid10 = "10,10;5,1;0,4;3,1,6,8,1,2,9,2,1,5,0,8;7,8,7,6,3,3,6,0,3,8";
		String grid11 = "11,11;9,5;7,1;9,0,8,8,9,1,8,4,2,3,9,10;2,0,0,10,6,3,10,6,6,2";
		String grid12 = "12,12;0,6;9,11;8,3,3,0,11,8,7,4,7,7,10,2;2,8,11,2,2,6,4,6,9,8";
		String grid13 = "13,13;4,2;2,4;6,1,1,10,8,4,9,2,2,8,9,4;6,4,3,4,3,11,1,12,1,9";
		String grid14 = "14,14;2,13;12,7;8,6,9,4,7,1,4,4,4,7,2,3;8,13,0,4,0,8,5,7,10,0";
		String grid15 = "15,15;12,13;5,7;7,0,9,14,14,8,5,8,8,9,8,4;6,6,4,3,10,2,7,4,3,11";
		long startTime = System.nanoTime();
		String solutionString = solve(grid, "UC", false);
		long stopTime = System.nanoTime();
		System.out.println();
		System.out.println(solutionString);

		System.out.println("Time elapsed: " + (stopTime - startTime) / 1e9f);
		startTime = System.nanoTime();
		solutionString = solve(grid, "AS1", false);
		stopTime = System.nanoTime();
		System.out.println();
		System.out.println(solutionString);

		System.out.println("Time elapsed: " + (stopTime - startTime) / 1e9f);
	}

}
