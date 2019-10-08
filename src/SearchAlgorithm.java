import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

public abstract class SearchAlgorithm {

	Collection<Node> frontier;

	public Node solve(SearchProblem problem) {
		System.out.println("Solving...");
		frontier = initFrontier();
		frontier.addAll(this.expand(new Node(problem.getInitialState()), problem));
		System.out.println("Starting frontier is " + frontier);
		boolean done = false;
		Node solution = null;
		while (!done) {
			if (frontier.isEmpty()) {
				System.out.println("No more nodes in frontier => FAILURE");
				done = true;
			} else {
				Node node = chooseLeafNode(frontier, problem);
				System.out.println("Inspecting node " + node);
				if (problem.isGoal(node.getState())) {
					System.out.println("Goal node reached => SUCCESS");
					solution = node;
					done = true;
				} else {
					Collection<Node> possibleNodes = this.expand(node, problem);
					// TODO:: Check repeated states??
					frontier.addAll(possibleNodes);
					// System.out.printf("Expanding node, frontier is " +
					// frontier);
				}
			}
		}
		return solution;
	}

	public Collection<Node> expand(Node node, SearchProblem problem) {
		Collection<Node> nodes = new ArrayList<Node>();
		Collection<String> actions = problem.getAllowedActions(node.getState());
		for (String action : actions) {
			Object next = problem.getNextState(node.getState(), action);
			nodes.add(new Node(next, node, action, problem.getStepCost(node.getState(), action, next)));
		}
		return nodes;
	}

	public abstract Collection<Node> initFrontier();

	public abstract Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem);
}

class DFS extends SearchAlgorithm {
	public Collection<Node> initFrontier() {
		return new Stack<Node>();
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		Stack<Node> stack = (Stack<Node>) (frontier);
		return stack.pop();
	}
}

class BFS extends SearchAlgorithm {
	public Collection<Node> initFrontier() {
		Queue<Node> queue = new LinkedList<Node>();
		return queue;
	}

	public Node chooseLeafNode(Collection<Node> frontier, SearchProblem problem) {
		Queue<Node> queue = (Queue<Node>) (frontier);
		return queue.poll();
	}
}