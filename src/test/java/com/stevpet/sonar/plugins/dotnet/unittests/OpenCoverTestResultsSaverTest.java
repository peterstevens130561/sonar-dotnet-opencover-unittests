package com.stevpet.sonar.plugins.dotnet.unittests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

public class OpenCoverTestResultsSaverTest {

	@Mock private PathResolver pathResolver;
	@Mock private FileSystem fileSystem;

	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void instantion() {
		try {
			new OpenCoverTestResultsSaver(pathResolver,fileSystem);
		} catch (Exception e) {
			fail("could not instantiate");
		}
	}
}
