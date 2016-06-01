package studentcapture.lti;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import studentcapture.lti.LTIConsumerProfileHandler;

public class LTIConsumerProfileHandlerTest {
	LTIConsumerProfileHandler consumerProfileHandler;
	String consumerProfile;
	String guid = "5692e425-d380-4e3f-a31f-dc6081a0c932";
	
	/**
	 * Requires a LTI_printout.txt file to run.
	 * The printout consist of a toolConsumerProfile.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		FileReader file = new FileReader("LTI_printout.txt");
		BufferedReader textReader = new BufferedReader(file);
		StringBuilder sb = new StringBuilder();
		String temp;
		
		while((temp = textReader.readLine()) != null){
			sb.append(temp +"\n");
		}
		
		textReader.close();
		file.close();
		
		consumerProfile = sb.toString();
		consumerProfileHandler = new LTIConsumerProfileHandler();
	}

	@Test
	public void getGuidTest() {
		assertEquals(guid, consumerProfileHandler.getGuid(consumerProfile));
	}
	
	@Test
	public void getKeyTest() {
		assertEquals("product.name", consumerProfileHandler.getFirstContentValueOF(consumerProfile, "key"));
	}
	
	@Test
	public void getAllGuid(){
		ArrayList<String> allguid = consumerProfileHandler.getAllContentValueOF(consumerProfile, "guid");
		assertEquals(guid, allguid.get(0));
		assertEquals("sakai.school.edu", allguid.get(1));
	}

}
