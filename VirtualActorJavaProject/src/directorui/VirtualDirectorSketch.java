package directorui;

import controlP5.*;
import processing.core.*;
import ssmlobjects.ProsodyElem;

import java.util.ArrayList;
import java.util.TreeMap;

public class VirtualDirectorSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	private VirtualDirector director;
	private ControlP5 cp5;
	private String textField = "";
	
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
	  PFont font = createFont("segoe",12);
	  cp5 = new ControlP5(this);
	  
	  // contour
	  contourArea = new ContourArea(10, 370, 500, 300, createGraphics(500, 300));
	  
	  cp5.addToggle("toggleRemoveCP")
	     .setPosition(450,700)
	     .setSize(60,20)
	     .setValue(true)
	     .setMode(ControlP5.SWITCH)
	     .setCaptionLabel("Add Remove")
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
	     .setSize(80,40)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  cp5.addBang("clear")
	     .setPosition(120,120)
	     .setSize(80,40)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  // sliders
	  cp5.addSlider("sliderVolume")
	     .setPosition(10,180)
	     .setSize(30,150)
	     .setRange(0,100)
//	     .setRange(0,5)
//	     .setNumberOfTickMarks(6)
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
	  
	  textFont(font);
  }

  public void draw() {
	  background(0);
	  fill(255,255,255);
	  
	  contourArea.passMouse(mouseX, mouseY);
	  contourArea.draw();
	  image(contourArea.getPg(), contourArea.getX0(), contourArea.getY0());
	  
	  textField = cp5.get(Textfield.class,"input").getText();
	  text(textField, 10,30);
	  
  }
  
  public void play() {
//	  System.out.println("input: " +textField);
//	  System.out.println("volume: "+sliderVolume);
//	  System.out.println("rate: "+sliderRate);
//	  System.out.println("pitch: "+sliderPitch);
//	  System.out.println("range: "+sliderRange);
	  
	  director.setText(textField);
	  director.setVolume(sliderVolume);
	  director.setRate(sliderRate);
	  director.setPitch(sliderPitch);
	  director.setRange(sliderRange);
	  director.setContour(contourArea.getContour());
	  director.playAudio();
  }

  public void clear() {
	  contourArea.reset();
	  
	  cp5.get(Textfield.class,"input").clear();
	  cp5.getController("sliderVolume").setValue(ProsodyElem.DEFAULT_VOLUME);
	  cp5.getController("sliderRate").setValue(ProsodyElem.DEFAULT_RATE);
	  cp5.getController("sliderPitch").setValue(ProsodyElem.DEFAULT_PITCH);
	  cp5.getController("sliderRange").setValue(ProsodyElem.DEFAULT_RANGE);
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