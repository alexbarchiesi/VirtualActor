package directorui;

import java.util.Map;

import controlP5.*;
import processing.core.*;
import ssmlobjects.*;

/**
 * Processing sketch, GUI of application.
 *
 */
public class VirtualDirectorSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	private VirtualDirector director;
	private ControlP5 cp5;

	private int sliderVolume	= ProsodyElement.DEFAULT_VOLUME;
	private int sliderRate		= ProsodyElement.DEFAULT_RATE;
	private int sliderPitch		= ProsodyElement.DEFAULT_PITCH;
	private int sliderRange		= ProsodyElement.DEFAULT_RANGE;

	private ContourAreaSketch contourArea;
	private WaveFormAreaSketch waveFormArea;
	private int w, h;

	private RadioButton prosodyBlocksList;
	private int currentBlockIndex;

	private Textfield inputText;
	private String originalText;

	public VirtualDirectorSketch(VirtualDirector parent, int width, int height) {
		this.director = parent;
		this.w = width;
		this.h = height;
	}

	public void setup() {

		// initiate
		size(w,h);
		cp5 = new ControlP5(this);
		PFont font = createFont("Segoe UI", 18);
		textFont(font);

		// contour & waveform
		contourArea = new ContourAreaSketch(10, 370, 500, 200, createGraphics(500, 200));
		waveFormArea = new WaveFormAreaSketch(10, 670, 500, 100, createGraphics(500, 100));

		cp5.addToggle("toggleRemoveCP")
			.setPosition(10,600)
			.setSize(100,30)
			.setValue(true)
			.setMode(ControlP5.SWITCH)
			.setCaptionLabel("Mode: Add/Remove")
		;
		cp5.addToggle("toggleCoords")
			.setPosition(130,600)
			.setSize(100,30)
			.setValue(false)
			.setMode(ControlP5.SWITCH)
			.setCaptionLabel("Display Coordinates")
		;

		cp5.addButton("clearContour")
			.setPosition(410,600)
			.setSize(100,40)
			.setCaptionLabel("Clear Contour Area")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
		;

		// input text field & play
		inputText = cp5.addTextfield("input")
			.setPosition(10,60)
			.setSize(500,40)
			.setFont(font)
			.setFocus(true)
			.setColor(color(255,255,255))
			;

		cp5.addButton("apply")
			.setPosition(10,120)
			.setSize(100,40)
			.setCaptionLabel("Apply Values")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;

		cp5.addButton("playBlock")
			.setPosition(130,120)
			.setSize(100,40)
			.setCaptionLabel("Play Prosody Block")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;

		cp5.addButton("clearTxt")
			.setPosition(410,120)
			.setSize(100,40)
			.setCaptionLabel("Clear Text")
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

		cp5.addButton("clearSliders")
			.setPosition(410,300)
			.setSize(100,40)
			.setCaptionLabel("Clear Sliders")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;

		// load/save to file
		cp5.addButton("loadFile")
			.setPosition(530,60)
			.setSize(100,40)
			.setCaptionLabel("Load from File")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;     

		cp5.addButton("saveFile")
			.setPosition(650,60)
			.setSize(100,40)
			.setCaptionLabel("Save to File")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;

		cp5.addButton("playFile")
			.setPosition(770,60)
			.setSize(100,40)
			.setCaptionLabel("Play Whole File")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
			;

		// list of prosody blocks in file
		clearProsodyBlocksList();
		prosodyBlocksList.addItem("", 0); // first empty item
	}

	private void clearProsodyBlocksList() {
		prosodyBlocksList = cp5.addRadioButton("prosodyBlocksList");
		prosodyBlocksList.setPosition(530, 120)
			.setSize(20, 500)
			.setColorForeground(color(120))
			.setItemsPerRow(1)
			.setSpacingColumn(20)
			.setItemHeight(20)
			.setBarHeight(25)
			.setCaptionLabel("Prosody Blocks")
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
		;
	}

	public void draw() {
		background(0);

		text("Virtual Actor: Director UI", 10,30);

		contourArea.passMouse(mouseX, mouseY);
		contourArea.draw();
		image(contourArea.getPg(), contourArea.getX0(), contourArea.getY0());

		waveFormArea.draw();
		image(waveFormArea.getPg(), waveFormArea.getX0(), waveFormArea.getY0());

		if(director.isPlayAtNextDraw()) {
			director.playAudio();
		}

	}

	public void apply() {
		// apply current settings to block
		applyBlockSettings();
		// refresh the list for display
		loadProsodyBlocksList();
	}

	private void applyBlockSettings() {
		director.setText(inputText.getText());
		director.setVolume(sliderVolume);
		director.setRate(sliderRate);
		director.setPitch(sliderPitch);
		director.setRange(sliderRange);
		director.setContour(contourArea.getContour());

		waveFormArea.loadAudio(director.getAudio());
	}

	public void playBlock() {
		applyBlockSettings();
		director.loadAudio(false);
	}

	public void playFile() {
		applyBlockSettings();
		loadProsodyBlocksList();

		director.loadAudio(true);
	}

	public void prosodyBlocksList(int key) {
		// select current active block
		loadBlock(key, director.getBlock(currentBlockIndex));
	}

	public void clearTxt() {
		inputText.clear();
	}

	public void clearSliders() {
		cp5.getController("sliderVolume").setValue(ProsodyElement.DEFAULT_VOLUME);
		cp5.getController("sliderRate").setValue(ProsodyElement.DEFAULT_RATE);
		cp5.getController("sliderPitch").setValue(ProsodyElement.DEFAULT_PITCH);
		cp5.getController("sliderRange").setValue(ProsodyElement.DEFAULT_RANGE);
	}

	public void clearContour() {
		contourArea.reset();
	}

	public void mousePressed() {
		System.out.println(mouseX);
		System.out.println(contourArea.isOver());

		if(contourArea.isOver()) {
			contourArea.mousePressed();
		}
	}

	public void mouseDragged() {
		if(contourArea.isOver()) {
			contourArea.mouseDragged();
		}
	}

	public void mouseReleased() {
		contourArea.mouseReleased();
	}

	public void toggleRemoveCP(boolean flag) {
		contourArea.toggleAddRemoveMode(flag);
	}

	public void toggleCoords(boolean flag) {
		contourArea.toggleDisplayCoords(flag);
	}

	public void loadFile() {
		director.loadFile();

	}

	public void saveFile() {
		applyBlockSettings();
		loadProsodyBlocksList();

		director.saveFile();
	}

	public void loadBlock(int index, ProsodyElement workingBlock) {
		currentBlockIndex = index;
		originalText = workingBlock.getContent();
		inputText.setText(originalText);

		cp5.getController("sliderVolume").setValue(workingBlock.getD_volume());
		cp5.getController("sliderRate").setValue(workingBlock.getD_rate());
		cp5.getController("sliderPitch").setValue(workingBlock.getD_pitch());
		cp5.getController("sliderRange").setValue(workingBlock.getD_range());

		contourArea.setContour(workingBlock.getContour());
		applyBlockSettings();
	}

	public void loadProsodyBlocksList() {
		prosodyBlocksList.remove();
		clearProsodyBlocksList();

		for(Map.Entry<Integer, String> entry : director.getProsodyList().entrySet()) {
			prosodyBlocksList.addItem("[ " + entry.getKey() + " ] " + entry.getValue(), entry.getKey());
		}
		
		prosodyBlocksList.activate(currentBlockIndex);
	}
}