package directorui;

import maryttsutils.MaryTTSWrapper;
import controlP5.*;
import processing.core.*;
import ssmlobjects.*;

public class VirtualDirectorSketch extends PApplet {
	ControlP5 cp5;
	String input = "";
	private VirtualDirector director;

  public VirtualDirectorSketch(VirtualDirector parent) {
	this.director = parent;
	}

public void setup() {
	  size(600,600);
	  
	  PFont font = createFont("segoe",20);
	  cp5 = new ControlP5(this);
	  
	  cp5.addTextfield("input")
	     .setPosition(20,100)
	     .setSize(400,40)
	     .setFont(font)
	     .setFocus(true)
	     .setColor(color(255,0,0))
	     ;
	  
	  cp5.addBang("play")
	     .setPosition(20,170)
	     .setSize(80,40)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  cp5.addBang("clear")
	     .setPosition(120,170)
	     .setSize(80,40)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;
	  
	  textFont(font);
  }

  public void draw() {
	  background(0);
	  fill(255);
	  //text(cp5.get(Textfield.class,"input").getText(), 360,130);
	  text(input, 360,180);
  }
  
  public void play() {
	  String content = cp5.get(Textfield.class,"input").getText();
	  System.out.println(content);
	  
	  director.setText(content);
	  director.playAudio();
  }

  public void clear() {
	  cp5.get(Textfield.class,"input").clear();
  }
}