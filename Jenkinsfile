pipeline {
    agent any

    options {
        timeout(time: 1, unit: 'HOURS')
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = "library"
        JAR_NAME = "library-0.0.1-SNAPSHOT.jar"
    }

    stages {
        stage('Build Application') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    sh './mvnw clean package -DskipTests'
                }
                
            }
            post {
                failure {
                    echo "❌ Build falló. Revisar logs."
                }
            }
        }

        stage('Run Tests') {
            environment {
                PROFILE = "test"
            }
            steps {
                    sh """
                        echo "🔍 Ejecutando tests"
                        ./mvnw test
                    """
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                    echo "📊 Reportes de tests archivados."
                }
                failure {
                    echo "❌ Tests fallaron. Revisar reportes."
                }
            }
        }

        stage('Deploy to Production') {
            when { branch 'main' }
            steps { script { deployWithDocker('prod', 'docker-compose.prod.yml', 'env_file_prod') } }
            post {
                failure {
                    sh "docker compose -p ${APP_NAME}-prod -f docker-compose.prod.yml down || true"
                }
            }
        }

        stage('Deploy to Development') {
            when { branch 'development' }
            steps { script { deployWithDocker('dev', 'docker-compose.dev.yml', 'env_file_dev') } }
        }
    }

    post {
        always {
            echo "Pipeline finalizado - Estado: ${currentBuild.result}"
        }
        success {
            echo "✅ ¡Despliegue exitoso!"
        }
        failure {
            echo "❌ Pipeline falló. Revisar logs."
        }
    }
}

def deployWithDocker(String env, String composeFile, String envFileCredential) {
    withCredentials([file(credentialsId: envFileCredential, variable: 'ENV_FILE')]) {
            sh """
                if [ ! -s "\$ENV_FILE" ]; then
                    echo "❌ Archivo .env vacío o no existe."
                    exit 1
                fi
                cp \$ENV_FILE .env
                echo "🧹 Limpiando contenedores antiguos..."
                docker compose -p ${APP_NAME}-${env} -f ${composeFile} down || true
                echo "🚀 Desplegando entorno ${env}..."
                docker compose -p ${APP_NAME}-${env} -f ${composeFile} up --build -d
            """
    }
}