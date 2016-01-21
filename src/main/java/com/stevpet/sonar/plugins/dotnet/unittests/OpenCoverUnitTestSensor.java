/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.unittests;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.OpenCoverCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpeFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaverBase;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * ProjectBuilder for dotnet projects
 * 
 * The build method will be invoked by sonar in the ProjectBuild phase, and
 * populates the MicrosoftWindowsEnvironment
 * 
 * @author stevpet
 * 
 */
public class OpenCoverUnitTestSensor implements Sensor {

	private MsCoverConfiguration configuration;
	private TestCache cache;
	private TestRunner testRunner;
	private FileSystem fileSystem;
	private CoverageReader reader;
	private CoverageSaver coverageSaver;
	private TestResultsBuilder testResultsBuilder;
	private VsTestTestResultsSaverBase testResultsSaver;
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

	/**
	 * Includes all dependencies
	 * 
	 * @param fileSystem
	 * @param configuration
	 * @param unitTestBatchData
	 * @param testRunner
	 * @param testResultsBuilder
	 * @param testResultsSaver
	 * @param coverageReader
	 * @param coverageSaver
	 * @param microsoftWindowsEnvironment
	 */
	public OpenCoverUnitTestSensor(FileSystem fileSystem,
			MsCoverConfiguration configuration,
			TestCache unitTestBatchData, TestRunner testRunner,
			TestResultsBuilder testResultsBuilder,
			VsTestTestResultsSaverBase testResultsSaver,
			CoverageReader coverageReader, CoverageSaver coverageSaver,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
		this.fileSystem = fileSystem;
		this.configuration = configuration;
		this.cache = unitTestBatchData;
		this.testRunner = testRunner;
		this.testResultsBuilder = testResultsBuilder;
		this.testResultsSaver = testResultsSaver;
		this.reader = coverageReader;
		this.coverageSaver = coverageSaver;
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		;
	}

	/**
	 * For export in plugin, uses the standard defauls
	 * @param fileSystem
	 * @param msCoverConfiguration
	 * @param unitTestBatchData
	 * @param coverageSaver
	 * @param microsoftWindowsEnvironment
	 * @param pathResolver
	 */
	public OpenCoverUnitTestSensor(FileSystem fileSystem,
			MsCoverConfiguration msCoverConfiguration,
			TestCache unitTestBatchData, 
			CoverageSaver coverageSaver,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment, PathResolver pathResolver) {
		this(fileSystem, msCoverConfiguration, unitTestBatchData, 
				DefaultOpenCoverTestRunner.create(msCoverConfiguration, microsoftWindowsEnvironment, fileSystem),
				new SpeFlowTestResultsBuilder(microsoftWindowsEnvironment), 
				new VsTestTestResultsSaver(pathResolver,fileSystem), 
				new OpenCoverCoverageReader(msCoverConfiguration), 
				new OpenCoverCoverageSaver(microsoftWindowsEnvironment,pathResolver,fileSystem),
				microsoftWindowsEnvironment);
	}
	private static final Logger LOG = LoggerFactory
			.getLogger(OpenCoverUnitTestSensor.class);

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return project.isModule() && configuration.runOpenCover()
				&& microsoftWindowsEnvironment.hasUnitTestSourceFiles();
	}

	@Override
	public void analyse(Project module, SensorContext context) {
		File testResultsFile;
		File coverageFile;
		SonarCoverage sonarCoverage;
		if (!cache.gatHasRun()) {
			coverageFile = new File(fileSystem.workDir(), "coverage.xml");
			testRunner.setCoverageFile(coverageFile);
			testRunner.execute();
			testResultsFile = testRunner.getTestResultsFile();

			sonarCoverage = new SonarCoverage();
			reader.read(sonarCoverage, coverageFile);
			cache.setSonarCoverage(sonarCoverage);
			cache.setHasRun(coverageFile, testResultsFile);
		} else {
			sonarCoverage = cache.getSonarCoverage();
			testResultsFile = cache.getTestResults();
			coverageFile = cache.getTestCoverage();
		}

		coverageSaver.save(context, sonarCoverage);

		if (testResultsFile != null) {
			ProjectUnitTestResults testResults = testResultsBuilder.parse(
					testResultsFile, coverageFile);
			testResultsSaver.save(context, testResults);
		}
	}
}