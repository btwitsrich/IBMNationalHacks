pipeline {
    agent any

    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/btwitsrich/IBMNationalHacks.git'
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

        // ✅ OWASP stage must be *inside* the `stages` block
        stage('OWASP Dependency-Check') {
            steps {
                bat '''
                    "C:\\Tools\\dependency-check\\bin\\dependency-check.bat" ^
                    --project "IBMNationalHacks" ^
                    --scan "." ^
                    --format "HTML" ^
                    --out "dependency-check-report"
                '''
            }
        }
    }

    post {
        success {
            echo 'Build and tests completed successfully.'
            
             publishHTML(target: [
                reportDir: 'dependency-check-report',
                reportFiles: 'dependency-check-report.html',
                reportName: 'OWASP Dependency-Check Report',
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true
            ])
        }
        failure {
            echo 'Something went wrong with the build.'
        }
    }
}
