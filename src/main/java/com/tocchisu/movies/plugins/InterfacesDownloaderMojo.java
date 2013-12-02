package com.tocchisu.movies.plugins;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which downloads IMDB interfaces
 * 
 * @goal download
 * 
 * @phase process-sources
 * 
 *        Usage : mvn clean install movies:donwload -Dmovies.directory='...'
 */
public class InterfacesDownloaderMojo extends AbstractMojo {
	// URL for downloading plain text interfaces
	private static final String	IMDB_INTERFACES_URL	= "http://www.website.com/information.asp";
	/**
	 * Location of the movie files.
	 * 
	 * @parameter expression="${movies.directory}" default-value="${basedir'/interfaces}"
	 */
	private File				moviesDirectory;
	/**
	 * Should the movie files automatically be downloaded even if a movie file is arleady present on the file system.
	 * 
	 * @parameter expression="${movies.forceDownload}" default-value=true
	 */
	private boolean				forceDownload;

	private static enum Protocol {
		FTP, HTTP
	};

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
		URL url = null;
		try {
			url = new URL(IMDB_INTERFACES_URL);
			FileUtils.copyURLToFile(url, new File(getMoviesDirectory(), "movies.txt"));
		}
		catch (IOException e) {
			fail("Error while downloading IMDB movies files from {0}", url);
		}
	}

	/**
	 * Checks existence of the directory where the movies will be downloaded. If it doesn't exist, the method will create one.
	 * 
	 * @return A safe directory location to download movies
	 * @throws MojoExecutionException
	 */
	private File getMoviesDirectory() throws MojoExecutionException {
		if (getLog().isInfoEnabled()) {
			getLog().info(
					MessageFormat
							.format("Movies files will be downloaded in the directory {0}. Feel free to define a specific location to receive movies files. Usage : mvn movies:download -Dmovies.directory='...'",
									moviesDirectory.getAbsoluteFile()));
		}
		if (!moviesDirectory.exists()) {
			if (moviesDirectory.getParentFile().canWrite()) {
				moviesDirectory.mkdir();
				getLog().info(MessageFormat.format("Directory {0} has been created in order to receive movies files", moviesDirectory.getAbsoluteFile()));
			}
			else {
				fail("It sounds like directory {0} cannot be accessed. Please check your permission rights on this directory or change the directory using mvn movies:download -Dmovies.directory='...' command",
						moviesDirectory.getParentFile());
			}
		}
		else if (!moviesDirectory.isDirectory()) {
			fail("Attempt to download movies within a file instead of a directory. Please turn {0} into a directory or change its location using mvn movies:download -Dmovies.directory='...' command ",
					moviesDirectory.getAbsoluteFile());
		}
		return moviesDirectory;
	}

	private void fail(String message, Object... params) throws MojoExecutionException {
		throw new MojoExecutionException(MessageFormat.format(message, params));
	}
}
