package sourceutils;

import java.net.MalformedURLException;
import java.net.URL;

public class StringOperations {
	
	public static String formatString(String input){
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
