pipeline {
  agent any

  environment{
      APP_VERSION = "${env.GIT_COMMIT.substring(0,4)}.${env.BUILD_ID}"
      DOCKER_REGESTRY = "https://hub.docker.com/repository/docker"
      DOCKER_ARTIFACTORY = "shubhamk54/weather-app"
      SONAR_PROJECT_KEY = "weather-prediction"
      SONAR_HOST = "http://localhost:8083"
      ENV_TO_DEPLOY = "dev"
  }

  stages {
    stage ('Checkout code') {
      steps {
        checkout scm
      }
    }
    stage ('Build Aftifact') {
      steps {
        sh 'mvn install'
      }
    }
    stage ('Sonar Analysis') {
      steps {
            withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
            sh "mvn sonar:sonar  -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} -Dsonar.host.url=${env.SONAR_HOST} -Dsonar.login=${SONAR_TOKEN}"
          }
      }
    }
    stage('Docker Build') {
      steps {
        script{
          env.DOCKER_IMAGE="${env.DOCKER_ARTIFACTORY}:${env.APP_VERSION}"
          sh "docker build -t ${env.DOCKER_IMAGE} ."
        }
      }
    }
    stage('Push to DockerHub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'docherHub', passwordVariable: 'docherHubUserToken', usernameVariable: 'docherHubUserId')]) {
          sh "echo ${env.docherHubUserToken}  | docker login -u ${env.docherHubUserId} --password-stdin"
          sh "docker push ${env.DOCKER_IMAGE}"
        }
      }
    }

    stage('Deploy') {
      steps {
        sh "cat deployment-mf/${params.ENV_TO_DEPLOY}/deployment.yaml | envsubst | cat > deployment-mf/${params.ENV_TO_DEPLOY}/deployment-stg.yaml"
        sh "kubectl apply -f deployment-mf/${params.ENV_TO_DEPLOY}/deployment-stg.yaml --namespace=${params.ENV_TO_DEPLOY}"
        sh "kubectl apply -f deployment-mf/${params.ENV_TO_DEPLOY}/service.yaml --namespace=${params.ENV_TO_DEPLOY}"
      }
    }
  }
}
