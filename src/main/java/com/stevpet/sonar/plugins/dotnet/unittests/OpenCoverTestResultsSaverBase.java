package com.stevpet.sonar.plugins.dotnet.unittests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;

public class OpenCoverTestResultsSaverBase implements BatchExtension{
    private static final Logger LOG = LoggerFactory
            .getLogger(OpenCoverTestResultsSaverBase.class);
    TestResultsSaver testResultsSaver;
    SensorContext sensorContext;
    private TestResultsFormatter testResultsFormatter;
    private ResourceResolver resourceResolver;

    @SuppressWarnings("ucd")
    public OpenCoverTestResultsSaverBase(
            DefaultResourceResolver resourceResolver,
            TestResultsFormatter testResultsFormatter) {

        this.testResultsFormatter = testResultsFormatter;
        this.resourceResolver = resourceResolver;
    }

    public void save(SensorContext sensorContext,ProjectUnitTestResults projectUnitTestResults) {
        this.sensorContext=sensorContext;
    	for (ClassUnitTestResult classUnitTestResult : projectUnitTestResults
                .values()) {
            java.io.File file = classUnitTestResult.getFile();
            if (file == null) {
                continue;
            }
            File sonarFile = resourceResolver.getFile(file);
            if (sonarFile == null) {
                continue;
            }
            saveFileSummaryResults(classUnitTestResult, sonarFile);
            saveFileTestResults(classUnitTestResult, sonarFile);
        }
    }

    public void saveFileSummaryResults(ClassUnitTestResult fileResults,
            File sonarFile) {
        sensorContext.saveMeasure(sonarFile, CoreMetrics.SKIPPED_TESTS,
                fileResults.getIgnored());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_ERRORS,
                (double) 0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_SUCCESS_DENSITY,
                fileResults.getDensity() * 100.0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_FAILURES,
                fileResults.getFail());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_EXECUTION_TIME,
                1000.0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TESTS,
                fileResults.getTests());
    }

    public void saveFileTestResults(ClassUnitTestResult fileResults,
            File sonarFile) {
        String data = testResultsFormatter
                .formatClassUnitTestResults(fileResults);
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sensorContext.saveMeasure(sonarFile, testData);
    }

}