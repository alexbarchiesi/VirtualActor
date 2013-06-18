package ssmlobjects;

import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sourceutils.TextFileUtilities;

/**
 * Object representation of a Prosody element as per SSML specifications
 *
 */
public class ProsodyElement {
	public final static String DEFAULT_CONTENT = "Type here...";
	public final static int DEFAULT_VOLUME = 100;
	public final static int DEFAULT_RATE = 0;
	public final static int DEFAULT_PITCH = 0;
	public final static int DEFAULT_RANGE = 2;	
	
	private String	content;
	private int		d_volume;
	private int		d_rate;
	private int		d_pitch;
	private int		d_range;
	private TreeMap<Integer, Integer> contour;

	public ProsodyElement() {
		this.content = DEFAULT_CONTENT;
		this.d_volume = DEFAULT_VOLUME;
		this.d_rate = DEFAULT_RATE;
		this.d_pitch = DEFAULT_PITCH;
		this.d_range = DEFAULT_RANGE;
		this.contour = new TreeMap<Integer, Integer>();
	}
	
	public ProsodyElement(Node prosodyNode) {
		if (prosodyNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) prosodyNode;
            
            this.content = element.getTextContent().trim();
            
            if (element.hasAttribute("volume")) {
                this.d_volume = TextFileUtilities.extractInt(element.getAttribute("volume"));
            } else {
        		this.d_volume = DEFAULT_VOLUME;
            }

            if (element.hasAttribute("rate")) {
                this.d_rate = TextFileUtilities.extractInt(element.getAttribute("rate"));
            } else {
        		this.d_rate = DEFAULT_RATE;
            }

            if (element.hasAttribute("pitch")) {
                this.d_pitch = TextFileUtilities.extractInt(element.getAttribute("pitch"));
            } else {
        		this.d_pitch = DEFAULT_PITCH;
            }

            if (element.hasAttribute("range")) {
                this.d_range = numericPitchRange(element.getAttribute("range").trim());
            } else {
        		this.d_range = DEFAULT_RATE;
            }
            
            this.contour = new TreeMap<Integer, Integer>();
            		
            if (element.hasAttribute("contour")) {
            	generateContour(element.getAttribute("contour").replaceAll("\\s",""));
            }
        } else {
        	new ProsodyElement();
        }
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getD_volume() {
		return d_volume;
	}

	public void setD_volume(int d_volume) {
		this.d_volume = d_volume;
	}

	public int getD_rate() {
		return d_rate;
	}

	public void setD_rate(int d_rate) {
		this.d_rate = d_rate;
	}

	public int getD_pitch() {
		return d_pitch;
	}

	public void setD_pitch(int d_pitch) {
		this.d_pitch = d_pitch;
	}

	public int getD_range() {
		return d_range;
	}

	public void setD_range(int d_range) {
		this.d_range = d_range;
	}
	
	public TreeMap<Integer, Integer> getContour() {
		TreeMap<Integer, Integer> contourCopy = new TreeMap<Integer, Integer>();
		contourCopy.putAll(this.contour);
		
		return contourCopy;
	}

	public void setContour(TreeMap<Integer, Integer> ct) {
		this.contour = new TreeMap<Integer, Integer>();
		if(ct == null || ct.isEmpty()) return;
		this.contour.putAll(ct);
	}

	public SSMLDocument asDocument() {
		SSMLDocument newDoc = new SSMLDocument();
		newDoc.putBlock(this);
		
		return newDoc;
	}

	public String toString(){
			String output = "<prosody ";
			
			if(d_volume !=100)	output += "volume=\""	+ d_volume+"\" ";
	//							output += "volume=\""	+ ordinalVolume(d_volume)+"\" ";
			if(d_rate !=0)		output += "rate=\""		+ (d_rate > 0 ? "+" : "")	+ d_rate+"%\" ";
			if(d_pitch !=0)		output += "pitch=\""	+ (d_pitch > 0 ? "+" : "")	+ d_pitch+"st\" ";
	//							output += "pitch=\""	+ ordinalPitch(d_pitch) +"\" ";
	//		if(d_range !=0)		output += "range=\""	+ (d_range > 0 ? "+" : "")	+ d_range+"%\" ";
								output += "range=\""	+ ordinalPitch(d_range) +"\" ";
			
			if(!contour.isEmpty()){
				output += "contour=\"";
				
				for(Map.Entry<Integer, Integer> entry : contour.entrySet()){
					int time  = entry.getKey();
					int pitch = entry.getValue();
					
					output += "("+time+"%,"+pitch+"%) ";
				}
				
				output += "\"";
			}
			
			output += ">\n"+content+"\n</prosody>";
			
			return output;
		}

	private void generateContour(String contour) {
			System.out.println(contour);
			 for(String point : contour.split("[()]+")){
				String[] pair = point.split(",");
				
				if(pair.length != 2){
	//				System.out.println("skipping bad contour pair: "+point);
					continue;
				}
				
				int time = TextFileUtilities.extractInt(pair[0]);
				int pitch = TextFileUtilities.extractInt(pair[1]);
				
				this.contour.put(time, pitch);
			}
		}

	private String ordinalVolume(int value){
		switch(value){
		case 0:
			return "silent";
		case 1:
			return "x-soft";
		case 2:
			return "soft";
		case 3:
			return "medium";
		case 4:
			return "loud";
		case 5:
			return "x-loud";
		}
		
		return "default";
	}

	private String ordinalPitch(int value){
		switch(value){
		case 0:
			return "x-low";
		case 1:
			return "low";
		case 2:
			return "medium";
		case 3:
			return "high";
		case 4:
			return "x-high";
		}
		
		return "default";
	}
	
	private int numericPitchRange(String value){
		switch(value){
		case "x-low":
			return 0;
		case "low":
			return 1;
		case "medium":
			return 2;
		case "high":
			return 3;
		case "x-high":
			return 4;
		}
		return DEFAULT_RANGE;
	}

}
