package directorui;

import controlP5.*;
import processing.core.*;
import ssmlobjects.ProsodyElem;

public class VirtualDirectorSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	private VirtualDirector director;
	private ControlP5 cp5;
	private String textField = "";
	
	private int sliderVolume	= ProsodyElem.DEFAULT_VOLUME;
	private int sliderRate		= ProsodyElem.DEFAULT_RATE;
	private int sliderPitch		= ProsodyElem.DEFAULT_PITCH;
	private int sliderRange		= ProsodyElem.DEFAULT_RANGE;

  public VirtualDirectorSketch(VirtualDirector parent) {
	this.director = parent;
	}

public void setup() {
	  size(600,600);
	  
	  // initiate
	  PFont font = createFont("segoe",20);
	  cp5 = new ControlP5(this);
	  
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
	  textField = cp5.get(Textfield.class,"input").getText();
	  
	  background(0);
	  fill(255,255,255);
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
	  director.playAudio();
  }

  public void clear() {
	cp5.get(Textfield.class,"input").clear();
	cp5.getController("sliderVolume").setValue(ProsodyElem.DEFAULT_VOLUME);
	cp5.getController("sliderRate").setValue(ProsodyElem.DEFAULT_RATE);
	cp5.getController("sliderPitch").setValue(ProsodyElem.DEFAULT_PITCH);
	cp5.getController("sliderRange").setValue(ProsodyElem.DEFAULT_RANGE);
  }
}