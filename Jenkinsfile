pipeline {
    agent none
    stages {
        stage('Build') {
            parallel {
                stage('Build on Windows') {
                    agent {
                        label "windows"
                    }
                    steps {
                        bat 'mvn -B -DskipTests clean package'
                    }
                    post {
                        always {
                            archiveArtifacts artifacts: '**/target/*.jar*', fingerprint: true
                        }
                    }
                }
                stage('Build on Windows') {
                    agent {
                        label "linux"
                    }
                    steps {
                        sh 'mvn -B -DskipTests clean package'
                    }
                    post {
                        always {
                            archiveArtifacts artifacts: '**/target/*.jar*', fingerprint: true
                        }
                    }
                }
                stage('Build on OSX') {
                    agent {
                        label "osx"
                    }
                    steps {
                        sh 'mvn -B -DskipTests clean package'
                    }
                    post {
                        always {
                            archiveArtifacts artifacts: '**/target/*.jar*', fingerprint: true
                        }
                    }
                }
            }
        }
    }
}