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
                    sh "chdir=${WORKSPACE}/NetworkScanner mvn validate compile test"
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
    }
}