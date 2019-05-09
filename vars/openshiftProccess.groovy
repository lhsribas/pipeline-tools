#!/usr/bin/env groovy

/**
 * This method
 */
def createImageBuilder(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}") 
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            if (!openshift.selector("bc", "${pipelineParams.application}").exists()) 
            {
                openshift.newBuild("--name=${pipelineParams.application}", "--image-stream=${imageStream}", "--binary")
            }
        }
    }
}

/**
 * This method
 */
def startBuild(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}") 
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            openshift.selector("bc", "${pipelineParams.application}").startBuild("--from-file=target/${artifactId}-${version}.jar", "--wait")
        }
    }
}

/**
 * This method
 */
def tagImage(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}") 
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            openshift.tag("${pipelineParams.application}:${pipelineParams.tag}", "${pipelineParams.application}:${pipelineParams.environment}-${version}")
        }
    }

}

/**
 * This method
 */
def vefirifyExistsDeploymentConfig(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}") 
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            return !openshift.selector('dc', "${pipelineParams.application}").exists()
        }
    }
}

/**
 * This method
 */
def createDeploymentConfig(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}") 
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            openshift.newApp("${pipelineParams.template}", "--name=${pipelineParams.application}", "${pipelineParams.templateParams}")
            return false
        }
    }
}

/**
 * This method
 */
def rolloutVersion(Map pipelineParams)
{
    openshift.withCluster("${pipelineParams.cluster}")
    {
        openshift.withProject("${pipelineParams.project}") 
        {
            echo "Using project: ${openshift.project()}"
            // Getting the deploymentConfig
            def deploymentConfig = openshift.selector("dc", "${pipelineParams.application}")
            
            // Convert deploymentConfig to object and return it
            def dcObject = deploymentConfig.object()
            
            //Change the tag of image into the deploymentConfig
            dcObject.spec.triggers[0].imageChangeParams.from.name="${pipelineParams.application}:${pipelineParams.environment}-${version}"
            
            //Print the new value of tag image
            echo "${dcObject.spec.triggers[0].imageChangeParams.from.name}"
            
            openshift.apply(dcObject)
        }
    }
}