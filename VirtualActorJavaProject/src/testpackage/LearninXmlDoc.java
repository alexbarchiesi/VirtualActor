package testpackage;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sourceutils.TextFileUtilities;

public final class LearninXmlDoc
{
	private static String getTagValue(final Element element, String tag)
	{
		if (element.hasAttribute(tag)) {
			return element.getAttribute(tag);
		}
		return "nope";
	}

	public static void main(String[] args)
	{
		final String fileName = "E:\\EPFL\\MSc Semester Project II - Virtual Actor\\VirtualActor\\xml-input\\maryxml_presentation1_emotions1.xml";

		readXML(fileName);
	}

	private static void readXML(String fileName)
	{
		Document document;
		DocumentBuilder documentBuilder;
		DocumentBuilderFactory documentBuilderFactory;
		NodeList nodeList;
		File xmlInputFile;

		try
		{
			xmlInputFile = new File(fileName);
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlInputFile);
			nodeList = document.getElementsByTagName("prosody");

			document.getDocumentElement().normalize();

			for (int index = 0; index < nodeList.getLength(); index++)
			{
				Node node = nodeList.item(index);
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;

					System.out.println(element.getTextContent());
					System.out.println("\trate : " + TextFileUtilities.extractInt(getTagValue(element, "rate")));
					System.out.println("\tpitch : " + TextFileUtilities.extractInt(getTagValue(element, "pitch")));
					System.out.println("\tvolume : " + TextFileUtilities.extractInt(getTagValue(element, "volume")));
					System.out.println("\trange : " + getTagValue(element, "range"));
					System.out.println("-----");
				}
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}
