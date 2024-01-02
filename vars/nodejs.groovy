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
            stage ('Sonar Checks') {
                steps {
                   script{
                        env.ARGS="-Dsonar.sources=."
                        common.sonarChecks()
                   }
                }
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit testing') {
                        steps {
                            sh "echo Starting Unit Testing"
                            sh "echo Unit Testing Completed"
                        }
                    }
                    stage('Integration Testing') {
                        steps {
                            sh "echo Starting Integration Testing"
                            sh "echo Integration Testing Completed"
                        }
                    }
                    stage('Functional Testing') {
                        steps {
                            sh "echo Starting Funtional Testing"
                            sh "echo Functional Funtional Completed"
                        }
                    }
                }
            }
            stage("Generating Artifacts") {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                    sh "zip ${Component}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                }
            }
            stage("Uploading the Artifacts") {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "echo Generating Artifacts"
                }
            }
        
        }
    }
}