package testpackage;

import maryttsutils.MaryTTSWrapper;

public class VirtualActorTest1 {
	
	public static void main(String[] args) {
		MaryTTSWrapper.init();
		
//		MaryTTSWrapper.printAvailableConfig();
//		MaryTTSWrapper.printConfig();
		
		// 2001
//		MaryTTSWrapper.readTxtInput("I'm sorry Dave, I'm afraid I cant do that.");
		
		// read first tweet
		MaryTTSWrapper.readTweetAsMaryXML("#art", null, "-20%", "(0%,30%) (10%,-10%) (40%,40%) (60%,0%) (90%,30%) (100%,0%)");
				
		// reading don quixote
//		MaryTTSWrapper.readRawMaryXMLFile("../xml-input/maryxml_donquixotewindmills.xml");	
		
		// presentation 1 tests
		
		// same contour, different volume, rate, pitch, range.
//		MaryTTSWrapper.readRawMaryXMLFile("../xml-input/maryxml_presentation1_emotions1.xml");
		
		// same everything, different contour.
//		MaryTTSWrapper.readRawMaryXMLFile("../xml-input/maryxml_presentation1_emotions2.xml");
//		MaryTTSWrapper.readSSMLFile("../xml-input/ssml_presentation1_emotions2.xml");
//		MaryTTSWrapper.readSSMLFile("../xml-input/ssml_presentation1_emotions3.xml");
		
		System.exit(0);
	}

}
