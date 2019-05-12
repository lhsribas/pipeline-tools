#!/usr/bin/env groovy

/*
 * This method is responsible to control the events of sonar
 */
def qualityGateResults(def _timeout, def _URLReport) {
    timeout(time: _timeout, unit: 'MINUTES') 
    {
        def qg = qualityGatesRunner(_URLReport)
        
        if (qg.status != 'OK') {
            error "Fail, Please improve the quality of your code \nAnalisys Status: ${qg.status}"
        }
    }	
}

def qualityGatesRunner(def _URLReport)
{
    def props = readProperties file: _URLReport
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
