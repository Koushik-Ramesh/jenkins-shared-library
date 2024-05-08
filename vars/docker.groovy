def call() {
    node() {
    sh "rm -rf *"
    git branch: 'main', url: "https://github.com/Koushik-Ramesh/${COMPONENT}.git"
    env.APP_TYPE=""
    common.lintchecks()
    if(env.TAG_NAME != null) {
        stage('Generating the artifacts') {
                if(env.APPTYPE == "nodejs") {
                    sh "echo Generating Artifiacts...."
                    sh "npm install"

                }
                else if(env.APPTYPE == "maven") {
                    sh "echo Generating Artifiacts...."
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "ls -ltr"
                }
                else if(env.APPTYPE == "python") {
                    sh "echo Generating Artifiacts...."
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                }
                else {
                    sh ''' 
                        echo Generating Artifiact
                        cd static/
                        zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                    '''

                }
                sh "wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
                sh "docker build -t 751732450123.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."
                sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 751732450123.dkr.ecr.us-east-1.amazonaws.com"
                sh "docker push 751732450123.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"
            }
        }    
    }
}