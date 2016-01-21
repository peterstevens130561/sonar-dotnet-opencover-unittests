package com.stevpet.sonar.plugins.dotnet.unittests;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.OpenCoverCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpeFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverUnitTestSensor extends OpenCoverSensorBase {

	public OpenCoverUnitTestSensor(FileSystem fileSystem,
			MsCoverConfiguration msCoverConfiguration,
			TestCache unitTestBatchData, 
			CoverageSaver coverageSaver,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment, PathResolver pathResolver) {
		super(fileSystem, msCoverConfiguration, unitTestBatchData, 
				new OpenCoverCoverageRunner(msCoverConfiguration, microsoftWindowsEnvironment, fileSystem),
				new SpeFlowTestResultsBuilder(microsoftWindowsEnvironment), 
				new VsTestTestResultsSaver(pathResolver,fileSystem), 
				new OpenCoverCoverageReader(msCoverConfiguration), 
				new OpenCoverCoverageSaver(microsoftWindowsEnvironment,pathResolver,fileSystem),
				microsoftWindowsEnvironment);

	}

}
