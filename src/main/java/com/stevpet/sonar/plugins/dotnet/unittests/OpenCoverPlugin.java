package com.stevpet.sonar.plugins.dotnet.unittests;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReaderBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverBase;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.OpenCoverCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultTestResultsParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowScenarioMethodResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunnerBase;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestBatchData;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleMicrosoftWindowsEnvironment;

public class OpenCoverPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(
        		VsTestEnvironment.class,
        		PathResolver.class,
        		SimpleMicrosoftWindowsEnvironment.class,
        		DefaultMsCoverConfiguration.class,
        		SpecFlowTestResultsBuilder.class,
        		OpenCoverFileNamesParser.class,
        		DefaultResourceResolver.class,
        		DefaultTestResultsParser.class,
        		OpenCoverTestResultsSaver.class,
        		DefaultTestResultsFormatter.class,
                SpecFlowScenarioMethodResolver.class,
                WindowsCodeCoverageCommand.class,
        		OpenCoverCoverageRunner.class,
        		OpenCoverCoverageReader.class,
        		OpenCoverCoverageSaver.class,
                OpenCoverSensor.class,
                UnitTestBatchData.class);
   
        return exported;
    }

}
