package ssmlobjects;

import java.util.Map;
import java.util.TreeMap;

public class ProsodyElem {
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

	public ProsodyElem() {
		this.content = "";
		this.d_volume = DEFAULT_VOLUME;
		this.d_rate = DEFAULT_RATE;
		this.d_pitch = DEFAULT_PITCH;
		this.d_range = DEFAULT_RANGE;
		this.contour = new TreeMap<Integer, Integer>();
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
				int time = entry.getKey();
				int pitch = entry.getValue();
				
				output += "("+time+"%,"+pitch+"%)";
			}
			
			output += "\"";
		}
		
		output += ">\n"+content+"\n</prosody>";
		
		return output;
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

}
