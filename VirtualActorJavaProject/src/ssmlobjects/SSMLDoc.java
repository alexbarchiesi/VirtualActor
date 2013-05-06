package ssmlobjects;

import java.util.ArrayList;

public class SSMLDoc {
	private ArrayList<ProsodyElem> blocks;
	

	public SSMLDoc() {
		blocks = new ArrayList<ProsodyElem>();
	}
	
	public void putBlock(ProsodyElem pe){
		this.blocks.add(pe);
	}
	
	public String toString(){
		String output =	"<?xml version=\"1.0\"?> \n" +
						"<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" \n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
						"xsi:schemaLocation=\"http://www.w3.org/2001/10/synthesis \n" +
						"http://www.w3.org/TR/speech-synthesis/synthesis.xsd\" \n" +
						"xml:lang=\"en-US\"> \n" +
						"<p> \n";
		
		for(ProsodyElem pe : blocks){
			output += pe.toString() + "\n";
		}
		
		output+="</p> \n</speak>";
		
		return output;
	}
}
