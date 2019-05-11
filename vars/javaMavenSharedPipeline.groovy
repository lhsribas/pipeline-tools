#!/usr/bin/env groovy

import hudson.FilePath

/**
 * javaMavenSharedPipeline.groovy
 *
 */

 /**
  * pipelineParams 
  * {
  *     jenkinsPodLabel:
  *     maven:
  *     mavenSettingsConfig:
  *     cluster:
  *     project:
  *     application:
  *     imageStream:
  *     sonarEnv: Sonar 6.4
  *     environment:
  *     template:
  *     templateParams:
  *     tag:
  *     gitUrl
  *     gitBranch
  *     gitCredentials
  * }
  */
def call(body) {

        def config = [:]

        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        def runnerSonar = (config.sonarEnv != "") ? false : true
        def rollout = true

        /*
         * Controls the version of image
         */
        def version = null
        
        /*
         * Controls the name of  Application
         */
        def artifactId = null

    pipeline {

        agent 
        {
            node 
            {      
                label "${config.jenkinsPodLabel}"
            }
        }

        stages 
        {
            stage("Initialize")
            {
                steps
                {
                    script
                    {
                        echo "Git -> branch: ${config.gitBranch}, url: ${config.gitUrl}"
                        git branch: "${config.gitBranch}", url: "${config.gitUrl}"

                        version = pomProccess.getVersionFromPom()
                        artifactId = pomProccess.getArtifactIdFromPom()
                    }
                }
            }

            stage("Clean Project")
            {
                steps
                {
                    script
                    {
                        mavenProccess.execCleanProject("${config.maven}", "${config.mavenSettingsConfig}")
                    }
                }
            }

            stage("Test Project")
            {
                steps
                {
                    script
                    {
                        mavenProccess.execTestProject("${config.maven}", "${config.mavenSettingsConfig}")
                    }
                }
            }

            stage("Compile Project")
            {
                steps
                {
                    script
                    {
                        mavenProccess.execCompileProject("${config.maven}", "${config.mavenSettingsConfig}")
                    }
                }
            }

            stage("Package Project")
            {
                steps
                {
                    script
                    {
                        mavenProccess.execPackageProject("${config.maven}", "${config.mavenSettingsConfig}")
                    }
                }
            }

            if(runnerSonar)
            {
                stage('Sonar')
                {
                    steps
                    {
                        script
                        {
                            mavenProccess.execSonarQualityGate("${config.maven}", "${config.mavenSettingsConfig}", "${config.sonarEnv}")
                        }
                    }
                }

                stage('Quality Gate') 
                {
                    steps 
                    {
                        script{
                            timeout(time: 5, unit: 'MINUTES') 
                            {
                                def _qg = sonarQualityGatesProccess.qualityGateResults("target/sonar/report-task.txt")
                                
                                if (_qg.status != 'OK') 
                                {
                                    error "Please review the quality of your code.\nstatus of analisys: ${qg.status}"
                                }
                            }	
                        }
                    }
                }
            } 
            else 
            {
                info "Sonar quality gate is disabled"
            }
        }
    }
}