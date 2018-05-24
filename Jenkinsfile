pipeline {
    agent none
    triggers { pollSCM('H/15 * * * *') }
    tools {
        maven 'Maven 3.5.3'
        jdk 'OpenJDK 9'
    }
    options { buildDiscarder(logRotator(numToKeepStr: '5')) }
    stages {
        stage('Build natives') {
            parallel {
                stage('Build Windows natives') {
                    agent {
                        label "windows"
                    }
                    steps {
                        bat 'mvn -B -am -pl plugins/windows/,plugins/wintab/ clean compile'
                    }
                    post {
                        success {
                            stash includes: 'plugins/**/target/natives/*.dll', name: 'windows-natives'
                        }
                    }
                }
                stage('Build Linux natives') {
                    agent {
                        label "linux"
                    }
                    steps {
                        sh 'mvn -B -am -pl plugins/linux/ clean compile'
                    }
                    post {
                        success {
                            stash includes: 'plugins/**/target/natives/*.so*', name: 'linux-natives'
                        }
                    }
                }
                stage('Build OSX natives') {
                    agent {
                        label "osx"
                    }
                    steps {
                        sh 'mvn -B -am -pl plugins/OSX/ clean compile'
                    }
                    post {
                        success {
                            stash includes: '**/target/natives/*.jnilib', name: 'osx-natives'
                        }
                    }
                }
            }
        }
        stage('Build') {
            agent {
                label "linux"
            }
            steps {
                unstash 'windows-natives'
                unstash 'osx-natives'
                unstash 'linux-natives'
                sh 'mvn -B -P windows,linux,osx,wintab -Dmaven.antrun.skip -Dmaven.javadoc.skip -Dmaven.source.skip -Dmaven.test.skip -DskipTests -DskipITs package'
            }
            post {
                success {
                    stash includes: '**/target/*.jar', name: 'all-java-jars'
                    archiveArtifacts artifacts: '**/target/*.jar*', fingerprint: true
                }
            }
        }
        stage('Deploy') {
            agent {
                label "linux"
            }
            steps {
                unstash 'windows-natives'
                unstash 'osx-natives'
                unstash 'linux-natives'
                sh 'echo $GPG_SECRET_KEYS | base64 --decode | gpg --batch --import'
                sh 'echo $GPG_OWNERTRUST | base64 --decode | gpg --import-ownertrust'
                withMaven(
                        maven: 'Maven 3.5.3',
                        jdk: 'OpenJDK 9',
                        globalMavenSettingsConfig: 'global-maven-settings-ossrh',
                        mavenOpts: '-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts' //Work around for JDK9 missing cacerts
                ) {
                    sh "mvn -P windows,linux,osx,wintab -Dmaven.antrun.skip -Dmaven.test.skip -DskipTests -DskipITs deploy"
                }
            }
        }
    }
}