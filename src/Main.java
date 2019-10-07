import java.util.Arrays;
import java.util.Scanner;

public class Main {
	

	public static char[][] gridParser(String grid){
		int ns = grid.split(";")[3].split(",").length; //number of stones
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
		for (char[] row: map)
		    Arrays.fill(row, 'E');
		map[ix][iy] = 'I';
		map[tx][ty] = 'T';
		for (int i = 0; i < ns; i+=2) {
			int six = sc.nextInt();
			int siy = sc.nextInt();
			map[six][siy] = 'S';
		}
		for (int i = 0; i < nw; i+=2) {
			int wix = sc.nextInt();
			int wiy = sc.nextInt();
			map[wix][wiy] = 'W';
		}
		sc.close();
		return map;
	}
	public static String solve(String grid, String strategy, boolean visualize) {
		String solution = "";
		return solution;
		
	}
	public static void main(String[] args) {
		String grid = "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		char [][] map = gridParser(grid);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(map[i][j]+" ");
			}
			System.out.println("");
		}
	}

}
