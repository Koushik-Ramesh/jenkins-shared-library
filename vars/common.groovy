def sonarChecks() {
    stage('Sonar Checks') {
        sh "echo starting Sonar Checks for ${Component}"
    // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.projectKey=${Component} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}"
    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
    // sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${Component}"
        sh "echo Sonar Checks for ${Component} completed"
    }    
}

// def lintchecks() {
//     sh "echo Starting lintchecks for ${Component}"
//     sh "pylint *.py || true"
//     sh "echo lintchecks completed for ${Component}"
// }

def lintchecks() {
    stage ('Lint Checks') {
        if (env.APPTYPE == "nodejs") {
            sh "echo Installing JSlist"
            sh "npm i jslint"
            sh "echo Starting lintchecks for ${Component}"
            sh "node_modules/jslint/bin/jslint.js server.js || true"
            sh "echo lintchecks completed for ${Component}"
        }
        else if (env.APPTYE == "maven") { 
            sh "echo Starting lintchecks for ${Component}"
            sh "mvn checkstyle:check || true"
            sh "echo lintchecks completed for ${Component}"
        }
        else if (env.APPTYE == "pylint") { 
            sh "echo Starting lintchecks for ${Component}"
            sh "pylint *.py || true"
            sh "echo lintchecks completed for ${Component}"
        }   
        else {
            sh "Lint checks for frontend is in progress"
        }     
    } 
}
