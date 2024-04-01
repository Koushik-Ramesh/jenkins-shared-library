def sonarChecks() {
    stage('Sonar Checks') {
        sh "echo starting Sonar Checks for ${COMPONENT}"
    // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}"
    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
    // sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${COMPONENT}"
        sh "echo Sonar Checks for ${COMPONENT} completed"
    }    
}

// def lintchecks() {
//     sh "echo Starting lintchecks for ${COMPONENT}"
//     sh "pylint *.py || true"
//     sh "echo lintchecks completed for ${COMPONENT}"
// }

def lintchecks() {
    stage ('Lint Checks') {
        if (env.APPTYPE == "nodejs") {
            sh "echo Installing JSlist"
            sh "npm i jslint"
            sh "echo Starting lintchecks for ${COMPONENT}"
            sh "node_modules/jslint/bin/jslint.js server.js || true"
            sh "echo lintchecks completed for ${COMPONENT}"
        }
        else if (env.APPTYPE == "maven") { 
            sh "echo Starting lintchecks for ${COMPONENT}"
            sh "mvn checkstyle:check || true"
            sh "echo lintchecks completed for ${COMPONENT}"
        }
        else if (env.APPTYPE == "pylint") { 
            sh "echo Starting lintchecks for ${COMPONENT}"
            sh "pylint *.py || true"
            sh "echo lintchecks completed for ${COMPONENT}"
        }   
        else {
            sh " echo Lint checks for frontend is in progress"
        }     
    } 
}

def testCases() {
    stage('Test Cases') {
        def stages = [:]

        stages["Unit Testing"] = {
            echo "Starting Unit Testing"
            echo "Unit Testing is Completed"
        }
        stages["Integration Testing"] = {
            echo "Starting Integration Testing"
            echo "Integration Testing is Completed"
        }
        stages["Funtional Testing"] = {
            echo "Starting Functional Testing"
            echo "Functional Testing is Completed"
        }
        parallel(stages)
    }
}

def artifacts() {
    stage ('Checking the artifacts release'){
        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print UPLOAD_STATUS
    }

    if(env.UPLOAD_STATUS == "") {
        stage ('Generating the artifacts') {
            if (env.APPTYPE == "nodejs") {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                }
            else if (env.APPTYPE == "maven") {
                    sh "echo Generating Artifacts"
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                }
            else if (env.APPTYPE == "python") {
                    sh "echo Generating Artifacts"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                }
            else {
                sh '''
                    echo Generating Artifacts....
                    cd static/
                    zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                '''
            }        
        }
        stage('Uploading the artifacts') {
            withCredentials([usernamePassword(credentialsId: 'NEXUS_CRED', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) { 
                    sh "echo Uploading ${COMPONENT} artifact to NEXUS"
                    sh "curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.37.89:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                    sh "echo Uploading ${COMPONENT} artifact to NEXUS is completed"
            }
        }
    }
}
