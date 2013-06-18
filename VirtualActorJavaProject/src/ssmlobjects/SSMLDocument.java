package ssmlobjects;

import java.util.ArrayList;
import java.util.TreeMap;

import org.w3c.dom.*;

import sourceutils.TextFileUtilities;

/**
 * Object representation of a simple SSML document
 * 
 */
public class SSMLDocument {
	private ArrayList<ProsodyElement> blocks;


	public SSMLDocument() {
		blocks = new ArrayList<ProsodyElement>();
	}

	public SSMLDocument(String path) {
		String ssmlTxt = TextFileUtilities.readFile(path);
		Document xmlDoc = TextFileUtilities.parseXML(ssmlTxt);

		NodeList nodeList = xmlDoc.getElementsByTagName("prosody");
		xmlDoc.getDocumentElement().normalize();

		ArrayList<ProsodyElement> elems = generateProsodyElems(nodeList);
		blocks = new ArrayList<ProsodyElement>();
		blocks.addAll(elems);
	}

	private ArrayList<ProsodyElement> generateProsodyElems(NodeList nodeList) {
		ArrayList<ProsodyElement> elems = new ArrayList<ProsodyElement>();

		for(int i=0; i<nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);

			// build ProsodyElem from node
			ProsodyElement e = new ProsodyElement(childNode);
			elems.add(e);
		}
		return elems;
	}

	public void putBlock(ProsodyElement pe) {
		this.blocks.add(pe);
	}

	public ProsodyElement getBlock(int i) {
		return this.blocks.get(i);
	}

	public TreeMap<Integer, String> getBlockList() {
		TreeMap<Integer, String> list = new TreeMap<Integer, String>();
		for(ProsodyElement block : this.blocks) {
			list.put(this.blocks.indexOf(block), block.getContent());
		}
		return list;
	}

	public String toString() {
		String output =	"<?xml version=\"1.0\"?> \n" +
				"<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" \n" +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
				"xsi:schemaLocation=\"http://www.w3.org/2001/10/synthesis \n" +
				"http://www.w3.org/TR/speech-synthesis/synthesis.xsd\" \n" +
				"xml:lang=\"en-US\"> \n" +
				"<p> \n";

		for(ProsodyElement pe : this.blocks) {
			output += pe.toString() + "\n";
		}
		output+="</p> \n</speak>";

		return output;
	}

	public boolean isEmpty() {
		return this.blocks.isEmpty();
	}

	public void writeToFile(String path) {
		TextFileUtilities.writeToFile(path, this.toString());
	}
}
