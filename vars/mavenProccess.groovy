#!/usr/bin/env groovy
/*
 *
 */
def build(def _maven, def _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn compile -DskipTests"
    }
} 

/*
 *
 */
def test(def _maven, def _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn test"
    }
}

/*
 *
 */
def package(def _maven, def _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn package -DskipTests"
    }
}

/*
 *
 */
def clean(def _maven, def _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn clean"
    }
}
