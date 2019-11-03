import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class Main {

	public static State getState(String grid) {
		// Divide by two since each position has 2 components (x, y)
		byte numStones = (byte) (grid.split(";")[3].split(",").length / 2);
		byte numWarriors = (byte) (grid.split(";")[4].split(",").length / 2);
		Scanner sc = new Scanner(grid);
		sc.useDelimiter(";|,");

		byte m = (byte) sc.nextInt();
		byte n = (byte) sc.nextInt();
		byte ix = (byte) sc.nextInt();
		byte iy = (byte) sc.nextInt();
		byte tx = (byte) sc.nextInt();
		byte ty = (byte) sc.nextInt();

		byte[] ironManPosition = { ix, iy };
		byte[] thanosPosition = { tx, ty };
		byte[][] stonePositions = new byte[numStones][2];
		byte[][] warriorPositions = new byte[numWarriors][2];

		for (byte i = 0; i < numStones; i++) {
			stonePositions[i][0] = (byte) sc.nextInt();
			stonePositions[i][1] = (byte) sc.nextInt();
		}
		for (byte i = 0; i < numWarriors; i++) {
			warriorPositions[i][0] = (byte) sc.nextInt();
			warriorPositions[i][1] = (byte) sc.nextInt();
		}
		sc.close();

		return new State(m, n, ironManPosition, thanosPosition, stonePositions, warriorPositions, ((byte) 0));
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		EndGame e = new EndGame(getState(grid));
		SearchAlgorithm algorithm = null;
		switch (strategy) {
		case "BF":
			algorithm = new BFS();
			break;
		case "DF":
			algorithm = new DFS();
			break;
		case "UC":
			algorithm = new BestFirstSearch(new DamageFunction());
			break;
		case "ID":
			algorithm = new IDS();
			break;
		case "GR1":
			algorithm = new BestFirstSearch(new H1());
			break;
		case "GR2":
			algorithm = new BestFirstSearch(new H2());
			break;
		case "AS1":
			algorithm = new BestFirstSearch(new compoundEvalFunc(new H1(), new DamageFunction()));
			break;
		case "AS2":
			algorithm = new BestFirstSearch(new compoundEvalFunc(new H2(), new DamageFunction()));
			break;
		default:
			break;
		}
		Node solution = algorithm.solve(e);
		if (solution == null)
			return "There is no solution";
		State solutionState = (State) (solution.getState());
		if (visualize) {
			System.out.println(solution.visualizePath());
			System.out.println("====================================");
			System.out.println("Took damage: " + solutionState.damage);
			System.out.println("Total nodes: " + algorithm.getNumberNodesExpanded());
		}
		return (solution.actionsToString() + ";" + solutionState.damage + ";" + algorithm.getNumberNodesExpanded());

	}

	public static void main(String[] args) {
		String grid5 = "5,5;2,2;4,2;4,0,1,2,3,0,2,1,4,1,2,4;3,2,0,0,3,4,4,3,4,4";
		String grid15 = "15,15;12,13;5,7;7,0,9,14,14,8,5,8,8,9,8,4;6,6,4,3,10,2,7,4,3,11";
		String grid14 = "14,14;2,13;12,7;8,6,9,4,7,1,4,4,4,7,2,3;8,13,0,4,0,8,5,7,10,0";

		long startTime = System.nanoTime();
		String solutionString = solve(grid14, "DF", true);
		long stopTime = System.nanoTime();
		System.out.println(solutionString);
		System.out.println("Time elapsed: " + (stopTime - startTime) / 1e9f);
	}
}
