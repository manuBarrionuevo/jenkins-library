// DockerBuildDeploy.groovy

def buildDockerImage(imageName, version, directory) {
    dir(directory) {
        sh """
            docker build -t $imageName:$version .
        """
    }
}

def pushDockerImage(imageName, version, directory) {
    sh """
        docker push $imageName:$version
    """
}

// DockerBuildDeploy.groovy

def dockerLogin(registryUrl) {
    withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
        withDockerRegistry([url: registryUrl]) {
            return true
        }
    }
    return false
}

// validate dir

def validateDirectories(directoryList) {
    directoryList.each { directory ->
        if (!fileExists(directory)) {
            error "El directorio '${directory}' no existe."
        }
    }
}

def fileExists(path) {
    def file = new File(path)
    return file.exists()
}