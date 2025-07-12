def call(String targetPath, String cacheType) {
    def remoteUser = "root"
    def remoteHost = "15.207.112.21"

    def paths = [
        "frontend": "/var/www-app/frontend",
        "backend": "/var/www-app/backend",
        "api-server": "/var/www-app/api-server",
        "report": "/var/www-app/report"
    ]

    def selectedPaths = []

    if (targetPath == 'all') {
        selectedPaths = paths.values()
    } else if (paths.containsKey(targetPath)) {
        selectedPaths = [paths[targetPath]]
    } else {
        error("Invalid TARGET_PATH selection")
    }

    for (path in selectedPaths) {
        if (cacheType == 'env') {
            // Just .env deployment + config:cache
            sh """
                rsync -avz --exclude='.git' .env ${remoteUser}@${remoteHost}:${path}/
                ssh ${remoteUser}@${remoteHost} 'cd ${path} && php artisan config:cache'
            """
        } else if (cacheType == 'config' || cacheType == 'route') {
            // Only cache commands
            sh """
                ssh ${remoteUser}@${remoteHost} 'cd ${path} && php artisan ${cacheType}:cache'
            """
        } else if (cacheType == 'all') {
            // Both cache commands
            sh """
                ssh ${remoteUser}@${remoteHost} 'cd ${path} && php artisan config:cache && php artisan route:cache'
            """
        } else {
            error("Invalid CACHE_TYPE value")
        }
    }
}
