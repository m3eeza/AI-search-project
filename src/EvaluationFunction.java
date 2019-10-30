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

	String heuristicGoal;

	public H1(State initialState) {
		String s = initialState.getValue();
		heuristicGoal = s.replace('S', 'E');
		heuristicGoal.replace('I', 'E');
		heuristicGoal.replace('T', 'I');
	}

	@Override
	public int compare(Node o1, Node o2) {

		return distanceToGoal(o1.getState()) - distanceToGoal(o2.getState());
	}

	public short distanceToGoal(Object state) {
		String s = ((State) state).getValue();
		short distance = 0;
		for (short i = 0; i < s.length(); i++) {
			if (s.charAt(i) != heuristicGoal.charAt(i)) {
				distance++;
			}
		}
		return distance;
	}

}

class H1Damage extends EvaluationFunction {

	char[] heuristicGoal;

	public H1Damage(State initialState) {
		String s = initialState.getValue();
		heuristicGoal = s.replace('S', 'E').toCharArray();
	}

	@Override
	public int compare(Node o1, Node o2) {

		return distanceToGoal(o1.getState()) + o1.getPathCost() - distanceToGoal(o2.getState()) - o2.getPathCost();
	}

	public short distanceToGoal(Object state) {
		char[] s = ((State) state).getValue().toCharArray();
		short distance = 0;
		for (short i = 0; i < s.length; i++) {
			if (s[i] != heuristicGoal[i]) {
				distance++;
			}
		}
		return distance;
	}

}