package com.stevpet.sonar.plugins.dotnet.unittests;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.DefaultMicrosoftWindowsEnvironment;

public class OpenCoverPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(
        		DefaultMicrosoftWindowsEnvironment.class,
        		DefaultMsCoverConfiguration.class,       		
                UnitTestCache.class,     		
                OpenCoverUnitTestSensor.class
                );
   
        return exported;
    }

}
