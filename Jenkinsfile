pipeline {
    agent none
    triggers { pollSCM('H/15 * * * *') }
    tools {
        maven 'Maven 3.5.3'
        jdk 'OpenJDK 9'
    }
    options { buildDiscarder(logRotator(numToKeepStr: '5')) }
    stages {
        stage('Build') {
            parallel {
                stage('Build on Windows') {
                    agent {
                        label "windows"
                    }
                    steps {
                        bat 'echo %JAVA_HOME%'
                        bat 'mvn -B -DskipTests clean package'
                    }
                    post {
                        success {
                            stash includes: 'plugins/**/target/*.jar*', name: 'windows-artifacts'
                        }
                    }
                }
                stage('Build on Linux') {
                    agent {
                        label "linux"
                    }
                    steps {
                        sh 'echo $JAVA_HOME'
                        sh 'mvn -B -DskipTests clean package'
                    }
                    post {
                        success {
                            stash includes: 'plugins/**/target/*.jar*', name: 'linux-artifacts'
                            stash includes: '**/target/*.jar*', excludes: 'plugins/**/target/*.jar*', name: 'core-artifacts'
                        }
                    }
                }
                stage('Build on OSX') {
                    agent {
                        label "osx"
                    }
                    steps {
                        sh 'echo $JAVA_HOME'
                        sh 'mvn -B -DskipTests clean package'
                    }
                    post {
                        success {
                            stash includes: 'plugins/**/target/*.jar*', name: 'osx-artifacts'
                        }
                    }
                }
            }
        }
        stage('Unpack') {
            agent {
                label "linux"
            }
            steps {
                unstash 'core-artifacts'
                unstash 'windows-artifacts'
                unstash 'osx-artifacts'
                unstash 'linux-artifacts'
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/*.jar*', fingerprint: true
                }
            }
        }
    }
}