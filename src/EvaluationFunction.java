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
	// The heuristic only considers the number of damage inflected by
	// the available stones, which is inevitable to be collected.

	@Override
	public int compare(Node o1, Node o2) {

		return distanceToGoal(o1.getState()) - distanceToGoal(o2.getState());
	}

	public byte distanceToGoal(Object state) {
		return (byte) (((State) state).stonePositions.length * 3);
	}

}

class H2 extends EvaluationFunction {
	// Heuristic that looks at the damage inflicted by the remaining
	// stones, while also considering any surrounding warriors
	// to the stone (which the agent will have to face)
	@Override
	public int compare(Node o1, Node o2) {
		return distanceToGoal(o1.getState()) - distanceToGoal(o2.getState());
	}

	public byte distanceToGoal(Object s) {
		State state = (State) s;
		byte distance = 0;
		for (byte[] stonePos : state.stonePositions) {
			distance += 3;
			byte[] newPos = stonePos.clone();
			for (byte[] pos : Utils.directions.values()) {
				newPos[0] += pos[0];
				newPos[1] += pos[1];
				if (Utils.positionExistsIn(state.warriorPositions, newPos) != -1) {
					// It means that there exists a warrior around the stone in that cell
					distance++;
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