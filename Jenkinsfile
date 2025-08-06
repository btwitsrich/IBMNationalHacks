pipeline {
    agent any

    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/btwitsrich/IBMNationalHacks.git',
                    branch: 'master',
                    credentialsId: 'github-pat' // Use the actual ID from Jenkins Credentials
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
