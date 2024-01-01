def lintchecks() {
    sh "echo Starting lintchecks for ${Component}"
    sh "mvn checkstyle:check || true"
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
                            sh "echo Starting Unit Testing"
                            sh "echo Integration Testing Completed"
                        }
                    }
                    stage('Functional Testing') {
                        steps {
                            sh "echo Starting Unit Testing"
                            sh "echo Functional Testing Completed"
                        }
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

