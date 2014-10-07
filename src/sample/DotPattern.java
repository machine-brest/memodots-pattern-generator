package sample;

import java.util.*;

public class DotPattern
{
	private int columns;
	private int rows;
	private int shapesPerPattern;
	private int dotsPerPattern;
	private int colorsPerPattern;

	private boolean isAllowOpen = false;
	private boolean isAllowDiagonals = false;

	private List<DotShape> shapes;

	public DotPattern(int columns, int rows) {

		if (columns < 1 && rows < 1)
			throw new IllegalArgumentException("Invalid grid size");

		this.columns = columns;
		this.rows    = rows;

		shapes = new ArrayList<DotShape>();
	}

	public List<DotShape> getShapes() {
		return shapes;
	}

	public boolean isAllowDiagonals() { return isAllowDiagonals; }
	public boolean isAllowOpen() { return isAllowOpen; }

	public int getColorsPerPattern() { return colorsPerPattern; }

	public void setAllowOpen(boolean isAllowOpen) { this.isAllowOpen = isAllowOpen;	}
	public void setAllowDiagonals(boolean isAllowDiagonals) { this.isAllowDiagonals = isAllowDiagonals;	}
	public void setShapesPerPattern(int shapesPerPattern) { this.shapesPerPattern = shapesPerPattern; }
	public void setDotsPerPattern(int dotsPerPattern) { this.dotsPerPattern = dotsPerPattern; }
	public void setColorsPerPattern(int colorsPerPattern) { this.colorsPerPattern = colorsPerPattern; }

	/**
	 * Genarates dot pattern of simple shapes
	 */
	public void generate() {

		shapes = new ArrayList<DotShape>(shapesPerPattern);
		System.out.println("Generating pattern " + columns + "x" + rows );

		// initially set all dots as free

		List<Dot> freeDots = new ArrayList<Dot>();
		for (int dotIndex = 0, l = columns * rows; dotIndex < l; ++ dotIndex) {
			Dot dot = new Dot();
			dot.x = dotIndex % columns;
			dot.y = (dotIndex - dot.x) / columns;
			freeDots.add(dot);
		}

		int dpsMin = (!isAllowDiagonals() && !isAllowOpen() ? 4 : (isAllowOpen() ? 2 : 3));
		for ( int sr = shapesPerPattern, dr = dotsPerPattern; sr > 0 && dr > 0 && !freeDots.isEmpty(); --sr) {
			int dots;
			if (sr == 1) { // last
				dots = dr;
			}
			else {
				double pcut = 100 - ((sr - 1) * dpsMin / (dr / 100.0));
				dots = new Random().nextInt((int) Math.round(dr * pcut / 100));
				if (dots % 2 != 0 && (!isAllowDiagonals() && !isAllowOpen()))
					dots -= 1;
			}

			dr -= dots = Math.max(dpsMin, dots);

			DotShape shape = new DotShape();
			shape.setMaxSize(dots);

			System.out.printf("Building a shape of %d dots\n", shape.getMaxSize());

			if (generateShape(new ArrayList<Dot>(freeDots), shape)) {
				System.out.printf("Shape created: %s\n", shape);
				freeDots.removeAll(shape.getDots());
				shapes.add(shape);
			}
			else {
				System.out.printf("Could not create shape. No more free dots");
				break;
			}
		}
	}

	/**
	 * Generates dot shape using given number of dots.
	 * Shape can be closed/open and have or not diagonal lines.
	 *
	 * @param freeDots Free dots to use
	 * @return Shape
	 */
	protected Boolean generateShape(List<Dot> freeDots, DotShape shape) {

		if (freeDots.isEmpty())
			return false;

		Dot from = freeDots.size() > 1
				? freeDots.get(new Random().nextInt(freeDots.size() - 1))
				: freeDots.get(0);

		if (generateShapeDots(shape, from, freeDots)) {
			return true;
		}

		shape.getDots().clear();
		freeDots.remove(from);

		return generateShape(freeDots, shape);
	}

