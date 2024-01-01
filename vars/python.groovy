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
                    sh "echo starting Sonar Checks for ${Component}"
                    sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${Component} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}"
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
                    sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${Component}"
                    sh "echo Sonar Checks for ${Component} completed"
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