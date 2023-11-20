pipeline {
    agent none
    triggers { pollSCM('H/15 * * * *') }
    tools {
        maven 'Maven 3.9.5'
        jdk 'OpenJDK 16'
    }
    options { buildDiscarder(logRotator(numToKeepStr: '5')) }
    parameters {
        booleanParam(defaultValue: false, description: 'Perform Release', name: 'release')
    }
    stages {
        stage('Build core') {
            agent {
                label "osx"
            }
            steps {
                sh 'mvn -B -Dmaven.antrun.skip -Dmaven.source.skip -Dmaven.test.skip -DskipTests -DskipITs -pl coreAPI/ package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'coreAPI/target/apidocs/**/*', fingerprint: true
                }
            }
        }
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
            when { branch 'master' }
            steps {
                milestone(1)
                unstash 'windows-natives'
                unstash 'osx-natives'
                unstash 'linux-natives'
                sh 'echo $GPG_SECRET_KEYS | base64 --decode | gpg --batch --import'
                sh 'echo $GPG_OWNERTRUST | base64 --decode | gpg --import-ownertrust'
                withMaven(
                        maven: 'Maven 3.9.5',
                        jdk: 'OpenJDK 16',
                        globalMavenSettingsConfig: 'global-maven-settings-ossrh'
                ) {
                    sh "mvn -P windows,linux,osx,wintab -Dmaven.antrun.skip -Dmaven.test.skip -DskipTests -DskipITs deploy"
                }
            }
        }
        stage('Release') {
            agent {
                label "linux"
            }
            when {
                expression {
                    return params.release
                }
            }
            steps {
                milestone(3)
                unstash 'windows-natives'
                unstash 'osx-natives'
                unstash 'linux-natives'
                sh 'echo $GPG_SECRET_KEYS | base64 --decode | gpg --batch --import'
                sh 'echo $GPG_OWNERTRUST | base64 --decode | gpg --import-ownertrust'
                withMaven(
                        maven: 'Maven 3.9.5',
                        jdk: 'OpenJDK 16',
                        globalMavenSettingsConfig: 'global-maven-settings-ossrh'
                ) {
                    sh "mvn -P windows,linux,osx,wintab versions:set -DremoveSnapshot"
                    script {
                        VERSION_TAG = sh(script: "mvn -Dexpression=project.version help:evaluate | grep -e '^[[:digit:]]'", returnStdout: true).trim()
                    }
                    sh "git tag -a ${VERSION_TAG} -m 'Release tag ${VERSION_TAG}'"
                    sh "mvn -P windows,linux,osx,wintab,release -Dmaven.antrun.skip -Dmaven.test.skip -DskipTests -DskipITs deploy"
                    sh "mvn -P windows,linux,osx,wintab versions:revert"
                    sh "mvn -P windows,linux,osx,wintab versions:set -DnextSnapshot"
                    sh "git commit -m 'Next development release' ."
                    sh "git push origin HEAD:master --follow-tags"
                }
            }
        }
    }
}