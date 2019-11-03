import java.util.LinkedHashMap;
import java.util.Map;

class Utils {

	final static Map<String, byte[]> directions = new LinkedHashMap<String, byte[]>();
	
	static {
		byte[] up = { -1, 0 };
		directions.put("up", up);

		byte[] down = { 1, 0 };
		directions.put("down", down);

		byte[] right = { 0, 1 };
		directions.put("right", right);

		byte[] left = { 0, -1 };
		directions.put("left", left);
	}

	public static boolean isValid(byte[] pos, byte m, byte n) {
		// m >> rows (height); n >> cols (width)
		return (pos[0] >= 0 && pos[0] < m && pos[1] >= 0 && pos[1] < n);
	}

	public static boolean isSamePosition(byte[] pos1, byte[] pos2) {
		// Takes 2 arrays of size 2 each (think tuples x,y)
		// and returns whether they are the same.
		return pos1[0] == pos2[0] && pos1[1] == pos2[1];
	}

	public static byte positionExistsIn(byte[][] positions, byte[] target) {
		// Given an array of positions (thus 2d), it checks if
		// the target position exists in the collection.
		for (byte i = 0; i < positions.length; i++) {
			if (isSamePosition(positions[i], target)) {
				return i;
			}
		}
		return -1;
	}

	public static byte[][] removeNulls(byte[][] nullsArray) {
		// Given a 2d array, remove any cells containing nulls, and
		// return an array with the null-holes patched.

		byte nullsCount = 0;
		for (byte[] b : nullsArray) {
			if (b == null)
				nullsCount++;
		}

		byte[][] nullsRemoved = new byte[nullsArray.length - nullsCount][2];
		for (byte i = 0, j = 0; i < nullsArray.length; i++) {
			if (nullsArray[i] != null) {
				nullsRemoved[j] = nullsArray[i];
				j++;
			}
		}

		return nullsRemoved;
	}
}
