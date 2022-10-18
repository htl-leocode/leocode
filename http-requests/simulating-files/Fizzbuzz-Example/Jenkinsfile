pipeline {
    agent {
        any {
            image 'maven:3-alpine'
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn test --no-transfer-progress'
            }
        }
    }
}
