package directorui;

/**
 * Object representation of a Pitch Contour control point 
 *
 */
public class ContourTarget {

	public static final int TARGET_RADIUS = 8;
	public static final int RADIUS_DELTA = 4;
	
	private int x;
	private int y;

	public ContourTarget(int x0, int y0) {
		this.x  = x0;
		this.y  = y0;
	}

	public boolean isOver(int px, int py) {
		return Math.abs(px-this.x) < TARGET_RADIUS + RADIUS_DELTA && Math.abs(py-this.y) < TARGET_RADIUS + RADIUS_DELTA;
	}

	public void setX(int px) {
		this.x = px;
	}

	public void setY(int py) {
		this.y = py;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
