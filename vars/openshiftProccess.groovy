#!/usr/bin/env groovy

/**
 * This method is reponsible to create a BuildConfig based in a image of Red Hat.
 */
def execCreateImageBuilder(def _Cluster, def _Project, def _Application, def _ImageStream)
{
    openshift.withCluster("${_Cluster}") 
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            if (!openshift.selector("bc", "${_Application}").exists()) 
            {
                openshift.newBuild("--name=${_Application}", "--image-stream=${_ImageStream}", "--binary")
            }
        }
    }
}

/**
 * This method is responsible to copy the artefact do inside of BuildConfig and generates an image
 */
def execStartBuild(def _Cluster, def _Project, def _Application, def _Version, def _Path)
{
    openshift.withCluster("${_Cluster}") ß
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            openshift.selector("bc", "${_Application}").startBuild("--from-file=${_Path}/${_Application}-${_Version}.jar", "--wait")
        }
    }
}

/**
 * This method is responsible to create a tag of an image
 */
def execTagImage(def _Cluster, def _Project, def _Application, def _Version, def _Tag, def _Environment)
{
    openshift.withCluster("${_Cluster}") 
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            openshift.tag("${_Application}:${_Tag}", 
                          "${_Application}:${_Environment}-${_Version}")
        }
    }
}

/**
 * This method is responsible to verify if exists the DeploymentConfig
 */
def vefirifyExistsDeploymentConfig(def _Cluster, def _Project, def _Application)
{
    openshift.withCluster("${_Cluster}") 
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            return !openshift.selector('dc', "${_Application}").exists()
        }
    }
}

/**
 * This method is reponsible to create a DeploymentConfig
 */
def createDeploymentConfig(def _Cluster, def _Project, def _Application, def _Template, def _TemplateParams)
{
    openshift.withCluster("${_Cluster}") 
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            openshift.newApp("${_Template}", "--name=${_Application}", "${_TemplateParams}")
            return false
        }
    }
}

/**
 * This method is responsible to execute the rollout of the image in the DeploymentConfig
 */
def rolloutVersion(def _Cluster, def _Project, def _Application, def _Environment, def _Version)
{
    openshift.withCluster("${_Cluster}")
    {
        openshift.withProject("${_Project}") 
        {
            echo "Using project ::: ${openshift.project()}"
            // Getting the deploymentConfig
            def deploymentConfig = openshift.selector("dc", "${_Application}")
            // Convert deploymentConfig to object and return it
            def dcObject = deploymentConfig.object()
            //Change the tag of image into the deploymentConfig
            dcObject.spec.triggers[0].imageChangeParams.from.name="${_Application}:${_Environment}-${_Version}"
            //Print the new value of tag image
            echo "${dcObject.spec.triggers[0].imageChangeParams.from.name}"
            
            openshift.apply(dcObject)
        }
    }
}