@Library('Utilities2') _
node('worker_node1') {
  stage('Source') {
    cleanWs()
    // Get code from our git repository
    git branch: 'wip', url: 'https://github.com/mestebangutierrez/maven-training.git'
  }
  stage('Compile') { 
    // Run Maven to execute compile
    mavenBuild "clean compile"
  }
  stage('Unit Test') { // Run Maven to execute Unit Tests
    try {
        mavenBuild "test"
    } catch(err) {
        junit testResults: '**/target/*-reports/TEST-*.xml', allowEmptyResults: true
        throw err
    }
  }
  stage('Integration Test') { // Run Maven to execute Integration tests
    try {
        mavenBuild "verify"
    } finally {
        junit testResults: '**/target/*-reports/TEST-*.xml', allowEmptyResults: true
    }
  }
  stage('Deployment') {
    configFileProvider([configFile(fileId: 'artifactory-publishing-settings', variable: 'MAVEN_SETTINGS')]) {
        mavenBuild '-s $MAVEN_SETTINGS -DskipTests=true deploy'
    }
  }
}