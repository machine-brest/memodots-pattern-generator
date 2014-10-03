package sample;

import java.util.LinkedList;
import java.util.List;

public class DotShape
{
	/**
	 * Contains shape vertices
	 */
	private List<Dot> dots = new LinkedList<Dot>();

	public DotShape() {}

	public List<Dot> getDots() {
		return dots;
	}

	public class Dot {

		public int x;
		public int y;

		private Dot(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
