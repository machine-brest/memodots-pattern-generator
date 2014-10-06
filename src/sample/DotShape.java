package sample;

import java.util.LinkedList;
import java.util.List;

public class DotShape
{
	private List<Dot> dots = new LinkedList<Dot>();

	private int dotMaxCount;

	public DotShape() {}

	public List<Dot> getDots() {
		return dots;
	}

	public Boolean isClosed() {
		// todo: check
		return true;
	}

	public int getDotsMaxCount() {
		return dotMaxCount;
	}

	public void setMaxDotCount(int dotMaxCount) {
		this.dotMaxCount = dotMaxCount;
	}
}
