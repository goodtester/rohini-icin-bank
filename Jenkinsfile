pipeline {
    agent any
    environment{
        EC2_IP='ec2-user@ec2-54-82-72-210.compute-1.amazonaws.com'
    }
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
                sh "docker build --platform=linux/amd64 -t goodtester/rohini-icin-bank:${BUILD_NUMBER} ."
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
                sshagent (credentials: ['aws-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'docker_credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                        sh "ssh ${EC2_IP} docker login -u ${username} -p ${password}"
                    }
                    sh 'ssh ${EC2_IP}  docker stop rohini-icin-bank || echo "no containers to stop"'
                    sh 'ssh ${EC2_IP}  docker rm rohini-icin-bank || echo "no containers to remove"'
                    sh 'ssh ${EC2_IP}  docker rmi goodtester/rohini-icin-bank:${BUILD_NUMBER} || echo "no images to delete"'
                    sh 'ssh ${EC2_IP}  docker pull goodtester/rohini-icin-bank:${BUILD_NUMBER}'
                    sh 'ssh ${EC2_IP}  docker run -d --name rohini-icin-bank -p 8080:8080 goodtester/rohini-icin-bank:${BUILD_NUMBER}'
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