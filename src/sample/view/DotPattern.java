package sample.view;

import sample.DotShape;

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

		int minDPS = (!isAllowDiagonals() && !isAllowOpen() ? 4 : (isAllowOpen() ? 2 : 3));

		System.out.println("Dots per shape min: " + minDPS);

		for (int dotsRemains = getDotsPerPattern(); dotsRemains > 0; ) {
			int shapesRemains = getShapesPerPattern() - shapes.size();
			int dots = (shapesRemains == 1) ? dotsRemains : (dotsRemains - shapesRemains * minDPS) / shapesRemains;

			if (shapesRemains > 1) {
				dots = minDPS + (int) Math.round(Math.random() * dots);
			}

			if (dots % 2 != 0 && (!isAllowDiagonals() && !isAllowOpen()))
				dots = Math.min(minDPS, dots + 1);

			dotsRemains -= dots;
			shapes.add(generateShape(dots));

			System.out.println("Generating shape using " + dots + " dots. Remains " + dotsRemains);
		}
	}

	private DotShape generateShape(int maxDots) {

		// 1. find free rectangular block
		// 2. randomize coords
		// 3. generate shape

		return new DotShape();
	}
}
