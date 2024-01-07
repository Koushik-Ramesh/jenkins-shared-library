def call() {
    node {
        common.lintchecks()
        common.sonarChecks()
        common.testCases()
    }
}

/* Declarative pipeline
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
            stage("Code Compile") {
                steps {
                    sh "echo Generating Artifacts for $Component "
                    sh "mvn clean compile"
                }
            }
            stage ('Sonar Checks') {
                steps {
                    script {
                        env.ARGS="-Dsonar.java.binaries=target/"
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
            stage('Generating Artifacts') {
                steps {
                    sh "echo Artifact complete"
                }

            }
                
        } 
    }
}
*/

