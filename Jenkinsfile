pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/btwitsrich/IBMNationalHacks.git'
            }
        }
        stage("Sonarqube Analysis") {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('OWASP-ZAP') {
            steps {
                // sh 'zap.sh -cmd -quickurl "http://localhost:8080" -quickprogress -scanners 40018'
                // sh 'zap.sh -cmd -quickurl "http://localhost:8080" -quickprogress'
                // sh 'zap.sh -cmd -quickurl "http://localhost:8080" -quickprogress -scanners 40018,50000,50001'
                sh '/opt/zap/zap.sh -cmd -quickurl http://172.17.0.1:8080 -quickprogress -scanners 40018,50000,50001'
            }
        }
        stage("Docker Build & Push") {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh 'docker build -t btwitsrich/ibm-national-hacks:latest .'
                        sh 'docker push btwitsrich/ibm-national-hacks:latest'
                    }
                }
            }
        }
        stage("Deploy to Kubernetes") {
            steps {
                script {
                    withKubeConfig(credentialsId: 'kubeconfig') {
                        sh 'kubectl apply -f deployment.yml'
                        sh 'kubectl apply -f service.yml'
                    }
                }
            }
        }
        stage("TRIVY FS Scan") {
            steps {
                sh 'trivy fs .'
            }
        }
        stage("TRIVY Image Scan") {
            steps {
                sh 'trivy image btwitsrich/ibm-national-hacks:latest'
            }
        }
        stage("Kube-Bench") {
            steps {
                sh 'kube-bench'
            }
        }
        stage("Kubescape Scan") {
            steps {
                sh 'kubescape scan'
            }
        }
    }
    post {
        always {
            echo 'Pipeline finished.'
            junit 'target/surefire-reports/*.xml'
            // mail to: 'recipient@example.com',
            //      subject: "Jenkins Build ${currentBuild.fullDisplayName}",
            //      body: "Check console output at ${env.BUILD_URL} to view the results."
            slackSend (channel: '#general', message: "Build ${currentBuild.fullDisplayName} finished with status: ${currentBuild.currentResult}. Check console output at ${env.BUILD_URL}")
        }
        success {
            echo 'Pipeline succeeded.'
        }
        failure {
            echo 'Pipeline failed.'
        }
        unstable {
            echo 'Pipeline is unstable.'
        }
    }
    environment {
        //CI = 'true'
    }
} // <-- This was the missing closing brace
