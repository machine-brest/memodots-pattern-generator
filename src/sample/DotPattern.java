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

	private byte[][] grid;
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

		grid   = new byte[columns][rows];
		shapes = new ArrayList<DotShape>(shapesPerPattern);

		System.out.println("Generating pattern " + columns + "x" + rows );

		int dpsMin = (!isAllowDiagonals() && !isAllowOpen() ? 4 : (isAllowOpen() ? 2 : 3));
		for ( int sr = shapesPerPattern, dr = dotsPerPattern; sr > 0 && dr > 0; --sr) {
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
			shapes.add(generateShape(dots));
		}
	}

	/**
	 * Generates dot shape using given number of dots.
	 * Shape can be closed/open and have or not diagonal lines.
	 *
	 * @param dps Dots per shape. Must be positive
	 * @return Shape
	 */
	protected DotShape generateShape(int dps) {

		System.out.printf("Creating shape from %d dots\n----------------------\n", dps);

		DotPoint dot = new DotPoint();
		int dotIndex = new Random().nextInt(columns * rows);
		dot.x = dotIndex % columns;
		dot.y = (dotIndex - dot.x) / columns;

		System.out.printf("Initial location %d,%d\n", dot.x, dot.y);

		DotShape shape = new DotShape();

		for (int i = 0; i < dps; ++i) {

			DotPoint newDot = generateShapeDot(shape, dot);

			if (newDot != null) {
				System.out.printf("   added point %d,%d\n", newDot.x, newDot.y);
				shape.getDots().add(newDot);
				grid[newDot.x][newDot.y] = 1;
				dot = newDot;
			}
		}

		if (!isAllowOpen()) {
			shape.setClosed(true);
		}

		return shape;
	}

	protected DotPoint generateShapeDot(DotShape shape, DotPoint from) {

		int dotx = from.x;
		int doty = from.y;

		List<DotPoint> dots = new ArrayList<DotPoint>();

		if (dotx + 1 < columns) dots.add(new DotPoint(dotx + 1, doty));
		if (doty + 1 < rows)    dots.add(new DotPoint(dotx, doty + 1));
		if (dotx - 1 > -1)      dots.add(new DotPoint(dotx - 1, doty));
		if (doty - 1 > -1)      dots.add(new DotPoint(dotx, doty - 1));

		if (isAllowDiagonals()) {
			if (dotx + 1 < columns && doty + 1 < rows) dots.add(new DotPoint(dotx + 1, doty + 1));
			if (dotx - 1 > -1 && doty + 1 < rows)      dots.add(new DotPoint(dotx - 1, doty + 1));
			if (dotx - 1 > -1 && doty - 1 > -1)        dots.add(new DotPoint(dotx - 1, doty - 1));
			if (dotx + 1 < columns && doty - 1 > -1)   dots.add(new DotPoint(dotx + 1, doty - 1));
		}

		// check dots

		for (Iterator<DotPoint> i = dots.iterator(); i.hasNext(); ) {
			DotPoint dot = i.next();
			if (grid[dot.x][dot.y] == 1 || shape.getDots().contains(dot)) {
				i.remove();
			}

			// check if shape can be closed on this point

			if (!isAllowOpen()) {
				if (shape.getDots().size() > 0) {
					DotPoint firstDot = shape.getDots().get(0);
				}
			}
		}

		if (dots.size() == 0)
			return null;

		DotPoint dot = dots.get(new Random().nextInt(dots.size()));

		return new DotPoint(dot.x, dot.y);
	}
}
