package testpackage;

import java.awt.BorderLayout;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JPanel;

import processing.core.PApplet;

import maryttsutils.MaryTTSWrapper;

public class WaveFormTest {
	public boolean playAudio = false;
	public String text ="And we will never be alone again, cause it doesnt happen every day.";

	public WaveFormTest(){
		MaryTTSWrapper.init();
	}

	public static void main(String[] args) {
		WaveFormTest test = new WaveFormTest();
		AudioInputStream audio = MaryTTSWrapper.txt2audio(test.text);

		int w=600, h=150;

		PApplet mainSketch = new WaveFormSketch(test, audio, w, h);
		DisplayFrame mainFrame = new DisplayFrame(mainSketch, w, h);

//		test.text = "bye bye bye";
	}

	public void playAudio() {
		System.out.println("yes indeed...!");
		MaryTTSWrapper.playAudio(MaryTTSWrapper.txt2audio(text));
	}
}

class DisplayFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;

	public DisplayFrame(PApplet sketch, int w, int h){	
		this.setSize(w+18, h+45); // TODO something border dimensions
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new javax.swing.JPanel( new BorderLayout() );
		panel.add(sketch, BorderLayout.CENTER);
		this.add(panel);

		sketch.init(); //this is the function used to start the execution of the sketch
		this.setVisible(true);
		this.setResizable(false);
	}
}
