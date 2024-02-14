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
    pinVars.dockerLogin = { registryUrl ->
        withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
            echo "Intentando login en Docker Hub con URL: ${registryUrl}"
            try {
                withDockerRegistry([url: registryUrl]) {
                    echo 'Inicio de sesión exitoso en Docker Hub'
                    return 'success'
                }
        } catch (Exception e) {
                echo "Fallo en el inicio de sesión en Docker Hub: ${e.message}"
                return 'failure'
            }
        }
    }

    return pinVars
}
