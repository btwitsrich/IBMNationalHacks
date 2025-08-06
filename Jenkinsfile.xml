pipeline {
    agent any

    tools {
        maven 'Maven 3.8.8'  // Use the name of the Maven installation you configured in Jenkins
        jdk 'JDK 17'         // Use the JDK name installed/configured in Jenkins
    }

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
