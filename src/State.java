public class State {
	public byte damage;
	public byte m, n;
	public byte[] ironManPosition, thanosPosition;
	public byte[][] stonePositions, warriorPositions;
	public boolean isThanosKilled;

	public State(byte m, byte n, byte[] ironManPosition, byte[] thanosPosition, byte[][] stonePositions,
			byte[][] warriorPositions, byte damage) {
		this.m = m;
		this.n = n;
		this.ironManPosition = ironManPosition;
		this.thanosPosition = thanosPosition;
		this.stonePositions = stonePositions;
		this.warriorPositions = warriorPositions;
		this.damage = damage;
		this.isThanosKilled = false;
	}

	public State(byte m, byte n, byte[] ironManPosition, byte[] thanosPosition, byte[][] stonePositions,
			byte[][] warriorPositions, byte damage, boolean isThanosKilled) {
		this(m, n, ironManPosition, thanosPosition, stonePositions, warriorPositions, damage);
		this.isThanosKilled = isThanosKilled;
	}

	public String serialize() {
		// Use this to serialize the state (for handling repeated states)
		// Adding extra empty strings, or else the numbers will be summed.
		String s = this.ironManPosition[0] + "" + this.ironManPosition[1];
		s += this.thanosPosition[0] + "" + this.thanosPosition[1];

		for (byte[] b : this.stonePositions) {
			s += b[0] + ";" + b[1];
		}
		for (byte[] b : this.warriorPositions) {
			s += b[0] + ";" + b[1];
		}
		return s + (isThanosKilled ? 't' : 'f');
	}

}
