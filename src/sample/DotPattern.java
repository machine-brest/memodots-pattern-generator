package sample;

import java.util.*;

public class DotPattern
{
	private int columns;
	private int rows;
	private int shapesPerPattern = 1;
	private int dotsPerPattern = 12;
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

	public void setAllowOpen(boolean isAllowOpen) { this.isAllowOpen = isAllowOpen;	}
	public void setAllowDiagonals(boolean isAllowDiagonals) { this.isAllowDiagonals = isAllowDiagonals;	}
	public void setShapesPerPattern(int shapesPerPattern) { this.shapesPerPattern = shapesPerPattern; }
	public void setDotsPerPattern(int dotsPerPattern) { this.dotsPerPattern = dotsPerPattern; }

	/**
	 * Genarates dot pattern of simple shapes
	 */
	public void generate() {

		shapes = new ArrayList<DotShape>(shapesPerPattern);
		System.out.println("Generating pattern " + columns + "x" + rows );

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

		Dot dot = freeDots.size() > 1
				? freeDots.get(new Random().nextInt(freeDots.size() - 1))
				: freeDots.get(0);

		System.out.printf("Building a shape. Start from %d,%d\n", dot.x, dot.y);

		if (generateShapeDots(shape, dot, new ArrayList<Dot>(freeDots))) {
			return true;
		}

		freeDots.remove(dot);
		return generateShape(freeDots, shape);
	}

	protected Boolean generateShapeDots(DotShape shape, Dot from, List<Dot> freeDots) {

		if (freeDots.isEmpty())
			return false;

		System.out.printf("added point %d,%d\n", from.x, from.y);

		shape.getDots().add(from);

		if (shape.getDots().size() < shape.getDotsMaxCount()) {

			// all connected dots

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

			// check dots

			for (Iterator<Dot> i = dots.iterator(); i.hasNext(); ) {
				Dot dot = i.next();

				if (freeDots.contains(dot) && shape.getDots().contains(dot)) {
					i.remove();
				}
			}

			if (dots.size() == 0)
				return false;

			Dot newDot = dots.size() > 1
					? dots.get(new Random().nextInt(dots.size() - 1))
					: dots.get(0);

			return generateShapeDots(shape, newDot, freeDots);
		}

		return true;
	}
}
