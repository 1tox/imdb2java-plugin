package com.tocchisu.movies.plugins;

import java.io.File;
import java.text.MessageFormat;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which download IMDB interfaces
 * 
 * @goal download
 * 
 * @phase process-sources
 */
public class InterfacesDownloaderMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${movies.interfacesDirectory}" default-value="${project.build.directory}"
	 */
	private File	interfacesDirectory;
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${movies.forceDownload}" default-value=true
	 */
	private boolean	forceDownload;

	public void execute() throws MojoExecutionException {
		if (getLog().isWarnEnabled()) {
			getLog().warn(
					MessageFormat
							.format("Movies files are downloaded in the default directory {0}. It is better to define a specific location to receive interfaces files.",
									interfacesDirectory.getAbsoluteFile()));
		}
		Object object = getPluginContext().get("project.build.directory");
		getLog().info(object.toString());
	}
}
