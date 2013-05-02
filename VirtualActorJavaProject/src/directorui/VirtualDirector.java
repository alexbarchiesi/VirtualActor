package directorui;

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
	
	public void setText(String input){
		pe = new ProsodyElem();
		pe.setContent(input);
		sd =  new SSMLDoc();
		sd.putBlock(pe);
	}
	
	public void playAudio() {
		MaryTTSWrapper.readInput(sd.toString(), "SSML");
	}
}

class DisplayFrame extends javax.swing.JFrame {
	
    public DisplayFrame(VirtualDirector parent){	
        this.setSize(600, 600); //The window Dimensions
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBounds(20, 20, 600, 600);
        
        processing.core.PApplet sketch = new VirtualDirectorSketch(parent);
        panel.add(sketch);
        
        this.add(panel);
        
        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);
    }
}
