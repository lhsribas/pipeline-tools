#!/usr/bin/env groovy


/*
 *
 */
def execCleanProject(def _maven, def _settings){
    //withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") {
      //  sh "mvn clean"
    //}

    echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ${_maven} >>>>>>>>>> ${_settings}"
}
