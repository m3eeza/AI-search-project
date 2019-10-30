import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Queue;

public interface SearchAlgorithm {
	public Node solve(SearchProblem problem);

	public int getNumberNodesExpanded();

}

abstract class BaseSearch implements SearchAlgorithm {
	/*
	 * Used for simple searches (BFS, DFS and UCS)
	 */
	HashSet<String> memo = new HashSet<String>();
	Collection<Node> frontier;
	int totalNodes = 0;

	public int getNumberNodesExpanded() {
		return totalNodes;
	}

	public Node solve(SearchProblem problem) {
		frontier = initFrontier(problem);
		frontier.addAll(this.expand(new Node(problem.getInitialState()), problem));
		boolean done = false;
		Node solution = null;
		while (!done) {
			totalNodes++;
			if (frontier.isEmpty()) {
				done = true;
			} else {
				Node node = chooseLeafNode(frontier, problem);
				if (totalNodes % 20000 == 0) {
					// Just a debug to assure that the program has not crashed :D
					System.out.print('.');
				}
				if (problem.isGoal(node.getState())) {
					solution = node;
					done = true;
				} else {
					Collection<Node> possibleNodes = this.expand(node, problem);
					frontier.addAll(possibleNodes);
				}
			}
		}
		return solution;
	}

	public Collection<Node> expand(Node node, SearchProblem problem) {
		Collection<Node> nodes = new ArrayList<Node>();
		Collection<String> actions = problem.getAllowedActions(node.getState());
		for (String action : actions) {
			Object nextState = problem.getNextState(node.getState(), action);
			String value = ((State) (nextState)).getValue();
			if (memo.contains(value)) {
				continue;
			}
			memo.add(value);
			nodes.add(new Node(nextState, node, action, problem.getStepCost(node.getState(), action, nextState)));
		}
		return nodes;
	}

	public abstract Collection<Node> initFrontier(SearchProblem problem);

	public abstract Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem);
}

class DFS extends BaseSearch {
	public Collection<Node> initFrontier(SearchProblem problem) {
		return new Stack<Node>();
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		Stack<Node> stack = (Stack<Node>) (frontier);
		return stack.pop();
	}
}

class BFS extends BaseSearch {
	public Collection<Node> initFrontier(SearchProblem problem) {
		Queue<Node> queue = new LinkedList<Node>();
		return queue;
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		Queue<Node> queue = (Queue<Node>) (frontier);
		return queue.poll();
	}
}

class UCS extends BaseSearch {
	public Collection<Node> initFrontier(SearchProblem problem) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(new DamageFunction());

		return pq;
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		PriorityQueue<Node> pq = (PriorityQueue<Node>) (frontier);
		return pq.poll();
	}
}

class DLS extends DFS {
	int depth;

	public DLS(int depth) {
		this.depth = depth;
	}

	public Collection<Node> expand(Node node, SearchProblem problem) {
		Collection<Node> nodes = new ArrayList<Node>();

		int nodeDepth = node.getPathFromRoot().size();
		if (nodeDepth >= depth) {
			return nodes;
		}

		Collection<String> actions = problem.getAllowedActions(node.getState());
		for (String action : actions) {
			Object nextState = problem.getNextState(node.getState(), action);
			String value = ((State) (nextState)).getValue();
			if (memo.contains(value)) {
				continue;
			}
			memo.add(value);
			nodes.add(new Node(nextState, node, action, problem.getStepCost(node.getState(), action, nextState)));
		}
		return nodes;
	}
}

class IDS implements SearchAlgorithm {

	int depth = 0;
	int totalNodes = 0;

	public Node solve(SearchProblem problem) {
		while (true) {
			depth++;
			DLS dls = new DLS(depth);
			Node result = dls.solve(problem);
			totalNodes += dls.getNumberNodesExpanded();
			if (result != null) {
				return result;
			}
		}
	}

	public int getNumberNodesExpanded() {
		return totalNodes;
	}
}

class BestFirstSearch extends BaseSearch {
	EvaluationFunction evalFunc;

	public BestFirstSearch(EvaluationFunction evalFunc) {
		super();
		this.evalFunc = evalFunc;
	}

	public Collection<Node> initFrontier(SearchProblem problem) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(evalFunc);
		return pq;
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		PriorityQueue<Node> pq = (PriorityQueue<Node>) (frontier);
		return pq.poll();
	}
}

