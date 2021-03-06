package com.tocchisu.movies.plugins;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class DownloadMojoTest {

	@Test
	public void executeTest() throws Exception {
		DownloadMojo downloadMojo = spy(new DownloadMojo());
		String wrongProtocol = "wrongProtocol";
		Whitebox.setInternalState(downloadMojo, "protocol", wrongProtocol);
		try {
			downloadMojo.execute();
			fail("IllegalArgumentException should have been thrown while executing mojo with an protocol " + wrongProtocol);
		}
		catch (IllegalArgumentException e) {}
	}
}
