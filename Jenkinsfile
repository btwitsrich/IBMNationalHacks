pipeline {
    agent any

    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/btwitsrich/IBMNationalHacks.git', branch: 'master'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('OWASP Dependency-Check') {
            steps {
                bat '''
                    cd C:\\OWASP\\dependency-check
                    dependency-check.bat --project "IBMNationalHacks" --scan "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\Maven Jenkins Pipeline@2" --format HTML --out "C:\\OWASP\\dependency-check\\report"
                '''
            }
        }
    }

    post {
        success {
            echo 'Build and security checks passed.'
        }
        failure {
            echo 'Something went wrong with the build or scan.'
        }
    }
}
