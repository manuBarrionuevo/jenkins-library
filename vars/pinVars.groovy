def call() {
    def pinVars = [:]

    pinVars.buildDockerImage = { imageName, version, directory ->
        dir(directory) {
            sh """
                docker build -t $imageName:$version .
                docker images
            """
        }
    }

    pinVars.pushDockerImage = { imageName, version, directory ->
        sh """
            docker push $imageName:$version
        """
    }

    pinVars.dockerLogin = { registryUrl ->
        withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
            withDockerRegistry([url: registryUrl]) {
               return true
            }
        }
        return false
    }

    return pinVars
}
