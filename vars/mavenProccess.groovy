#!/usr/bin/env groovy
/*
 *
 */
def build(String _maven, String _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn compile -DskipTests"
    }
} 

/*
 *
 */
def test(String _maven, String _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn test"
    }
}

/*
 *
 */
def package(String _maven, String _settings){
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
