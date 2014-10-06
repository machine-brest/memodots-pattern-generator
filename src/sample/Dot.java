package sample;

public class Dot {

	public int x;
	public int y;

	public Dot() {
		this(0, 0);
	}

	public Dot(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object dot) {
		if (dot instanceof Dot) {
			return this.x == ((Dot) dot).x && this.y == ((Dot) dot).y;
		}

		return super.equals(dot);
	}
}
