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
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SensorAnalyzeTest {

	@Mock private FileSystem fileSystem ;
	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private OpenCoverTestRunner runner;
	private OpenCoverUnitTestSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext context;
	@Mock private TestResultsBuilder testResultsBuilder;
	@Mock private VsTestTestResultsSaver testResultsSaver;
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
		File testResultsFile = new File("testResults");
		File coverageFile = new File("workdir/coverage.xml");
		
		when(fileSystem.workDir()).thenReturn(new File("workdir"));
		when(runner.getTestResultsFile()).thenReturn(testResultsFile);
		when(cache.getTestCoverageFile()).thenReturn(coverageFile);
		when(cache.getTestResultsFile()).thenReturn(new File("testResults"));

		when(testResultsBuilder.parse(testResultsFile, coverageFile)).thenReturn(new ProjectUnitTestResults());
		sensor.analyse(module, context);
	}
}
