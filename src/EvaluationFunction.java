import java.util.Comparator;

public abstract class EvaluationFunction implements Comparator<Node>{

	@Override
	public abstract int compare(Node o1, Node o2);

}


class DamageFunction extends EvaluationFunction {

	@Override
	public int compare(Node o1, Node o2) {
		return o1.getPathCost() - o2.getPathCost();
	}
	
}
