#!/usr/bin/env groovy

/*
 *
 */
def execCleanProject(def _maven, def _settings){
    echo ">>>> maven ::: ${_maven} >>>>> maven_settings:::  ${_settings}"
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn clean"
    }
}
