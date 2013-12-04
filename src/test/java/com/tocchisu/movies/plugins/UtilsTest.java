package com.tocchisu.movies.plugins;

import static junit.framework.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

public class UtilsTest {
	public void testDownloadInterfaces() {}

	@Test
	public void testExtractInterfaces() throws IOException {
		URL sourceURL = Thread.currentThread().getContextClassLoader().getResource("iso-aka-titles.list.gz");
		File destinationFile = new File(System.getProperty("java.io.tmpdir"), "iso-aka-titles.list.gz");
		FileUtils.copyURLToFile(sourceURL, destinationFile);
		File unzippedFile = Utils.unGzip(destinationFile);
		assertNotNull(unzippedFile);
		assertEquals("iso-aka-titles.list", unzippedFile.getName());
		assertTrue(IOUtil.toString(new FileInputStream(unzippedFile)).startsWith("CRC: 0x8D08329F"));
		try {
			destinationFile = new File(System.getProperty("java.io.tmpdir"), "Wrong filename");
			Utils.unGzip(destinationFile);
			fail("Should throw an IllegalArgumentException when trying to extract ");
		}
		catch (IllegalArgumentException e) {

		}
	}
}