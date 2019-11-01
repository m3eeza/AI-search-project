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
		// Ensure the root node is added
		path.add(0, current);
		return path;
	}

	public String toString() {
		return "action=" + action + ", state=" + getState() + ", pathCost=" + pathCost + "]";
	}

	public String visualizePath() {
		// Returns a string, visualizing the grid through each step
		// Shitty code incoming ..
		String pathString = "";
		List<Node> nodes = getPathFromRoot();
		for (Node node : nodes) {
			pathString += ("Action : " + node.getAction() + "\n");
			pathString += ("Damage : " + node.getPathCost() + "\n");
			State state = (State) (node.getState());
			for (byte i = 0; i < state.m; i++) {
				for (byte j = 0; j < state.n; j++) {
					byte[] pos = { i, j };
					char output = 'E';
					boolean isStone = Utils.positionExistsIn(state.stonePositions, pos) != -1;
					boolean isWarrior = Utils.positionExistsIn(state.warriorPositions, pos) != -1;
					if (isStone) {
						output = 'S';
					} else if (isWarrior) {
						output = 'W';
					} else if (Utils.isSamePosition(state.thanosPosition, pos)) {
						output = 'T';
					}

					if (Utils.isSamePosition(state.ironManPosition, pos)) {
						if (output == 'E') {
							output = 'I';
						} else if (output == 'S') {
							output = '*';
						} else if (output == 'T') {
							if (!state.isThanosKilled) {
								output = '+';
							} else {
								output = 'I';
							}
						}
					}
					pathString += (output + " ");
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