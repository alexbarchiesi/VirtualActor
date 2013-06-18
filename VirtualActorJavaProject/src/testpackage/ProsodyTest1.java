package testpackage;

import maryttsutils.MaryTTSWrapper;
import ssmlobjects.*;

public class ProsodyTest1 {

	public static void main(String[] args) {
		ProsodyElement pe = new ProsodyElement();

		pe.setContent("hello? is it me you're looking for?");

		SSMLDocument sd = new SSMLDocument();

		sd.putBlock(pe);


		MaryTTSWrapper.init();

		System.out.println(sd.toString());
		MaryTTSWrapper.playAudio(MaryTTSWrapper.xml2audio(sd.toString(), "SSML"));

		System.exit(0);
	}

}
