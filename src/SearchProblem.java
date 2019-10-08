import java.util.Collection;

public interface SearchProblem {
    public Object getInitialState();

    public boolean isGoal(Object state);

    public Collection<String> getAllowedActions(Object state);

    public Object getNextState(Object state, String action);

    public int getStepCost(Object start, String action, Object dest);
}
