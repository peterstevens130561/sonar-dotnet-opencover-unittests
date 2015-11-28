package com.stevpet.sonar.plugins.dotnet.unittests;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;

public class OpenCoverTestResultsSaver extends OpenCoverTestResultsSaverBase
		implements BatchExtension {

	public OpenCoverTestResultsSaver(
			PathResolver pathResolver, FileSystem filesystem) {
		super(
			new DefaultResourceResolver(pathResolver,filesystem), 
			new DefaultTestResultsFormatter()
		);
	}

}
