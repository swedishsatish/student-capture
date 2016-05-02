package studentcapture.datalayer.filesystem;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilesystemInterfaceTest {

	@Test
	public void testGeneratePathWithoutStudent() {
		String path = FilesystemInterface.generatePath("5DV151", "1", "123");
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/");
	}
	
	@Test
	public void testGeneratePathWithStudent() {
		String path = FilesystemInterface.generatePath("5DV151", "1", "123", "654");
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/654/");
	}

	@Test
	public void testSaveAFile() {

	}

}
