package directorui;

import java.util.ArrayList;

import processing.core.*;

public class ContourTarget extends PApplet {
	private static final long serialVersionUID = 1L;
	
	public static final int TARGET_RADIUS = 4;
	
//	private final int x0;
//	private final int y0;
	private int x;
	private int y;
	
	public ContourTarget(int x0, int y0){
//		this.x0 = x0;
//		this.y0 = x0;
		this.x  = x0;
		this.y  = y0;
	}
	
	public boolean isOver(int px, int py) {
//		return dist(px, py, this.x, this.y) < TARGET_RADIUS;	// circle
		return abs(px-this.x) < TARGET_RADIUS && abs(py-this.y) < TARGET_RADIUS;
	}
	
	public void draw(){
		fill(255);
	}

	public void setX(int px) {
		this.x = px;
	}
	
	public void setY(int py) {
		this.y = py;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	

}
