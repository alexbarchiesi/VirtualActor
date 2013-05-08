package directorui;

import java.awt.BorderLayout;
import java.util.TreeMap;

import maryttsutils.MaryTTSWrapper;
import ssmlobjects.ProsodyElem;
import ssmlobjects.SSMLDoc;

public class VirtualDirector {
	private SSMLDoc sd = new SSMLDoc();
	private ProsodyElem pe = new ProsodyElem();

	public static void main(String[] args) {
		MaryTTSWrapper.init();
		// TODO Auto-generated method stub
		VirtualDirector director = new VirtualDirector();
		
		new DisplayFrame(director).setVisible(true);

	}
	
	public void reset(){
		sd =  new SSMLDoc();
		pe = new ProsodyElem();
	}
	
	public void setText(String input){
		reset();
		pe.setContent(input);
		sd.putBlock(pe);
	}
	
	public void setVolume(int d_volume) {
		pe.setD_volume(d_volume);
	}
	
	public void setRate(int d_rate) {
		pe.setD_rate(d_rate);
	}

	
	public void setPitch(int d_pitch) {
		pe.setD_pitch(d_pitch);
	}

	
	public void setRange(int d_range) {
		pe.setD_range(d_range);
	}


	public void playAudio() {
		System.out.println(sd.toString());
		
		MaryTTSWrapper.readInput(sd.toString(), "SSML");
	}

	public void setContour(TreeMap<Integer, Integer> contour) {
		pe.setContour(contour);
	}
}

class DisplayFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;

	public DisplayFrame(VirtualDirector parent){
        this.setSize(540, 800); //The window Dimensions
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        javax.swing.JPanel panel = new javax.swing.JPanel( new BorderLayout() );
        
        processing.core.PApplet sketch = new VirtualDirectorSketch(this.getWidth(), this.getHeight(), parent);
        panel.add(sketch, BorderLayout.CENTER);
        
        this.add(panel);
        
        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);
    }
}
