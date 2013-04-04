import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import org.w3c.dom.Document;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import marytts.util.dom.DomUtils;

public class VirtualActorMain {
	
	private static MaryInterface marytts;

	
	public static void main(String[] args) throws Exception {
		marytts = new LocalMaryInterface();
		
		printAvailableConfig();
		printConfig();
		
		// read first tweet about waffles
		readTweet("waffles");
				
		// reading don quixote
		readRawMaryXMLInput("../maryxml-input/donquixotewindmills1.xml");
		
	}
	
	private static void readRawMaryXMLInput(String path){
		String dataString = "";
		try {
			dataString = VirtualActorUtils.readFile(path);
			
		} catch (IOException e) {
			System.out.println("error opening file \"" + path);
			return;
		}

		System.out.println("Reading file \""+ path + "\":");
		
		marytts.setInputType("RAWMARYXML");
		Document xmlDocument = null;
		
		try {
			xmlDocument = DomUtils.parseDocument(dataString, false);
		} catch (Exception e) {
			System.out.println("something went wrong... xmlDocument");
			return;
		}
		AudioInputStream audio = null;
		try {
			audio = marytts.generateAudio(xmlDocument);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... audio");
			return;
		}

		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		try {
			player.join();
		} catch (InterruptedException e) {
			System.out.println("something went wrong... player.join");
		}
	}
	
	private static void printAvailableConfig() {
		System.out.println("Available voices: "			+ marytts.getAvailableVoices());
		System.out.println("Available locales: "		+ marytts.getAvailableLocales());
		System.out.println("Available input types: "	+ marytts.getAvailableInputTypes());
		System.out.println("Available output types: "	+ marytts.getAvailableOutputTypes());
		System.out.println();
	}
	
	private static void printConfig() {
		System.out.println("Voice: "		+ marytts.getVoice());
		System.out.println("Locale: "		+ marytts.getLocale());
		System.out.println("Input type: "	+ marytts.getInputType());
		System.out.println("Output type: "	+ marytts.getOutputType());
		System.out.println();
	}

	private static void readTweet(String keyword){
		String tweet = TwitterRetriever.getTweet(keyword);
		
		System.out.println("Reading tweet about \""+ keyword + "\":");
		System.out.println(tweet);
		
		marytts.setInputType("TEXT");

		AudioInputStream audio;
		try {
			audio = marytts.generateAudio(tweet);
		} catch (SynthesisException e) {
			System.out.println("something went wrong... marytts.generateAudio(tweet)");
			return;
		}

		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		try {
			player.join();
		} catch (InterruptedException e) {
			System.out.println("something went wrong... player.join");
		}
	}
}