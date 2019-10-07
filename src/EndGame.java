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
	
	public static boolean isValid(int x,int y,int m, int n) {
		return (x >= 0 && x < m && y >= 0 && y < n);
	}
	public static Pair getIronManLocation(char[][]grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// '*' a cell that has both Iron Man and a stone
				// '+' a cell that has both Iron Man and thanos
				if(grid[i][j] == 'I' || grid[i][j] == '*' || grid[i][j] == '+')
					return new Pair(i,j);
			}
		}
		return null;
	}
	public boolean isGoal(Object state) {
		int damage = ((State)state).damage;
		char[][] grid = ((State)state).grid;
		
		// if there are more stones to collect or thanos is not killed then this is not our goal
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == 'T' ||grid[i][j] == 'S') {
					return false;
				}
			}
		}
		return damage < 100;
	}

	public Collection<Object> getActions(Object state) {
		int damage = ((State)state).damage;
        ArrayList<Object> actions = new ArrayList<Object>();
        
        if(damage >= 100)
        	return actions;
        
		char[][] grid = ((State)state).grid;
		Pair IronManLocation = getIronManLocation(grid);
		
		return null;
	}

	public Object getNextState(Object state, Object action) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStepCost(Object start, Object action, Object dest) {
		// TODO Auto-generated method stub
		return 0;
	}

}

class Pair{
	public int x;
	public int y;
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
