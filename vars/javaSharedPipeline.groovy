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
                    echo "Git -> branch: ${config.gitBranch}, url: ${config.gitUrl}"
                    git branch: "${config.gitBranch}", url: "${config.gitUrl}"
                }
            }

            stage("example")
            {
                steps
                {
                    echo "${pomProccess.getArtifactIdFromPom()}"
                }
            }

        }
    }
}