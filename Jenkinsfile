@Library('Jenkins-Shared-Library') _

pipeline {
    agent any

    parameters {
        choice(name: 'TARGET_PATH', choices: [
            'all',
            'frontend', 'backend', 'api-server', 'report',
            'muni-server', 'narayna-server', 'ram-server', 'rathnam-server',
            'naresh-server', 'ravi-server', 'saravana-server', 'sri-server'
        ], description: 'Select app target (use "all" to apply to all)')

        choice(name: 'CACHE_TYPE', choices: ['env', 'config', 'route'], description: 'Type of deployment/cache operation (only one at a time)')
    }

    stages {
        stage('Checkout Shared Lib') {
            steps {
                git branch: 'develop',
                    credentialsId: 'your-git-credentials-id',
                    url: 'https://github.com/Panthangimuni/Jenkins-Shared-Library.git'
            }
        }

        stage('Run Laravel Cache Commands') {
            steps {
                script {
                    deployEnvAndCache(params.TARGET_PATH, params.CACHE_TYPE)
                }
            }
        }
    }
}
