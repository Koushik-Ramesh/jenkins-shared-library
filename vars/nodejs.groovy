def call() {
    node {
        common.lintchecks()
        env.ARGS="-Dsonar.sources=."
        env.NEXUS_URL="172.31.37.89"
        common.sonarChecks()
        common.testCases()
        common.artifacts()
    }
}

/* Declarative pipeline
def call() {
    pipeline {
        agent any
        environment {
            SONAR_URL = "172.31.45.126"
            NEXUS_URL = "172.31.37.89"
            SONAR_CRED = credentials('SONAR_CRED')
            NEXUS_CRED = credentials('NEXUS_CRED')
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
            stage('Check the release') {
                when {
                    expression { env.TAG_NAME !=null }
                }
                steps { 
                    script {
                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${Component}/ | grep ${Component}-${TAG_NAME}.zip || true")
                        print UPLOAD_STATUS
                    }
                }
            }
            stage("Generating Artifacts") {
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
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
                    sh "echo Uploading ${Component} artifact to NEXUS"
                    sh "curl -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${Component}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${Component}/${Component}-${TAG_NAME}.zip"
                    sh "echo Uploading ${Component} artifact to NEXUS is completed"
                }
            }
        }
    }
}
*/