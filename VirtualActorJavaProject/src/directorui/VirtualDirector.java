package directorui;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import maryttsutils.MaryTTSWrapper;
import ssmlobjects.ProsodyElement;
import ssmlobjects.SSMLDocument;

/**
 * Main execution class, instantiates and manages the different components.
 *
 */
public class VirtualDirector {
	// for file handling
	private JFileChooser xmlFileChooser;
	private JFileChooser wavFileChooser;

	// working data structures
	private SSMLDocument workingDocument;
	private ProsodyElement workingBlock;

	// GUI
	private DisplayFrame mainFrame;
	private VirtualDirectorSketch sketch;
	private boolean playAtNextDraw;
	private boolean playWholeFile;

	public VirtualDirector() {
		xmlFileChooser = new JFileChooser();
		xmlFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		xmlFileChooser.setFileFilter(new FileNameExtensionFilter("SSML file", "xml"));

		wavFileChooser = new JFileChooser();
		wavFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		wavFileChooser.setFileFilter(new FileNameExtensionFilter("WAV file", "wav"));

		workingDocument = new SSMLDocument();
		workingBlock = new ProsodyElement();
		workingDocument.putBlock(workingBlock);

		playAtNextDraw = false;
		playWholeFile = false;
	}

	public static void main(String[] args) {
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			e.printStackTrace();  
		} 

		MaryTTSWrapper.init();

		int w=1000, h=800;

		VirtualDirector director = new VirtualDirector();

		VirtualDirectorSketch mainSketch = new VirtualDirectorSketch(director, w,h);
		DisplayFrame mainFrame = new DisplayFrame(director, mainSketch, w, h);

		director.sketch = mainSketch;
		director.mainFrame = mainFrame;

		//		PApplet playerSketch = new PlayerSketch(director, 300,600);
		//		DisplayFrame playerFrame = new DisplayFrame(director, playerSketch, 200, 200);
	}

	private void reset() {
		workingDocument =  new SSMLDocument();
		workingBlock = new ProsodyElement();
	}

	public void setText(String input) {
		workingBlock.setContent(input);
	}

	public void setVolume(int d_volume) {
		workingBlock.setD_volume(d_volume);
	}

	public void setRate(int d_rate) {
		workingBlock.setD_rate(d_rate);
	}

	public void setPitch(int d_pitch) {
		workingBlock.setD_pitch(d_pitch);
	}

	public void setRange(int d_range) {
		workingBlock.setD_range(d_range);
	}

	public void loadAudio(boolean playWholeFile) {
		this.playAtNextDraw = true;
		this.playWholeFile = playWholeFile;
	}

	public void playAudio() {
		this.playAtNextDraw = false;
		String toPlay = "";

		if(this.playWholeFile) {
			toPlay = this.workingDocument.toString();
		} else {
			toPlay = this.workingBlock.asDocument().toString();
		}
		MaryTTSWrapper.playAudio(MaryTTSWrapper.xml2audio(toPlay, "SSML"));
		System.out.println("Now playing:");
		System.out.println(toPlay);
	}

	public void setContour(TreeMap<Integer, Integer> contour) {
		workingBlock.setContour(contour);
	}

	public AudioInputStream getAudio() {
		AudioInputStream audio = MaryTTSWrapper.xml2audio(workingBlock.asDocument().toString(), "SSML");
		if(audio == null) {
			System.out.println("oh shoot!");
		}
		return audio;
	}

	public boolean isPlayAtNextDraw() {
		return playAtNextDraw;
	}

	public void loadFile() {
		if (xmlFileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) { 
			File file = xmlFileChooser.getSelectedFile(); 

			if (MaryTTSWrapper.isValidType(file)) {
//				System.out.println("Loading file...");
				workingDocument = new SSMLDocument(file.getAbsolutePath());

				if(workingDocument.isEmpty()) {
					// do as if no file was given
//					System.out.println("Error empty file");
					reset();
				} else {
					workingBlock = workingDocument.getBlock(0);
				}
			}
		}
	}

	public void saveFile() {		
		if (xmlFileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) { 
			File file = xmlFileChooser.getSelectedFile();

			if(file.exists()) {
				// if file exists, prompt for confirmation
				String title = "Overwrite file";
				String message = "File already exists, overwrite?";

				int reply = JOptionPane.showConfirmDialog(mainFrame, message, title, JOptionPane.YES_NO_OPTION);
				if (reply != JOptionPane.YES_OPTION) {
					return; // cancel save operation
				}
			}
			// proceed to writing the file
			workingDocument.writeToFile(file.getAbsolutePath());
		}
	}

	public TreeMap<Integer, String> getProsodyList() {
		TreeMap<Integer, String> list = workingDocument.getBlockList();

		if(list.isEmpty()) {
			// should never happen
			list.put(0, "");
			System.out.println("empty working document sent to sketch");
		}
		return list;
	}

	public ProsodyElement getWorkingBlock(int key) {
		workingBlock = workingDocument.getBlock(key);
		return workingBlock;
	}

	public void exportAudioFile() {
		if (wavFileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) { 
			File file = wavFileChooser.getSelectedFile();

			if(file.exists()) {
				// if file exists, prompt for confirmation
				String title = "Overwrite file";
				String message = "File already exists, overwrite?";

				int reply = JOptionPane.showConfirmDialog(mainFrame, message, title, JOptionPane.YES_NO_OPTION);
				if (reply != JOptionPane.YES_OPTION) {
					return; // cancel save operation
				}
			}
			
			// proceed to writing the file
			AudioInputStream audio = MaryTTSWrapper.xml2audio(this.workingDocument.toString(), "SSML");
			
			try {
				AudioSystem.write(audio, AudioFileFormat.Type.WAVE, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addNewBlockAt(int pos) {
		workingBlock = new ProsodyElement();
		
		workingDocument.putBlock(workingBlock, pos);
	}

	public boolean isSingleBlock() {
		return workingDocument.isSingleBlock();
	}

	public void deleteCurrentBlock() {
		workingDocument.removeBlock(workingBlock);
	}
}