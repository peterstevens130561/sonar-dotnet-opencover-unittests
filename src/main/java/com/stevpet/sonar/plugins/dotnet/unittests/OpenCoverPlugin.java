package com.stevpet.sonar.plugins.dotnet.unittests;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.OpenCoverCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpeFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultUnitTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleMicrosoftWindowsEnvironment;

public class OpenCoverPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(

        		SimpleMicrosoftWindowsEnvironment.class,
        		DefaultMsCoverConfiguration.class,       		

        		OpenCoverCoverageRunner.class,
        		OpenCoverCoverageReader.class,
        		OpenCoverCoverageSaver.class,
        		SpeFlowTestResultsBuilder.class,
        		OpenCoverTestResultsSaver.class,
                DefaultUnitTestCache.class,
        		
                OpenCoverSensor.class
                );
   
        return exported;
    }

}
