#!/usr/bin/env groovy


/*
 *
 */
def clean(def _maven, def _settings){
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
        sh "mvn clean"
    }
}
