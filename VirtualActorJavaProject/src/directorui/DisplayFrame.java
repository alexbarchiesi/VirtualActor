package directorui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import processing.core.PApplet;

/**
 * JFrame used to display the Processing Sketch
 *
 */
class DisplayFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;

	public DisplayFrame(VirtualDirector parent, PApplet sketch, int w, int h) {	
		this.setSize(w+18, h+45);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new javax.swing.JPanel( new BorderLayout() );
		panel.add(sketch, BorderLayout.CENTER);
		this.add(panel);

		// start execution
		sketch.init();
		this.setVisible(true);
		this.setResizable(false);
	}
}
