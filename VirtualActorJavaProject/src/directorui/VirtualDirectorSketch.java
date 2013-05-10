package directorui;

import controlP5.*;
import processing.core.*;
import ssmlobjects.*;

public class VirtualDirectorSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	private VirtualDirector director;
	private ControlP5 cp5;
	
	private int sliderVolume	= ProsodyElem.DEFAULT_VOLUME;
	private int sliderRate		= ProsodyElem.DEFAULT_RATE;
	private int sliderPitch		= ProsodyElem.DEFAULT_PITCH;
	private int sliderRange		= ProsodyElem.DEFAULT_RANGE;
	
//	private TreeMap<Integer,Integer> countourTargets = new TreeMap<Integer,Integer>();
	private ContourArea contourArea;

  public VirtualDirectorSketch(int width, int height, VirtualDirector parent) {
	this.director = parent;
	this.width = width;
	this.height = height;
	}

public void setup() {
	  
	  // initiate
	  size(width,height);
	  cp5 = new ControlP5(this);
	  PFont font = createFont("Segoe UI", 18);
	  textFont(font);
	 	  
	  // contour
	  contourArea = new ContourArea(10, 370, 500, 300, createGraphics(500, 300));
	  
	  cp5.addToggle("toggleRemoveCP")
	     .setPosition(10,700)
	     .setSize(100,30)
	     .setValue(true)
	     .setMode(ControlP5.SWITCH)
	     .setCaptionLabel("Toggle Add/Remove mode")
	     ;
	  
	  // input text field
	  cp5.addTextfield("input")
	     .setPosition(10,60)
	     .setSize(500,40)
	     .setFont(font)
	     .setFocus(true)
	     .setColor(color(255,255,255))
	     ;
	  
	  cp5.addBang("play")
	     .setPosition(10,120)
	     .setSize(100,40)
	     .setCaptionLabel("Play Audio")
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  cp5.addBang("clearTxt")
	     .setPosition(410,120)
	     .setSize(100,40)
	     .setCaptionLabel("Clear Text")
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  cp5.addBang("clearSliders")
	     .setPosition(410,300)
	     .setSize(100,40)
	     .setCaptionLabel("Clear Sliders")
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  cp5.addBang("clearContour")
	     .setPosition(410,700)
	     .setSize(100,40)
	     .setCaptionLabel("Clear Contour Area")
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  // sliders
	  cp5.addSlider("sliderVolume")
	     .setPosition(10,180)
	     .setSize(30,150)
	     .setRange(0,100)
	     .setCaptionLabel("Volume")
	     ;

	  cp5.addSlider("sliderRate")
	     .setPosition(70,180)
	     .setSize(30,150)
	     .setRange(-100,100)
	     .setCaptionLabel("Rate")
	     ;

	  cp5.addSlider("sliderPitch")
	     .setPosition(130,180)
	     .setSize(30,150)
	     .setRange(-24,24)
	     .setCaptionLabel("Pitch")
	     ;

	  cp5.addSlider("sliderRange")
	     .setPosition(200,180)
	     .setSize(30,150)
	     .setRange(0,4)
	     .setNumberOfTickMarks(5)
	     .setCaptionLabel("Range")
	     ;
  }

  public void draw() {
	  background(0);
	  fill(255,255,255);
	  
	  contourArea.passMouse(mouseX, mouseY);
	  contourArea.draw();
	  image(contourArea.getPg(), contourArea.getX0(), contourArea.getY0());
	  
	  text("Virtual Actor: Director UI", 10,30);
	  
  }
  
  public void play() {
//	  System.out.println("input: " +textField);
//	  System.out.println("volume: "+sliderVolume);
//	  System.out.println("rate: "+sliderRate);
//	  System.out.println("pitch: "+sliderPitch);
//	  System.out.println("range: "+sliderRange);
	  
	  director.setText(cp5.get(Textfield.class,"input").getText());
	  director.setVolume(sliderVolume);
	  director.setRate(sliderRate);
	  director.setPitch(sliderPitch);
	  director.setRange(sliderRange);
	  director.setContour(contourArea.getContour());
	  director.playAudio();
  }

  public void clearTxt() {
	  cp5.get(Textfield.class,"input").clear();
  }
  
  public void clearSliders() {
	  cp5.getController("sliderVolume").setValue(ProsodyElem.DEFAULT_VOLUME);
	  cp5.getController("sliderRate").setValue(ProsodyElem.DEFAULT_RATE);
	  cp5.getController("sliderPitch").setValue(ProsodyElem.DEFAULT_PITCH);
	  cp5.getController("sliderRange").setValue(ProsodyElem.DEFAULT_RANGE);
  }
  
  public void clearContour() {
	  contourArea.reset();
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
  
  public void toggleRemoveCP(boolean theFlag) {
	  println("toggle event: " + theFlag);
	  contourArea.toggleAddRemoveMode(theFlag);
	}
}