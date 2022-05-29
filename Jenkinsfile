pipeline {
    agent any
    tools {
        maven "Maven 3.6.3"
    }
    stages {
        stage('SCM'){
            steps{
                git 'https://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@github.com/goodtester/rohini-icin-bank.git'
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('Docker Image') {
            steps{
                sh "docker build -t goodtester/rohini-icin-bank:${BUILD_NUMBER} ."
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker_credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login -u ${username} -p ${password}"
                }
                sh "docker push goodtester/rohini-icin-bank:${BUILD_NUMBER}"
            }
        }
        stage("Deploy") {
            steps{
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws_configure', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                    sh "sudo docker stop rohini-icin-bank || echo 'no containers to stop'"
                    sh "sudo docker rm rohini-icin-bank || echo 'no containers to remove'"
                    sh "sudo docker rmi goodtester/rohini-icin-bank:${BUILD_NUMBER} || echo 'no images to delete'"
                    sh "sudo docker pull goodtester/rohini-icin-bank:${BUILD_NUMBER}"
                    sh "sudo docker run -d --name rohini-icin-bank -p 8989:8080 goodtester/rohini-icin-bank:${BUILD_NUMBER}"
                }
            }
        }
        stage('Email Build Status'){
            steps{
                emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} More info at: ${env.BUILD_URL}", recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"
            }
        }
    }
}