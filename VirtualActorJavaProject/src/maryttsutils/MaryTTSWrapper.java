package maryttsutils;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import org.w3c.dom.Document;

import sourceutils.*;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import marytts.util.dom.DomUtils;

public class MaryTTSWrapper {
	
	private static MaryInterface MARYTTS;
	
	public static void init(){
		try {
			MARYTTS = new LocalMaryInterface();
		} catch (MaryConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readTxtInput(String txt){		
		MARYTTS.setInputType("TEXT");
	
		AudioInputStream audio;
		try {
			audio = MARYTTS.generateAudio(txt);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... marytts.generateAudio(txt)");
			return;
		}
	
		playAudio(audio);
	}
	
	public static void readInput(String input, String format){
		MARYTTS.setInputType(format);
		Document xmlDocument = null;
		
		try {
			xmlDocument = DomUtils.parseDocument(input, false);
		} catch (Exception e) {
			System.out.println("something went wrong... xmlDocument");
			return;
		}
		AudioInputStream audio = null;
		try {
			audio = MARYTTS.generateAudio(xmlDocument);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... audio");
			return;
		}
	
		playAudio(audio);
	}
	
	public static void readTxtFile(String path) {
		String dataString = "";
		try {
			dataString = TxtRetriever.readFile(path);
			
		} catch (IOException e) {
			System.out.println("error opening file \"" + path);
			return;
		}

		System.out.println("Reading file \""+ path + "\":");
		
		readTxtInput(dataString);
	}

	public static void readRawMaryXMLFile(String path){
		String dataString = "";
		try {
			dataString = TxtRetriever.readFile(path);
			
		} catch (IOException e) {
			System.out.println("error opening file \"" + path);
			return;
		}

		System.out.println("Reading file \""+ path + "\":");
		
		readInput(dataString, "RAWMARYXML");
	}
	
	public static void readSSMLFile(String path){
		String dataString = "";
		try {
			dataString = TxtRetriever.readFile(path);
			
		} catch (IOException e) {
			System.out.println("error opening file \"" + path);
			return;
		}

		System.out.println("Reading file \""+ path + "\":");
		
		readInput(dataString, "SSML");
	}
	
	public static void printAvailableConfig() {
		System.out.println("Available voices: "			+ MARYTTS.getAvailableVoices());
		System.out.println("Available locales: "		+ MARYTTS.getAvailableLocales());
		System.out.println("Available input types: "	+ MARYTTS.getAvailableInputTypes());
		System.out.println("Available output types: "	+ MARYTTS.getAvailableOutputTypes());
		System.out.println();
	}
	
	public static void printConfig() {
		System.out.println("Voice: "		+ MARYTTS.getVoice());
		System.out.println("Locale: "		+ MARYTTS.getLocale());
		System.out.println("Input type: "	+ MARYTTS.getInputType());
		System.out.println("Output type: "	+ MARYTTS.getOutputType());
		System.out.println();
	}
	
	public static void readTweetAsMaryXML(String keyword, String pitch, String rate, String contour){
		String tweet = TwitterRetriever.getFirstTweet(keyword);
		tweet = StringOperations.formatString(tweet);
		String maryxml = MaryTTSWrapper.txt2MaryXML(tweet, pitch, rate, contour);
		
		readInput(maryxml, "RAWMARYXML");
	}

	public static void playAudio(AudioInputStream audio){
		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		try {
			player.join();
		} catch (InterruptedException e) {
			System.out.println("something went wrong... player.join");
		}
	}

	public static String txt2MaryXML(String input, String pitch, String rate, String contour){
		String output =	"<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "+
						"<maryxml version=\"0.4\" "+
						 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
						 "xmlns=\"http://mary.dfki.de/2002/MaryXML\" "+
						 "xml:lang=\"en-US\"> " +
						 "<p> " +
						 "<prosody ";
		if(pitch != null){
			output += "pitch=\""+pitch+"\"  ";
		}
		if(rate != null){
			output += "rate=\""+rate+"\"  ";
		}
		if(contour != null){
			output += "contour=\""+contour+"\"  ";
		}
		
		output +=	">" +
					input +
					"</prosody> " +
					"</p> " +
					"</maryxml>";
		
		return output;
	}
}