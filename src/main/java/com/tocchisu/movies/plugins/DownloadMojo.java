package com.tocchisu.movies.plugins;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import com.tocchisu.movies.interfaces.DownloadStatusListener;
import com.tocchisu.movies.interfaces.IMDBInterfacesManager;

/**
 * IMDB regularly provides plain text data files, called "interfaces", that stores useful informations about movies, actors, genre, ratings, etc. These files,
 * can be held locally by downloading them at <a href="http://www.imdb.com/interfaces">http://www.imdb.com/interfaces</a>.
 * This class describes a maven goal which downloads IMDB interfaces.
 * 
 * @goal download
 * 
 * @phase process-sources
 * 
 *        Usage : mvn clean install movies:download -Dmovies.directory='...'
 */
public class DownloadMojo extends AbstractMojo {

	private static enum Protocol {
		FTP, HTTP
	};

	/**
	 * Location of the movie files.
	 * 
	 * @parameter expression="${movies.directory}" default-value="${basedir'/interfaces}"
	 */
	private File	targetDirectory;
	/**
	 * Should the movie files automatically be downloaded even if a movie file is already present on the file system.
	 * 
	 * @parameter expression="${movies.forceDownload}" default-value=false
	 */
	private boolean	forceDownload;

	/**
	 * Protocol used to download movies (FTP, HTTP)
	 * 
	 * @parameter expression="${movies.protocol}" default-value="ftp"
	 */
	private String	protocol;

	public void execute() throws MojoExecutionException {
		if (Protocol.valueOf(StringUtils.upperCase(protocol)) == null) {
			fail("Protocol not allowed. Available protocols are {0}", (Object[]) Protocol.values());
		}
		checkTargetDirectory();
		downloadInterface("iso-aka-titles", targetDirectory);
		downloadInterface("technical", targetDirectory);
		downloadInterface("movies", targetDirectory);

	}

	private void downloadInterface(String interfaceName, File destinationDirectory) throws MojoExecutionException {
		DownloadStatusListener listener = new InterfaceDownloadListener(interfaceName);
		try {
			IMDBInterfacesManager.download(interfaceName, destinationDirectory, listener);
		}
		catch (IOException e) {
			fail("Error while downloading IMDB interface {0}", e, interfaceName);
		}
	}

	/**
	 * Checks existence of the directory where the movies will be downloaded. If it doesn't exist, the method will create it.
	 * 
	 * @return A safe directory location to download movies
	 * @throws MojoExecutionException
	 */
	private File checkTargetDirectory() throws MojoExecutionException {
		if (getLog().isInfoEnabled()) {
			getLog().info(
					MessageFormat
							.format("Movies files will be downloaded in the directory {0}. Feel free to define a specific location to receive movies files. Usage : mvn movies:download -Dmovies.directory='...'",
									targetDirectory.getAbsoluteFile()));
		}
		if (!targetDirectory.exists()) {
			if (targetDirectory.getParentFile().canWrite()) {
				targetDirectory.mkdir();
				getLog().info(MessageFormat.format("Directory {0} has been created in order to receive movies files", targetDirectory.getAbsoluteFile()));
			}
			else {
				fail("It sounds like directory {0} cannot be accessed. Please check your permission rights on this directory or change the directory using mvn movies:download -Dmovies.directory='...' command",
						targetDirectory.getParentFile());
			}
		}
		else if (!targetDirectory.isDirectory()) {
			fail("Attempt to download movies within a file instead of a directory. Please turn {0} into a directory or change its location using mvn movies:download -Dmovies.directory='...' command ",
					targetDirectory.getAbsoluteFile());
		}
		return targetDirectory;
	}

	/**
	 * Utility method to log message errors in Maven console and fails tests execution
	 * 
	 * @param message
	 *            The message pattern to log
	 * @param params
	 *            The optional params to apply to the message pattern
	 * @throws MojoExecutionException
	 */
	private void fail(String message, Object... params) throws MojoExecutionException {
		throw new MojoExecutionException(MessageFormat.format(message, params));
	}

	/**
	 * Utility method to log message errors in Maven console and fails tests execution
	 * 
	 * @param message
	 *            The message pattern to log
	 * @param cause
	 *            Subsequent error
	 * @param params
	 *            The optional params to apply to the message pattern
	 * @throws MojoExecutionException
	 */
	private void fail(String message, Throwable cause, Object... params) throws MojoExecutionException {
		throw new MojoExecutionException(MessageFormat.format(message, params), cause);
	}

	private class InterfaceDownloadListener implements DownloadStatusListener {
		private String	fileName;

		public InterfaceDownloadListener(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public void beforeDownload(Long fileSize) {
			getLog().info("Downloading " + fileName + "...");
		}

		@Override
		public void onProgress(Long bytesCount, Long fileSize) {}

		@Override
		public void afterDownload(Long bytesCount, Long fileSize, File destinationFile) {
			getLog().info("Downloaded " + FileUtils.byteCountToDisplaySize(bytesCount) + " at " + destinationFile.getAbsolutePath());
		}
	}
}
