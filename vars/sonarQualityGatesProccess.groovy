#!/usr/bin/env groovy
def qualityGateResults(def URLReport) {
	
	def props = readProperties file: URLReport
	SONAR_CE_TASK_URL = props.ceTaskUrl
	SONAR_SERVER_URL = props.serverUrl
	
    waitUntil {
		sh "curl ${SONAR_CE_TASK_URL} -o ceTask.json"
		ceTask = readJSON file: 'ceTask.json'
		echo ceTask.toString()
		return "SUCCESS".equals(ceTask.task.status)
	}

	def qualityGateUrl = "${SONAR_SERVER_URL}" + "/api/qualitygates/project_status?analysisId=" + ceTask.task.analysisId
	def url = new URL(qualityGateUrl)
	def result = new groovy.json.JsonSlurper().parse(url.newReader())
    
	result.projectStatus
}
