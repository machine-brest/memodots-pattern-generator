package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DotPattern
{
	List<DotShape> shapes;

	private int shapesPerPattern = 1;
	private int dotsPerPattern = 12;
	private boolean isAllowOpen = false;
	private boolean isAllowDiagonals = false;

	public DotPattern(int columns, int rows) {

		if (columns < 1 && rows < 1)
			throw new IllegalArgumentException("Invalid grid size");

		shapes = new ArrayList<DotShape>();
	}

	public boolean isAllowOpen() {
		return isAllowOpen;
	}

	public void setAllowOpen(boolean isAllowOpen) {
		this.isAllowOpen = isAllowOpen;
	}

	public boolean isAllowDiagonals() {
		return isAllowDiagonals;
	}

	public void setAllowDiagonals(boolean isAllowDiagonals) {
		this.isAllowDiagonals = isAllowDiagonals;
	}

	public int getShapesPerPattern() {
		return shapesPerPattern;
	}

	public void setShapesPerPattern(int shapesPerPattern) {
		this.shapesPerPattern = shapesPerPattern;
	}

	public int getDotsPerPattern() {
		return dotsPerPattern;
	}

	public void setDotsPerPattern(int dotsPerPattern) {
		this.dotsPerPattern = dotsPerPattern;
	}

	public void generate() {

		shapes.clear();

		System.out.println("Generating pattern");

		int dpsMin = (!isAllowDiagonals() && !isAllowOpen() ? 4 : (isAllowOpen() ? 2 : 3));
		for ( int sr = getShapesPerPattern(), dr = getDotsPerPattern(); sr > 0 && dr > 0; --sr) {
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
	private DotShape generateShape(int dps) {

		// 1. find free rectangular block
		// 2. randomize coords
		// 3. generate shape

		// System.out.println("creating shape_#" + shapes.size() + ". dots " + dots + ". " + dr + " dots remains");

		return new DotShape();
	}
}
