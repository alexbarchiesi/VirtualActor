package testpackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parseContour {

	public static void main(String[] args) {
		String s = "(0%,0%) (10%,50%) (20%,-20%) (50%,10%) (70%,40%) (100%,10%)";

		String[] ss = s.split("[()\\s]+");


		for(String st: ss){
			if(st.length() == 0) continue;

			String[] sts = st.split(",");

			System.out.println(st);
			System.out.println(sts[0]);
			System.out.println(sts[1]);

			int x = extractInt(sts[0]);
			int y = extractInt(sts[1]);

			System.out.println(x+" = "+y);
		}
	}

	public static int extractInt(String str) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(str);
		if(m.find()){
			return Integer.parseInt(m.group());
		}
		return 230689; // unlikely
	}

}
