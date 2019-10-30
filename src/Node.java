import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

	private Object state;
	private Node parent;
	private String action;
	private int pathCost;

	public Node(Object state) {
		this.state = state;
		this.pathCost = 0;
	}

	public Node(Object state, Node parent, String action, int stepCost) {
		this(state);
		this.parent = parent;
		this.action = action;
		this.pathCost = parent.pathCost + stepCost;
	}

	public Object getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

	public Object getAction() {
		return action;
	}

	public int getPathCost() {
		return pathCost;
	}

	public boolean isRootNode() {
		return parent == null;
	}

	public List<Node> getPathFromRoot() {
		List<Node> path = new ArrayList<Node>();
		Node current = this;
		while (!current.isRootNode()) {
			path.add(0, current);
			current = current.getParent();
		}
		// ensure the root node is added
		path.add(0, current);
		return path;
	}

	public String toString() {
		return "action=" + action + ", state=" + getState() + ", pathCost=" + pathCost + "]";
	}

	public String pathToString() {
		String pathString = "";
		List<Node> nodes = getPathFromRoot();
		for (int i = 0; i < nodes.size(); i++) {
			pathString += ("Action : " + nodes.get(i).getAction() + "\n");
			State state = (State) (nodes.get(i).getState());
			for (int ii = 0; ii < state.grid.length; ii++) {
				for (int j = 0; j < state.grid.length; j++) {
					pathString += (state.grid[ii][j] + " ");
				}
				pathString += "\n";
			}
		}
		return pathString;
	}

	public String actionsToString() {
		String actionsString = "";
		List<Node> nodes = getPathFromRoot();
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getAction() != null) {
				actionsString += (nodes.get(i).getAction() + ",");
			}
		}
		actionsString = actionsString.substring(0, actionsString.length() - 1);
		return actionsString;
	}

	@Override
	public int compareTo(Node o) {
		return this.pathCost - o.pathCost;
	}
}