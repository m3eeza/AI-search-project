import java.util.ArrayList;
import java.util.List;

public class Node {

    private Object state;
    private Node parent;
    private Object action;
    private int pathCost;

    public Node(Object state) {
        this.state = state;
        this.pathCost = 0;
    }

    public Node(Object state, Node parent, Object action, int stepCost) {
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
        return "[parent=" + parent + ", action=" + action + ", state="
                + getState() + ", pathCost=" + pathCost + "]";
    }

    public String pathToString() {
        String pathString = "";
        List<Node> nodes = getPathFromRoot();
        for (int i = 0; i < nodes.size(); i++) {
            pathString += ("Action : " + nodes.get(i).getAction() + "\n");
            pathString += ("State  : " + nodes.get(i).getState()+ "\n");
        }
        return pathString;
    }
}