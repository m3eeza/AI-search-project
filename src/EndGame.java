import java.util.ArrayList;
import java.util.Collection;

public class EndGame implements SearchProblem {
	Object initialState;

	public EndGame(Object initialState) {
		this.initialState = initialState;
	}

	public Object getInitialState() {
		return this.initialState;
	}

	public static boolean isValid(byte[] pos, byte m, byte n) {
		// m >> rows (height); n >> cols (width)
		return (pos[0] >= 0 && pos[0] < m && pos[1] >= 0 && pos[1] < n);
	}

	public boolean isGoal(Object s) {
		// If Thanos is not killed yet, or killed but the damage
		// is greater than a 100, then it's not a goal state.
		State state = ((State) s);
		if (!state.isThanosKilled)
			return false;

		return state.damage < 100;
	}

	public Collection<String> getAllowedActions(Object s) {
		// Get a list of all possible next actions
		ArrayList<String> actions = new ArrayList<String>();
		State state = ((State) s);
		byte damage = state.damage;

		if (damage >= 100)
			return actions;

		byte numCollectedStones = (byte) (6 - state.stonePositions.length);
		boolean isOnStone = Utils.positionExistsIn(state.stonePositions, state.ironManPosition) != -1;
		boolean isOnThanos = Utils.isSamePosition(state.thanosPosition, state.ironManPosition);
		if (isOnStone) {
			actions.add("collect");
			// return actions; // This is an extra optimization, do we need it ?
		}
		if (isOnThanos) {
			actions.add("snap");
		}

		byte[] currPos = { (byte) (state.ironManPosition[0] - 1), state.ironManPosition[1] };
		if (isValid(currPos, state.m, state.n)) {
			if (Utils.positionExistsIn(state.warriorPositions, currPos) != -1) {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if ((!Utils.isSamePosition(state.thanosPosition, currPos)) || numCollectedStones == 6) {
				actions.add("up");
			}
		}

		currPos[0] = (byte) (state.ironManPosition[0] + 1);
		currPos[1] = state.ironManPosition[1];
		if (isValid(currPos, state.m, state.n)) {
			if (Utils.positionExistsIn(state.warriorPositions, currPos) != -1) {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if ((!Utils.isSamePosition(state.thanosPosition, currPos)) || numCollectedStones == 6) {
				actions.add("down");
			}
		}

		currPos[0] = state.ironManPosition[0];
		currPos[1] = (byte) (state.ironManPosition[1] - 1);
		if (isValid(currPos, state.m, state.n)) {
			if (Utils.positionExistsIn(state.warriorPositions, currPos) != -1) {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if ((!Utils.isSamePosition(state.thanosPosition, currPos)) || numCollectedStones == 6) {
				actions.add("left");
			}
		}

		currPos[0] = state.ironManPosition[0];
		currPos[1] = (byte) (state.ironManPosition[1] + 1);
		if (isValid(currPos, state.m, state.n)) {
			if (Utils.positionExistsIn(state.warriorPositions, currPos) != -1) {
				if (!actions.contains("kill")) {
					actions.add("kill");
				}
			} else if ((!Utils.isSamePosition(state.thanosPosition, currPos)) || numCollectedStones == 6) {
				actions.add("right");
			}
		}

		return actions;
	}

	public Object getNextState(Object s, String action) {
		// We're sure that the given action is valid already, since it was handled in
		// the `getAllowedActions`

		State state = (State) s;
		byte[] newIronManPosition = state.ironManPosition.clone();
		byte[][] newStonePositions = state.stonePositions.clone();
		byte[][] newWarriorsPositions = state.warriorPositions.clone();
		boolean newIsThanosKilled = state.isThanosKilled;

		byte m = state.m;
		byte n = state.n;

		byte newX = state.ironManPosition[0];
		byte newY = state.ironManPosition[1];
		byte extraDamage = 0;

		byte[] currPos = { 0, 0 };
		if (action == "up" || action == "down" || action == "left" || action == "right") {

			if (action == "up") {
				newX--;
			} else if (action == "down") {
				newX++;
			} else if (action == "left") {
				newY--;
			} else if (action == "right") {
				newY++;
			}

			// Update the location
			newIronManPosition[0] = newX;
			newIronManPosition[1] = newY;

		} else {
			if (action == "collect") {
				// Clear stone cell
				// Causes 3 damage
				for (byte i = 0; i < newStonePositions.length; i++) {
					if (newStonePositions[i][0] == newX && newStonePositions[i][1] == newY) {
						newStonePositions[i] = null;
					}
				}
				extraDamage += 3;
			} else if (action == "kill") {
				currPos[0] = (byte) (newX - 1);
				currPos[1] = newY;
				if (isValid(currPos, m, n)) {
					byte warriorPos = Utils.positionExistsIn(state.warriorPositions, currPos);
					if (warriorPos != -1) {
						extraDamage += 2;
						newWarriorsPositions[warriorPos] = null;
					}
				}

				currPos[0] = (byte) (newX + 1);
				currPos[1] = newY;
				if (isValid(currPos, m, n)) {
					byte warriorPos = Utils.positionExistsIn(state.warriorPositions, currPos);
					if (warriorPos != -1) {
						extraDamage += 2;
						newWarriorsPositions[warriorPos] = null;
					}
				}

				currPos[0] = newX;
				currPos[1] = (byte) (newY - 1);
				if (isValid(currPos, m, n)) {
					byte warriorPos = Utils.positionExistsIn(state.warriorPositions, currPos);
					if (warriorPos != -1) {
						extraDamage += 2;
						newWarriorsPositions[warriorPos] = null;
					}
				}

				currPos[0] = newX;
				currPos[1] = (byte) (newY + 1);
				if (isValid(currPos, m, n)) {
					byte warriorPos = Utils.positionExistsIn(state.warriorPositions, currPos);
					if (warriorPos != -1) {
						extraDamage += 2;
						newWarriorsPositions[warriorPos] = null;
					}
				}
			} else if (action == "snap") {
				newIsThanosKilled = true;
			}
		}

		newWarriorsPositions = Utils.removeNulls(newWarriorsPositions);
		newStonePositions = Utils.removeNulls(newStonePositions);

		extraDamage += getTurnDamage(m, n, newX, newY, newWarriorsPositions, state.thanosPosition);
		return new State(m, n, newIronManPosition, state.thanosPosition, newStonePositions, newWarriorsPositions,
				(byte) (state.damage + extraDamage), newIsThanosKilled);
	}

	private byte getTurnDamage(byte m, byte n, byte xPos, byte yPos, byte[][] warriorPositions, byte[] thanosPosition) {
		// Update the damage
		// For each adjacent warrior, inflect 1 damage
		// For adjacent Thanos, inflect an extra 5 damage

		byte damage = 0;
		byte[] currPos = { 0, 0 };
		currPos[0] = (byte) (xPos - 1);
		currPos[1] = yPos;
		if (isValid(currPos, m, n)) {
			if (Utils.positionExistsIn(warriorPositions, currPos) != -1) {
				damage++;
			} else if (Utils.isSamePosition(thanosPosition, currPos)) {
				damage += 5;
			}
		}

		currPos[0] = (byte) (xPos + 1);
		currPos[1] = yPos;
		if (isValid(currPos, m, n)) {
			if (Utils.positionExistsIn(warriorPositions, currPos) != -1) {
				damage++;
			} else if (Utils.isSamePosition(thanosPosition, currPos)) {
				damage += 5;
			}
		}

		currPos[0] = xPos;
		currPos[1] = (byte) (yPos - 1);
		if (isValid(currPos, m, n)) {
			if (Utils.positionExistsIn(warriorPositions, currPos) != -1) {
				damage++;
			} else if (Utils.isSamePosition(thanosPosition, currPos)) {
				damage += 5;
			}
		}

		currPos[0] = xPos;
		currPos[1] = (byte) (yPos + 1);
		if (isValid(currPos, m, n)) {
			if (Utils.positionExistsIn(warriorPositions, currPos) != -1) {
				damage++;
			} else if (Utils.isSamePosition(thanosPosition, currPos)) {
				damage += 5;
			}
		}

		return damage;
	}

	public int getStepCost(Object start, String action, Object dest) {
		int startDamage = ((State) start).damage;
		int destDamage = ((State) dest).damage;
		return destDamage - startDamage;
	}

}
