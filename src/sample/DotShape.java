package sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DotShape
{
	private Boolean closed = false;
	private List<DotPoint> dots = new ArrayList<DotPoint>();

	public DotShape() {}

	public List<DotPoint> getDots() {
		return dots;
	}

	public Boolean isClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
}
