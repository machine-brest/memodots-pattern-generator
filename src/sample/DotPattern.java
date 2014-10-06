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
			shape.setMaxDotCount(dots);

			if (generateShape(new ArrayList<Dot>(freeDots), shape)) {
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

		System.out.printf("Building a shape. Start from %d,%d\n", from.x, from.y);

		if (generateShapeDots(shape, from, freeDots)) {
			return true;
		}

		shape.getDots().clear();
		freeDots.remove(from);

		return generateShape(freeDots, shape);
	}

	protected Boolean generateShapeDots(DotShape shape, Dot from, List<Dot> freeDots) {

		System.out.printf("trying dot: %d,%d\n", from.x, from.y);

		if (freeDots.isEmpty()) { // no more free dots
			System.out.printf("no more free dots\n");
			return false;
		}

		if (shape.getDots().contains(from)) {
			return false;
		}

		// select next possible dot

		List<Dot> dots = new ArrayList<Dot>();

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

		while (dots.size() > 0) {
			Dot next = dots.size() > 1 ? dots.get(new Random().nextInt(dots.size() - 1)) : dots.get(0);
			shape.getDots().add(from);

			if (generateShapeDots(shape, next, freeDots)) {
				if (shape.getDots().size() == shape.getDotsMaxCount()) {
					System.out.printf("shape has already enough dots\n");
					return true;
				}
			}
			else {
				dots.remove(next);
				shape.getDots().remove(from);
				return false;
			}
		}

		return false;
	}
}
