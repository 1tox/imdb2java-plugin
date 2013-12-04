package com.tocchisu.movies.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;

public class Utils {

	private Utils() {}

	/**
	 * @param destinationFile
	 *            The target directory where interfaces will be downloaded
	 * @return
	 * @throws IOException
	 */

	public static File download(URL url, File destinationFile) throws IOException {
		FileUtils.copyURLToFile(url, destinationFile);
		return destinationFile;
	}

	/**
	 * @param sourceFile
	 *            A
	 * @param destinationDirectory
	 */
	public static File unGzip(File sourceFile) throws IOException {
		if (!sourceFile.exists()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} not found", sourceFile));
		}
		if (sourceFile.isDirectory()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} is a directory file. You have to provide a file instead.", sourceFile));
		}
		GZIPInputStream fis = new GZIPInputStream(new FileInputStream(sourceFile));
		File destinationFile = new File(sourceFile.getParentFile(), StringUtils.substringBeforeLast(sourceFile.getName(), "."));
		FileOutputStream fos = new FileOutputStream(destinationFile);
		doCopy(fis, fos);
		return destinationFile;
	}

	private static void doCopy(InputStream is, OutputStream os) throws IOException {
		try {
			int oneByte;
			while ((oneByte = is.read()) != -1) {
				os.write(oneByte);
			}
		}
		finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}
}