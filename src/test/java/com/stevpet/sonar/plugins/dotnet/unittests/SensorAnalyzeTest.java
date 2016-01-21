package com.stevpet.sonar.plugins.dotnet.unittests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaverBase;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SensorAnalyzeTest {

	@Mock private FileSystem fileSystem ;
	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private TestRunner runner;
	private OpenCoverUnitTestSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext context;
	@Mock private TestResultsBuilder testResultsBuilder;
	@Mock private VsTestTestResultsSaverBase testResultsSaver;
	@Mock private CoverageReader coverageReader;
	@Mock private CoverageSaver coverageSaver;
	@Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);	

		sensor = new OpenCoverUnitTestSensor(fileSystem,configuration,cache,runner, 
				testResultsBuilder, testResultsSaver,coverageReader,coverageSaver,microsoftWindowsEnvironment);
	}
	
	@Test
	public void test() {
		when(fileSystem.workDir()).thenReturn(new File("workdir"));
		when(runner.getTestResultsFile()).thenReturn(new File("testresults"));
		when(cache.getTestCoverage()).thenReturn(new File("coverage"));
		when(cache.getTestResults()).thenReturn(new File("testResults"));
		sensor.analyse(module, context);
	}
}
