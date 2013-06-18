package maryttsutils;
import java.io.File;

import javax.sound.sampled.AudioInputStream;

import org.w3c.dom.Document;

import sourceutils.*;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

/**
 * Utility class used to interact with MaryTTS
 *
 */
public class MaryTTSWrapper {

	private static MaryInterface MARYTTS;

	public static void init() {
		try {
			MARYTTS = new LocalMaryInterface();
		} catch (MaryConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static String loadTextFile(String path) {
		System.out.println("Reading file \""+ path + "\":");
		return TextFileUtilities.readFile(path);
	}

	public static AudioInputStream txt2audio(String txt) {		
		MARYTTS.setInputType("TEXT");

		AudioInputStream audio;
		try {
			audio = MARYTTS.generateAudio(txt);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... marytts.generateAudio(txt)");
			return null;
		}

		return audio;
	}

	public static AudioInputStream xml2audio(String input, String format) {
		MARYTTS.setInputType(format);
		Document xmlDocument = TextFileUtilities.parseXML(input);


		AudioInputStream audio = null;
		try {
			audio = MARYTTS.generateAudio(xmlDocument);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... audio");
			return null;
		}

		return audio;
	}

	public static void readTxtFile(String path) {
		String dataString = loadTextFile(path);

		playAudio(xml2audio(dataString, "TXT"));
	}

	public static void readRawMaryXMLFile(String path) {
		String dataString = loadTextFile(path);

		playAudio(xml2audio(dataString, "RAWMARYXML"));
	}

	public static void readSSMLFile(String path) {
		String dataString = loadTextFile(path);

		playAudio(xml2audio(dataString, "SSML"));
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

	public static void readTweetAsMaryXML(String keyword, String pitch, String rate, String contour) {
		String tweet = TwitterRetriever.getFirstTweet(keyword);
		tweet = TextFileUtilities.formatTweetString(tweet);
		String maryxml = MaryTTSWrapper.txt2MaryXML(tweet, pitch, rate, contour);
		playAudio(xml2audio(maryxml, "RAWMARYXML"));
	}

	public static void playAudio(AudioInputStream audio) {
		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		try {
			player.join();
		} catch (InterruptedException e) {
			System.out.println("something went wrong... player.join");
		}
	}

	public static String txt2MaryXML(String input, String pitch, String rate, String contour) {
		String output =	"<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "+
						"<maryxml version=\"0.4\" "+
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
						"xmlns=\"http://mary.dfki.de/2002/MaryXML\" "+
						"xml:lang=\"en-US\"> " +
						"<p> " +
						"<prosody ";
		if(pitch != null) {
			output += "pitch=\""+pitch+"\"  ";
		}
		if(rate != null) {
			output += "rate=\""+rate+"\"  ";
		}
		if(contour != null) {
			output += "contour=\""+contour+"\"  ";
		}

		output +=	">" +
					input +
					"</prosody> " +
					"</p> " +
					"</maryxml>";

		return output;
	}

	public static boolean isValidType(File file) {
		if ( file.getName().endsWith("xml") || file.getName().endsWith("txt") ) return true;
		return false;
	}
}