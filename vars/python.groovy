def lintchecks() {
    sh "echo Starting lintchecks for ${Component}"
    sh "pylint *.py || true"
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
                        common.sonarChecks()
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