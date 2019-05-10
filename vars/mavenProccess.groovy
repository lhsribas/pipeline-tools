#!/usr/bin/env groovy

/*
 *
 */
def execCleanProject(def _maven, def _settings)
{
    echo ">>>> maven ::: ${_maven} >>>>> maven_settings:::  ${_settings}"
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") 
    {
        sh "mvn clean"
    }
}

/*
 *
 */
def execCompileProject(def _maven, def _settings)
{
    echo ">>>> maven ::: ${_maven} >>>>> maven_settings:::  ${_settings}"
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") 
    {
        sh "mvn compile -DskipTests"
    }
} 

/*
 *
 */
def execTestProject(def _maven, def _settings)
{
    echo ">>>> maven ::: ${_maven} >>>>> maven_settings:::  ${_settings}"
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") 
    {
        sh "mvn test"
    }
}

/*
 *
 */
def execPackageProject(def _maven, def _settings)
{
     echo ">>>> maven ::: ${_maven} >>>>> maven_settings:::  ${_settings}"
    withMaven( maven: "${_maven}", mavenSettingsConfig: "${_settings}") 
    {
        sh "mvn package -DskipTests"
    }
}                           