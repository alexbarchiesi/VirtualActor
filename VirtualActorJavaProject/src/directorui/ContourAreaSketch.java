package directorui;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import processing.core.*;

/**
 * Processing sketch, graphical representation of pitch contour.
 *
 */
public class ContourAreaSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	public static final int[] BG_COLOR 		= {   2,  52,  77, 255};
	public static final int[] STROKE_COLOR 	= {   0, 180, 234, 255};
	public static final int[] CP_COLOR 		= { 255, 255, 255, 255};

	private final int x0, y0;	// top left
	private final int w,  h;	// size of box

	private PGraphics pg;

	// targets
	private ArrayList<ContourTarget> targets;
	private ContourTarget selected;
	private boolean locked, addRemove, displayCoords;

	public ContourAreaSketch(int x0, int y0, int width, int height, PGraphics pg) {
		this.x0 = x0;
		this.y0 = y0;
		this.w  = width;
		this.h  = height;
		this.pg = pg;

		reset();
	}

	public void reset() {
		targets   = new ArrayList<ContourTarget>();
		selected  = null;
		locked    = false;
		addRemove = true;
	}

	public void draw() {
		pg.beginDraw();
		pg.background(BG_COLOR[0], BG_COLOR[1], BG_COLOR[2], BG_COLOR[3]);

		// draw grid
		pg.stroke(STROKE_COLOR[0], STROKE_COLOR[1], STROKE_COLOR[2], STROKE_COLOR[3] * 0.1f);

		// vertical lines every 10%
		for(int v=10; v < 100; v+=10) {
			pg.line(map(v, 0, 100, 0, w), 0, map(v, 0, 100, 0, w), h);
		}

		// horizontal lines every 25%
		for(int v=75; v > -100; v-=25) {
			pg.line(0, map(v, 100, -100, 0, h), w, map(v, 100, -100, 0, h));
		}

		// base line
		pg.stroke(STROKE_COLOR[0], STROKE_COLOR[1], STROKE_COLOR[2], STROKE_COLOR[3] * 0.25f);
		pg.line(0, 0.5f*h, w, 0.5f*h);

		if(!targets.isEmpty()) {
			// convert control points to contour values
			TreeMap<Integer, Integer> plot = new TreeMap<Integer, Integer>();

			for(ContourTarget ct : targets) {
				plot.put(ct.getX(), ct.getY());
			}

			if(plot.firstEntry().getKey() != 0) {
				plot.put(0, plot.firstEntry().getValue());
			}

			if(plot.lastEntry().getKey() != this.w) {
				plot.put(this.w, plot.lastEntry().getValue());
			}

			// draw contour plot
			pg.stroke(STROKE_COLOR[0], STROKE_COLOR[1], STROKE_COLOR[2], STROKE_COLOR[3]);
			pg.noFill();
			pg.beginShape();
			int x, y;
			for(Map.Entry<Integer, Integer> entry : plot.entrySet()) {
				x = entry.getKey();
				y = entry.getValue();
				pg.vertex(x,y);
			}
			pg.endShape();

			// draw control points
			pg.noStroke();
			pg.fill(CP_COLOR[0], CP_COLOR[1], CP_COLOR[2], CP_COLOR[3]);
			for(Map.Entry<Integer, Integer> entry : plot.entrySet()) {
				x = entry.getKey();
				y = entry.getValue();
				pg.rectMode(RADIUS);
				pg.rect(x, y, ContourTarget.TARGET_RADIUS, ContourTarget.TARGET_RADIUS);
			}

			// display coordinates of control points
			if(displayCoords) {
				for(Map.Entry<Integer, Integer> entry : plot.entrySet()) {
					displayCoords(entry.getKey(),entry.getValue());
				}
			}
		}

		// display cursor coordinates
		if(isOver()) {
			displayCoords(mouseX, mouseY);
		}

		pg.endDraw();
	}

	private void displayCoords(int px, int py) {
		pg.fill(STROKE_COLOR[0], STROKE_COLOR[1], STROKE_COLOR[2], STROKE_COLOR[3]);

		String txt = round(map(px, 0, w, 0, 100))+"%,"+round(map(py, 0, h, 100, -100))+"%";
		pg.text(txt, px>w-5-pg.textWidth(txt)? w-pg.textWidth(txt) : px+5, py<10? 10 : py);

	}

	public void mousePressed() {		
		// if not empty, find selected
		if(!targets.isEmpty()) {
			for(ContourTarget ct : targets) {
				if(ct.isOver(mouseX, mouseY)) {
					if(!addRemove) {
						targets.remove(ct);
						return;
					}
					selected = ct;
					locked = true;
					return;
				}
			}
		}

		if(addRemove) {
			locked = true;
			selected = new ContourTarget(mouseX, mouseY);
			System.out.println("selected x: " + selected.getX());
			targets.add(selected);
		}
	}

	public void mouseDragged() {
		if(locked) {
			// move control point with cursor
			selected.setX(mouseX);
			selected.setY(mouseY);
		}
	}

	public void mouseReleased() {
		removeRedundant();
		locked = false;
	}

	private void removeRedundant() {
		// prune overlapping control points
		if(!targets.isEmpty()) {
			ArrayList<Integer> xValues = new ArrayList<Integer>();
			ArrayList<ContourTarget> toRemove = new ArrayList<ContourTarget>();
			for(ContourTarget ct : targets) {
				if(xValues.contains(ct.getX())) {
					toRemove.add(ct);
				} else {
					xValues.add(ct.getX());
				}
			}
			targets.removeAll(toRemove);
		}
	}

	public PGraphics getPg() {
		return pg;
	}

	public float getX0() {
		return x0;
	}

	public float getY0() {
		return y0;
	}

	public boolean isOver() {
		return (mouseX > 0 && mouseX < w && mouseY > 0 && mouseY <h);
	}

	public void toggleAddRemoveMode(boolean state) {
		addRemove = state;
	}

	public void toggleDisplayCoords(boolean state) {
		displayCoords = state;
	}

	public void setContour(TreeMap<Integer, Integer> ctr) {
		this.targets.clear();

		int x, y;
		for(Map.Entry<Integer, Integer> entry : ctr.entrySet()) {
			x = (int) map(entry.getKey(),	   0, 100,		0, this.w);
			y = (int) map(entry.getValue(), -100, 100, this.h,		0);

			this.targets.add(new ContourTarget(x, y));
		}
	}
	public TreeMap<Integer, Integer> getContour() {
		// convert control points to contour values for output
		if(targets.isEmpty()) return null;
		TreeMap<Integer, Integer> contour = new TreeMap<Integer, Integer>();
		int x,y;

		for(ContourTarget ct : targets) {
			x = (int) map(ct.getX(), 0, this.w,   0,  100);
			y = (int) map(ct.getY(), 0, this.h, 100, -100);
			contour.put(x,y);
		}

		if(contour.firstEntry().getKey() != 0) {
			contour.put(0, contour.firstEntry().getValue());
		}

		if(contour.lastEntry().getKey() != 100) {
			contour.put(100, contour.lastEntry().getValue());
		}

		return contour;
	}

	public void passMouse(int mX, int mY) {
		// keep local mouse position
		this.mouseX = mX - x0;
		this.mouseY = mY - y0;
	}
}
