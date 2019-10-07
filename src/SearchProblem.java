import java.util.Collection;

public interface SearchProblem {
	public Object getInitialState();
    public boolean isGoal(Object state);
    public Collection<Object> getActions(Object state);
    public Object getNextState(Object state,Object action);
    public int getStepCost(Object start, Object action, Object dest);
}
