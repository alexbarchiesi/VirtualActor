package testpackage;

import processing.core.*;

import directorui.ContourArea;

public class ContourTest extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private ContourArea contourArea;

public void setup() {
	  size(600,600);
	  background(0);
	  
	  contourArea = new ContourArea(100, 100, 400, 100, createGraphics(400, 100));
	  
  }

  public void draw() {
	  contourArea.passMouse(mouseX, mouseY);
	  
	  contourArea.draw();
	  
	  image(contourArea.getPg(), contourArea.getX0(), contourArea.getY0());
  }
  
  public void mousePressed(){
	  System.out.println(mouseX);
	  System.out.println(contourArea.isOver());
	  
	  if(contourArea.isOver()){
		  contourArea.mousePressed();
	  }
  }
  
  public void mouseDragged(){
	  if(contourArea.isOver()){
		  contourArea.mouseDragged();
	  }
  }
  
  public void mouseReleased(){
	  contourArea.mouseReleased();
  }
}