package com.tocchisu.movies.plugins;

import java.io.File;
import java.text.MessageFormat;
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
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${movies.directory}" default-value="${basedir'/src/main/resources/interfaces}"
	 */
	private File	moviesDirectory;
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${movies.forceDownload}" default-value=true
	 */
	private boolean	forceDownload;

	public void execute() throws MojoExecutionException {
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
			fail("Attempt to download movies within a file instead of a directory. Please turn {0} into a directory or change this location using mvn movies:download -Dmovies.directory='...' command ",
					moviesDirectory.getAbsoluteFile());
		}
	}

	private void fail(String message, Object... params) throws MojoExecutionException {
		throw new MojoExecutionException(MessageFormat.format(message, params));
	}
}
