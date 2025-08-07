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

        stage('OWASP Dependency-Check') {
            steps {
                bat '''
                    "C:\\Tools\\dependency-check\\bin\\dependency-check.bat" ^
                    --project "IBMNationalHacks" ^
                    --scan "." ^
                    --format "HTML" --format "JSON" ^
                    --out "dependency-check-report"
                '''
            }
        }

        stage('Customize Report') {
            steps {
                writeFile file: 'dependency-check-report/custom-style.html', text: '''(your full custom HTML goes here...)'''
                bat 'type dependency-check-report\\custom-style.html >> dependency-check-report\\dependency-check-report.html'
            }
        }

        stage('Install Python (if not present)') {
            steps {
                powershell '''
                $ErrorActionPreference = "Stop"

                # Define installer URL
                $pythonInstallerUrl = "https://www.python.org/ftp/python/3.12.2/python-3.12.2-amd64.exe"
                $installerPath = "$env:TEMP\\python-installer.exe"

                # Download installer
                Invoke-WebRequest -Uri $pythonInstallerUrl -OutFile $installerPath

                # Install silently and add to PATH
                Start-Process -FilePath $installerPath -ArgumentList "/quiet InstallAllUsers=1 PrependPath=1" -Wait

                # Verify installation
                python --version
                '''
            }
        }

        stage('Convert Report to CSV') {
            steps {
                writeFile file: 'parse_json_to_csv.py', text: '''
import json
import csv

with open("dependency-check-report/dependency-check-report.json", "r") as f:
    data = json.load(f)

rows = []

for dependency in data.get("dependencies", []):
    fileName = dependency.get("fileName", "")
    filePath = dependency.get("filePath", "")
    for evidence in dependency.get("evidenceCollected", []):
        evidenceType = evidence.get("type", "")
        for item in evidence.get("evidence", []):
            rows.append([
                fileName,
                filePath,
                evidenceType,
                item.get("source", ""),
                item.get("name", ""),
                item.get("value", ""),
                item.get("confidence", "")
            ])

with open("dependency-check-report/dependency-evidence.csv", "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(["File Name", "File Path", "Evidence Type", "Source", "Name", "Value", "Confidence"])
    writer.writerows(rows)
                '''
                bat 'python parse_json_to_csv.py'
            }
        }
    }

    post {
        success {
            echo '✅ Build and tests completed successfully.'

            publishHTML(target: [
                reportDir: 'dependency-check-report',
                reportFiles: 'dependency-check-report.html',
                reportName: 'OWASP Dependency-Check Report',
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true
            ])

            archiveArtifacts artifacts: 'dependency-check-report/dependency-evidence.csv', fingerprint: true
        }

        failure {
            echo '❌ Something went wrong with the build.'
        }
    }
}
