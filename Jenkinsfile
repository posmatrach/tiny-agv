pipeline {
    agent {
        label 'master'
    }
    stages {
        stage ('Compute version') {   
            steps {
                configFileProvider([configFile(fileId: '0fe83525-d72f-47b4-8769-536962b9c784', variable: 'computeversion')]) {
                    sh 'echo $computeversion'
                    sh 'chmod +x $computeversion'
                    sh '$computeversion'
                }   
            }    
            post {
                always {
                    archiveArtifacts artifacts: 'version.txt'
                }
            }
        }
    }       
}