	protected Boolean generateShapeDots(DotShape shape, Dot from, List<Dot> freeDots) {

		if (shape.size() == shape.getMaxSize()) {

			if (!isAllowOpen()) {

				Dot first = shape.getFirst();
				Dot last  = shape.getLast();

				if (isAllowDiagonals()) {
					if (Math.abs(first.x - last.x) > 1 || Math.abs(first.y - last.y) > 1)
						return false;
				} else {
					if (Math.abs(first.x - last.x) + Math.abs(first.y - last.y) > 1)
						return false;
				}

				shape.close(true);
			}

			return true; // shape complete
		}

		if (freeDots.isEmpty()) { // no more free dots
			return false;
		}

		// select next possible dot

		List<Dot> dots = new ArrayList<Dot>();

		/*
		if (from.x + 1 < columns) dots.add(new Dot(from.x + 1, from.y));
		if (from.y + 1 < rows)    dots.add(new Dot(from.x, from.y + 1));
		if (from.x - 1 > -1)      dots.add(new Dot(from.x - 1, from.y));
		if (from.y - 1 > -1)      dots.add(new Dot(from.x, from.y - 1));

		if (isAllowDiagonals()) {
			if (from.x + 1 < columns && from.y + 1 < rows) dots.add(new Dot(from.x + 1, from.y + 1));
			if (from.x - 1 > -1 && from.y + 1 < rows)      dots.add(new Dot(from.x - 1, from.y + 1));
			if (from.x - 1 > -1 && from.y - 1 > -1)        dots.add(new Dot(from.x - 1, from.y - 1));
			if (from.x + 1 < columns && from.y - 1 > -1)   dots.add(new Dot(from.x + 1, from.y - 1));
		}
		*/

		dots.add(new Dot(from.x + 1, from.y));
		dots.add(new Dot(from.x, from.y + 1));
		dots.add(new Dot(from.x - 1, from.y));
		dots.add(new Dot(from.x, from.y - 1));

		if (isAllowDiagonals()) {
			dots.add(new Dot(from.x + 1, from.y + 1));
			dots.add(new Dot(from.x - 1, from.y + 1));
			dots.add(new Dot(from.x - 1, from.y - 1));
			dots.add(new Dot(from.x + 1, from.y - 1));
		}

		while (dots.size() > 0) {

			Dot next = dots.size() > 1 ? dots.get(new Random().nextInt(dots.size() - 1)) : dots.get(0);

			if (next.x < 0 || next.x >= columns || next.y < 0 || next.y >= rows ) {
				dots.remove(next);
				continue;
			}

			if (shape.contains(next)) {
				dots.remove(next);
				continue;
			}

			if (!freeDots.contains(next)) {
				dots.remove(next);
				continue;
			}

			// denying crossing lines

			if (isAllowDiagonals()) {

				Dot dot1 = null, dot2 = null;

				if (next.x - from.x == 1 && next.y - from.y == 1) {
					dot1 = new Dot(from.x + 1, from.y);
					dot2 = new Dot(from.x, from.y + 1);
				} else if (next.x - from.x == -1 && next.y - from.y == 1) {
					dot1 = new Dot(from.x, from.y + 1);
					dot2 = new Dot(from.x - 1, from.y);
				} else if (next.x - from.x == -1 && next.y - from.y == -1) {
					dot1 = new Dot(from.x - 1, from.y);
					dot2 = new Dot(from.x, from.y - 1);
				} else if (next.x - from.x == 1 && next.y - from.y == -1) {
					dot1 = new Dot(from.x, from.y - 1);
					dot2 = new Dot(from.x + 1, from.y);
				}

				DotShape shape1 = getShapeByDot(dot1);
				DotShape shape2 = getShapeByDot(dot2);

				if ((shape.contains(dot1) || shape.contains(dot2))
					|| (shape1 != null && shape2 != null && shape1 == shape2)) {
					dots.remove(next);
					continue;
				}
			}

			shape.add(from);

			if (! generateShapeDots(shape, next, freeDots)) {
				shape.remove(from);
				dots.remove(next);
				continue;
			}

			return true;
		}

		return false;
	}

	protected DotShape getShapeByDot(Dot dot) {

		if (shapes.size() > 0) {
			for (DotShape shape : shapes )
				if (shape.contains(dot))
					return shape;
		}

		return null;
	}
}
