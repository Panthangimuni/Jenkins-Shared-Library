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

        choice(name: 'CACHE_TYPE', choices: ['env', 'config', 'route'], description: 'Choose cache operation')
    }

    stages {
        stage('Run Laravel Deployment') {
            steps {
                script {
                    deployEnvAndCache(params.TARGET_PATH, params.CACHE_TYPE)
                }
            }
        }
    }
}
