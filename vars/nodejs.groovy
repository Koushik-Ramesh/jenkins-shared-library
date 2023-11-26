def lintchecks() {
    sh "echo Installing JSlist"
    sh "npm i jslint"
    sh "echo Starting lintchecks..."
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo lintchecks completed...."
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
                    sh "echo Generating Artifacts"
                    sh "npm install"
                }
            }
        }
    }
}