def lintchecks() {
    sh "echo Installing JSlist"
    sh "npm i jslint"
    sh "echo Starting lintchecks for ${Component}"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo lintchecks completed for ${Component}"
}

def call() {
    pipeline {
        agent any
        environment {
            SONAR_URL = "172.31.45.126"
            SONAR_CRED = credentials('SONAR_CRED')
             
        }
        stages {
            stage('lint checks') {
                steps {
                    script {
                        lintchecks()
                    }
                    
                }
            }
            stage("Generating Artifacts") {
                steps {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                }
            }
        stage ('Sonar Checks') {
            steps {
                sh "env"
                sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=cata${Component} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PWD}"
            }
        }
        }
    }
}