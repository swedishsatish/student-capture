package studentcapture.lti;
import java.util.ArrayList;

/**
 * 
 */

/**
 * This class handles takes a LTI consumer profile and splits up 
 * the information so they can be provided in methods for easy handle
 * @author jim
 *
 */
public class LTIConsumerProfileHandler {
	
	/**
	 * Makes a LTIConsumerProfileHandler
	 */
	public LTIConsumerProfileHandler(){
		
	}
	
	/**
	 * Takes in a text block of pre-given structure and a search word
	 * It then splits up the text block and searches for a given word
	 * When the word is found it returns the value it holds.
	 * 
	 * example: "guid" : "value"
	 * 
	 * it has guid as contentWord then it will return value.
	 * It only returns the first time the contentWord is found
	 * 
	 * @param consumerProfile
	 * @param contentWord
	 * @return String
	 */
	public String getFirstContentValueOF(String consumerProfile, String contentWord){
		
		String[] textFragment = consumerProfile.split("\n");
		
		for(int i = 0; i < textFragment.length; i++){
			if(textFragment[i].contains(contentWord)){
				String[] lineSplit = textFragment[i].split("\"");
				
				for(int j = 0; j < lineSplit.length; j++){
					if(lineSplit[j].contains(contentWord)){
						if((j+2) < lineSplit.length){
							return lineSplit[j+2];
						}
					}
				}
			}
		}
		
		return "";
	}
	
	/**
	 * Takes in a text block of pre-given structure and a search word
	 * It then splits up the text block and searches for a given word
	 * When the word is found it returns the value it holds.
	 * 
	 * example: "guid" : "value"
	 * 
	 * it has guid as contentWord then it will return value.
	 * 
	 * It returns an ArrayList<String> with every time contentWord is found
	 * and if no values are found it will return an empty ArrayList<String>
	 * 
	 * @param consumerProfile
	 * @param contentWord
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getAllContentValueOF(String consumerProfile, String contentWord){
		ArrayList<String> values = new ArrayList<String>();
		String[] textFragment = consumerProfile.split("\n");
		
		for(int i = 0; i < textFragment.length; i++){
			if(textFragment[i].contains(contentWord)){
				String[] lineSplit = textFragment[i].split("\"");
				
				for(int j = 0; j < lineSplit.length; j++){
					if(lineSplit[j].contains(contentWord)){
						if((j+2) < lineSplit.length){
							values.add(lineSplit[j+2]);
						}
					}
				}
			}
		}
		
		return values;
	}
	
	/**
	 * Uses getFirstContentValueOF but with a pre-given contentWord
	 * 
	 * @param consumerProfile
	 * @return
	 */
	public String getGuid(String consumerProfile){
		return getFirstContentValueOF(consumerProfile, "guid");
	}

}
