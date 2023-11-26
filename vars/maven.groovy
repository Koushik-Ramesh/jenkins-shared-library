def lintchecks() {
    sh "echo Starting lintchecks for ${Component}"
    sh "mvn checkstyle:check || true"
    sh "echo lintchecks completed for ${Component}"
}

def call() {
    pipeline {
        agent any
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
                    sh "echo Generating Artifacts for $Component "
                    sh "mvn clean package"
                }
            }
        }
    }
}