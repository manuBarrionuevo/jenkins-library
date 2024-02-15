def call() {
    def pinVars = [:]

    pinVars.buildDockerImage = { imageName, version ->
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
    dockerBuildDeploy.dockerLogin = { registryUrl ->
        withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) { credentials ->
            env.DOCKER_USER = credentials.username
            env.DOCKER_PASSWORD = credentials.password

            withDockerRegistry([url: registryUrl]) {
                return true
            }
        }
        return false
    }

    return pinVars
}
