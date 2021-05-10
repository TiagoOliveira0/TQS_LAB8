pipeline {
  agent any
  tools {
      jdk 'jdk11'
      maven 'mvn'
  }
  stages {
      stage('test java installation') {
          steps {
              sh 'java -version'
          }
      }
      stage('test maven installation') {
          steps {
              sh 'mvn -version'
          }
      }   
  }
}
