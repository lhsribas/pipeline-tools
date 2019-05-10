#!/usr/bin/env groovy

import hudson.FilePath

/**
 * sharedPipeline.groovy
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

        def jenkinsPodLabel = config.jenkinsPodLabel
        def gitUrl = config.gitUrl
        def gitBranch = config.gitBranch
        //def runnerSonar = (pipelineParams.sonarEnv != "") ? false : true
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

            stage("example")
            {
                steps
                {
                    script
                    {
                        mavenProccess.clean()
                    }
                }
            }

        }
    }
}