#!/usr/bin/env groovy

/**
 * mavenProcess.groovy
 *
 */

/*
 *
 */
def call(Map methodParams){
    buildStage(methodParams)
    testStage(methodParams)
    packageStage(methodParams)
}

/*
 *
 */
def buildStage(Map methodParams){
    stage('Compile'){
        steps{    
            withMaven( maven: "${methodParams.maven}", mavenSettingsConfig: "${methodParams.mavenSettingsConfig}") {
                sh "mvn compile -DskipTests"
            }
        }
    }
} 

/*
 *
 */
def testStage(Map methodParams){
    stage('Test') {
        steps {
            withMaven( maven: "${methodParams.maven}", mavenSettingsConfig: "${methodParams.mavenSettingsConfig}") {
                sh "mvn test"
            }
        }
    }
}

/*
 *
 */
def packageStage(Map methodParams){
    stage('Package') {
        steps {
            withMaven( maven: "${methodParams.maven}", mavenSettingsConfig: "${methodParams.mavenSettingsConfig}") {
                sh "mvn package -DskipTests"
            }
        }
    }
}