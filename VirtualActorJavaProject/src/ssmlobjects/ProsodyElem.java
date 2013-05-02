package ssmlobjects;

import java.util.Map;
import java.util.TreeMap;

public class ProsodyElem {
	private String	content;
	private int		d_volume;
	private int		d_rate;
	private int		d_pitch;
	private int		d_range;
	private TreeMap<Integer, Integer> contour;

	public ProsodyElem() {
		this.content = "";
		this.d_volume = 0;
		this.d_rate = 0;
		this.d_pitch = 0;
		this.d_range = 0;
		this.contour = new TreeMap<Integer, Integer>();
	}
	
	public String toString(){
		String output = "<prosody ";
		
		if(d_volume !=0) output += "volume=\""+d_volume+"\" ";
		if(d_rate !=0) output += "rate=\""+d_rate+"\" ";
		if(d_pitch !=0) output += "pitch=\""+d_pitch+"\" ";
		if(d_range !=0) output += "range=\""+d_range+"\" ";
		
		if(!contour.isEmpty()){
			output += "contour=\"";
			
			for(Map.Entry<Integer, Integer> entry : contour.entrySet()){
				int time = entry.getKey();
				int pitch = entry.getValue();
				
				output += "("+time+"%,"+pitch+")";
			}
			
			output += "\"";
		}
		
		output += ">"+content+"</prosody>";
		
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
	
	

}
