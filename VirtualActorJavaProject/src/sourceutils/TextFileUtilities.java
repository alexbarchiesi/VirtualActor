package sourceutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marytts.util.dom.DomUtils;

import org.w3c.dom.Document;

/**
 * utility class used to manage text files and String operations
 * 
 */
public class TextFileUtilities {

	// source: http://stackoverflow.com/a/326440
	public static String readFile(String path) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(path));

			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			stream.close();

			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();

		} catch (FileNotFoundException e) {
			// invalid file path?
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document parseXML(String input) {
		Document xmlDocument;
		try {
			xmlDocument = DomUtils.parseDocument(input, false);
		} catch (Exception e) {
			System.out.println("something went wrong... xmlDocument");
			return null;
		}
		return xmlDocument;
	}

	public static void writeToFile(String fileName, String toWrite) {
		BufferedWriter outFile;
		try {
			outFile = new BufferedWriter(new FileWriter(fileName));
			outFile.write(toWrite);
			outFile.flush();
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int extractInt(String str) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(str);
		if(m.find()) {
			return Integer.parseInt(m.group());
		}
		System.out.println("No Integer was found in String: " + str);
		return 230689; // unlikely
	}
	
	public static String formatTweetString(String input) {
		String output = input;

		// simple replace 
		output.replaceAll("#", "hashtag ");
		output.replaceAll("http://", " ");
		output.replaceAll("www", "w w w");
		output.replaceAll(".com", " dot com");
		output.replaceAll("/", " slash ");

		// url rewrite
		String [] parts = output.split("\\s");

		// Attempt to convert each item into an URL.   
		for( String item : parts ) try {
			@SuppressWarnings("unused")
			URL url = new URL(item);

			System.out.println(item);

			// If possible then replace

			System.out.println(item);

		} catch (MalformedURLException e) {
			// If there was an URL that was not it!...
		}

		StringBuilder builder = new StringBuilder();
		for(String item : parts) {
			builder.append(item + " ");
		}
		output = builder.toString();

		return output;
	}

}
