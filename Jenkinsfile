pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    sh 'docker-compose build'
                }
            }
        }

        stage('Start Containers') {
            steps {
                script {
                    sh 'docker-compose up'
                }
            }
        }
        stage('Wait for Health Check') {
                    steps {
                        echo 'Waiting for the app to become healthy...'
                        script {
                            def maxRetries = 20
                            def retryCount = 0
                            def success = false

                            while (retryCount < maxRetries) {
                                try {
                                    sh 'curl --fail http://localhost:8083/health'
                                    success = true
                                    break
                                } catch (Exception e) {
                                    retryCount++
                                    echo "App is not ready yet. Retrying (${retryCount}/${maxRetries})..."
                                    sleep 10
                                }
                            }

                            if (!success) {
                                error "Spring Boot app failed to start within the timeout period."
                            }
                        }
                    }
        }


        stage('Run Tests') {
            steps {
                script {
                    // Add commands for testing the application
                    // For example, using curl to check service health
                    sh 'curl -f http://localhost:8083/health' // Spring Boot app
                    //sh 'curl -f http://localhost:5000/health' // Flask app
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    sh 'docker-compose down'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution complete!'
        }
    }
}
