def call() {
    node {
        git branch: 'main', url: "https://github.com/Koushik-Ramesh/${COMPONENT}.git"      
        common.lintchecks()
        env.ARGS="-Dsonar.java.binaries=target/"
        common.sonarChecks()
        common.testCases()
        common.artifacts()
    }
}