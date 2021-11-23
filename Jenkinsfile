pipeline{
    agent any
    environment{
        SCANNER_HOME = tool 'SonarQubeScanner'
    }
    stages{
        stage("Git"){
            steps{
                git url:"https://github.com/MartiMarch/NetworkScanner.git"
            }
        }
        stage("Maven"){
            steps{
                script{
                    sh "chdir=${WORKSPACE}/NetworkScanner mvn validate compile test package verify install"
                }
            }
        }
        stage("SonarQube")
        {
            steps{
                withSonarQubeEnv("SonarQube"){
                    sh '''${SCANNER_HOME}/bin/sonar-scanner -X \
                       -Dsonar.projectName=NetworkScanner \
                       -Dsonar.projectKey=NetworkScanner \
                       -Dsonar.projectVersion=1.0 \
                       -Dsonar.sources=${WORKSPACE}/src/main/java \
                       -Dsonar.sourceEncoding=UTF-8 \
                       -Dsonar.language=java \
                       -Dsonar.java.binaries=${WORKSPACE}/target/classes
                    '''
                }
            }
        }
        stage("Nexus"){
            steps{
                script{
                    def pom = readMavenPom file: 'pom.xml'
                    nexusArtifactUploader(
                        nexusVersion: "nexus3",
                        protocol: "http",
                        nexusUrl: "192.168.1.34:8081",
                        groupId: "${pom.groupId}",
                        version: "${pom.version}",
                        repository: "network-scanner",
                        credentialsId: "nexus3",
                        artifacts: [
                            [artifactId: "${pom.artifactId}",
                            type: "${pom.packaging}",
                            classifier: "",
                            file: "${WORKSPACE}/target/ObtainARPofGateWay-1.0-SNAPSHOT.jar"]
                        ]
                    );
                }
            }
        }
    }
}
