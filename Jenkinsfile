pipeline {
    stages {
        stage ('Compute version') {   
            configFileProvider(
                [configFile(fileId: '0fe83525-d72f-47b4-8769-536962b9c784', variable: 'computeversion')]) {
                sh 'eval $computeversion > version.txt'
                sh 'cat version.txt'
             }       
            
            post {
                always {
                    archiveArtifacts artifacts: 'version.txt'
                }
            }
        }
    }       
}
