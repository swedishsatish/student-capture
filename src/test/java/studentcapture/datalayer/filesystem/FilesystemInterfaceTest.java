package studentcapture.datalayer.filesystem;

import static org.junit.Assert.*;

import org.junit.Test;
import studentcapture.config.StudentCaptureApplication;

import java.io.File;

public class FilesystemInterfaceTest extends StudentCaptureApplication {

	@Test
	public void testGeneratePathWithoutStudent() {
		String path = FilesystemInterface.generatePath("5DV151", 1, 123);
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/");
	}


	
	@Test
	public void testGeneratePathWithStudent() {
		String path = FilesystemInterface.generatePath("5DV151", 1, 123, 654);
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/654/");
	}

    @Test
    public void testCreatedMapStoreStudentVideo() {

    }

    @Test
    public void testStoreStudentVideo() {

        //The file to be stored
        File file = new File("~/");

        //Adjusting path
        String temp = file.getAbsolutePath();
        String path = temp.substring(0,temp.length()-1) +"/bugsbunny.webm";

        file = new File(path);

        System.out.println("name " + file.getName());
        System.out.println("Abs: " + file.getAbsolutePath());
        boolean res = FilesystemInterface.storeStudentVideo("5DV151",1,1,1,file);

        assertTrue(res);

    }


}
