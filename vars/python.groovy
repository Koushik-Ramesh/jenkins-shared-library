def lintchecks() {
    sh "echo Starting lintchecks for ${Component}"
    sh "pylint *.py || true"
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
           
        }
    }
}