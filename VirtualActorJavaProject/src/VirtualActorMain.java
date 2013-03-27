import java.util.Set;

import javax.sound.sampled.AudioInputStream;

import org.w3c.dom.Document;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;
import marytts.util.dom.DomUtils;

public class VirtualActorMain {

	
	public static void main(String[] args) throws Exception {

		MaryInterface marytts = new LocalMaryInterface();
		
		System.out.println("Available voices: "+marytts.getAvailableVoices());
		System.out.println("Available locales: "+marytts.getAvailableLocales());
		System.out.println("Available input types: "+marytts.getAvailableInputTypes());
		System.out.println("Available output types: "+marytts.getAvailableOutputTypes());
		System.out.println();
		
		marytts.setInputType("RAWMARYXML");
		
		System.out.println("Voice: "+marytts.getVoice());
		System.out.println("Locale: "+marytts.getLocale());
		System.out.println("Input type: "+marytts.getInputType());
		System.out.println("Output type: "+marytts.getOutputType());
		System.out.println();
		
		System.out.println(marytts.getInputType());
		System.out.println(marytts.getOutputType());
//		marytts.setVoice(voices.iterator().next());
		
		String dataString = VirtualActorUtils.readFile("../maryxml-input/donquixotewindmills1.xml");
		Document xmlDocument = DomUtils.parseDocument(dataString, false);
		AudioInputStream audio = marytts.generateAudio(xmlDocument);
		
		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		player.join();
		System.exit(0);
	}
	
}