package sample;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class DotShape
{
	private List<Dot> dots = new LinkedList<Dot>();
	private Boolean isClosed = false;

	/**
	 * Maximum size of this shape
	 */
	private int maxSize;

	public DotShape() {}

	public List<Dot> getDots() {
		return dots;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public Dot get(int index) {
		return dots.get(index);
	}

	public Dot getFirst() {
		return dots.get(0);
	}

	public Dot getLast() {
		return dots.get(dots.size() - 1);
	}

	public void add(Dot dot) {
		dots.add(dot);
		close(false);
	}

	public void remove(Dot dot) {
		dots.remove(dot);
		close(false);
	}

	public void remove(int index) {
		dots.remove(index);
		close(false);
	}

	public Boolean contains(Dot dot) {
		return dots.contains(dot);
	}

	public Boolean contains(int x, int y) {
		return dots.contains(new Dot(x, y));
	}

	public int size() {
		return dots.size();
	}

	public void close(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public void clear() {
		dots.clear();
		close(false);
	}

	/**
	 * Returns true if shape's first point has the same
	 * coordinates as the last one.
	 *
	 * @return true is shape is closed
	 */
	public Boolean isClosed() {
		return isClosed;
	}

	@Override
	public boolean equals(Object shape) {

		if (shape instanceof DotShape) {

			if (((DotShape) shape).size() != size())
				return false;

			for (int i = 0, l = size(); i < l; ++i)
				if (get(i) == ((DotShape) shape).get(i))
					return false;

			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ");
		for (Dot dot: dots) joiner.add(dot.toString());
		return "[ " + joiner + " ]" + (isClosed ? " closed," : " open,") + " size: " + size();
	}
}
