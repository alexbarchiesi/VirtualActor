package testpackage;

import maryttsutils.MaryTTSWrapper;
import ssmlobjects.*;

public class ProsodyTest1 {

	public static void main(String[] args) {
		ProsodyElem pe = new ProsodyElem();
		
		pe.setContent("hello? is it me you're looking for?");
		
		SSMLDoc sd = new SSMLDoc();
		
		sd.putBlock(pe);
		

		MaryTTSWrapper.init();
		
		System.out.println(sd.toString());
		MaryTTSWrapper.readInput(sd.toString(), "SSML");
		
		System.exit(0);
	}

}
