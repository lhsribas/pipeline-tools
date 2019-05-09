#!/usr/bin/env groovy

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

        def pipelineParams = [:]

        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = pipelineParams
        body()

        def jenkinsPodLabel = pipelineParams.pipelineParams
        def gitUrl = pipelineParams.gitUrl
        def gitBranch = pipelineParams.gitBranch
        //def runnerSonar = (pipelineParams.sonarEnv != "") ? false : true
        def rollout = true

    pipeline {

        agent 
        {
            node 
            {      
                label "master"
            }
        }

        stages 
        {
            stage("Initialize")
            {
                steps
                {
                    echo "checkout  ${jenkinsPodLabel}"
                    echo ">>>>>>>>  ${gitUrl}"
                    git branch: "${pipelineParams.gitBranch}", url: "${pipelineParams.gitUrl}"
                }
            }

           /* switch(pipelineParams.environment) 
            {
                case "dev":

                    stage('Compile')
                    {
                        steps
                        {    
                            withMaven( maven: "${pipelineParams.maven}", mavenSettingsConfig: "${pipelineParams.mavenSettingsConfig}") 
                            {
                                sh "mvn clean compile -DskipTests"
                            }
                        }
                    }

                    stage('Test') 
                    {
                        steps {
                            withMaven( maven: "${pipelineParams.maven}", mavenSettingsConfig: "${pipelineParams.mavenSettingsConfig}") 
                            {
                                sh "mvn test"
                            }
                        }
                    }
                
                    stage('Package') 
                    {
                        steps {
                            withMaven( maven: "${pipelineParams.maven}", mavenSettingsConfig: "${pipelineParams.mavenSettingsConfig}") 
                            {
                                sh "mvn package -DskipTests"
                            }
                        }
                    }

                    if(runnerSonar){
                        stage('Sonar')
                        {
                            steps
                            {
                                withMaven( maven: "${pipelineParams.maven}", mavenSettingsConfig: "${pipelineParams.mavenSettingsConfig}") 
                                {
                                    withSonarQubeEnv("${pipelineParams.sonarEnv}") 
                                    {
                                        sh 'mvn sonar:sonar'
                                    }
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

                    stage('Create Image Builder') 
                    {
                        steps 
                        {
                            script 
                            {
                                echo "Create Image Builder"
                                openshiftProccess.createImageBuilder(pipelineParams)
                            }
                        }
                    }

                    stage('Start Image Builder') 
                    {
                        steps 
                        {
                            script 
                            {
                                echo "Start Image Builder!"
                                openshiftProccess.startBuild(pipelineParams)
                            }
                        }
                    }

                    stage("Tag Image to ${pipelineParams.environment}") 
                    {
                        steps 
                        {
                            script 
                            {
                                echo "Tag Image!"
                                openshiftProccess.tagImage(pipelineParams)
                            }
                        }
                    }

                    stage("Create ${pipelineParams.environment}") {
                        when
                        {
                            expression 
                            {
                                echo "Verify exists Deployment Config!"
                                return  openshiftProccess.vefirifyExistsDeploymentConfig(pipelineParams);
                            }
                        }
                        
                        steps 
                        {
                            script 
                            {
                                echo "Create Deployment Config if not exists!"
                                rollout = openshiftProccess.createDeploymentConfig(pipelineParams)
                            }
                        }
                    }

                    stage('Rollout Dev')
                    {
                        when
                        {
                            expression 
                            {
                                return rollout
                            }
                        }
                        
                        steps{
                            script {
                                openshiftProccess.rolloutVersion(pipelineParams)
                            }
                        }
                    }

                break;
            }
            */
            
        }
    }
}