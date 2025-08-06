pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/btwitsrich/your-repo.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }

    post {
        success {
            echo 'Build and tests completed successfully.'
        }
        failure {
            echo 'Something went wrong with the build.'
        }
    }
}
