#!/usr/bin/env groovy

/*
 * This method get the ArtifactId of application developed in java, is necessary to install a pluguin called Pipeline Basic Steps on Jenkins.
 */
def getArtifactIdFromPom() {
  readMavenPom().getArtifactId()
}

/*
 * This method get the GroupId of application developed in java, is necessary to install a pluguin called Pipeline Basic Steps on Jenkins.
 */
def getGroupIdFromPom() {
  if(readMavenPom().getParent().getGroupId() != readMavenPom().getGroupId()){
    readMavenPom().getGroupId()
  } else {
    readMavenPom().getParent().getGroupId()
  }
}

/*
 * This method get the Version of application developed in java, is necessary to install a pluguin called Pipeline Basic Steps on Jenkins.
 */
def getVersionFromPom() {
  readMavenPom().getVersion()
}