// DockerBuildDeploy.groovy

def dockerBuildDeploy = [:]

dockerBuildDeploy.buildDockerImage = { imageName, version, directory ->
    dir(directory) {
        sh """
            docker build -t $imageName:$version .
        """
    }
}

dockerBuildDeploy.pushDockerImage = { imageName, version, directory ->
    sh """
        docker push $imageName:$version
    """
}

dockerBuildDeploy.dockerLogin = { registryUrl ->
    withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
        withDockerRegistry([url: registryUrl]) {
            return true
        }
    }
    return false
}

dockerBuildDeploy.validateDirectories = { directoryList ->
    directoryList.each { directory ->
        if (!fileExists(directory)) {
            error "El directorio '${directory}' no existe."
        }
    }
}

dockerBuildDeploy.fileExists = { path ->
    def file = new File(path)
    return file.exists()
}

return dockerBuildDeploy

