def call(String targetApp, String cacheType) {
    def remoteUser = "root"

    // Load the config
    def config = serverConfig.getServerPaths()

    def targets = []

    if (targetApp == 'all') {
        config.each { host, apps ->
            apps.each { name, path ->
                targets << [host: host, path: path, name: name]
            }
        }
    } else {
        def found = false
        config.each { host, apps ->
            if (apps.containsKey(targetApp)) {
                targets << [host: host, path: apps[targetApp], name: targetApp]
                found = true
            }
        }
        if (!found) {
            error("Invalid TARGET_PATH: ${targetApp} not found")
        }
    }

    targets.each { t ->
        echo "Running on ${t.name} at ${t.host}:${t.path}"

        if (cacheType == 'env') {
            sh """
                rsync -avz --exclude='.git' .env ${remoteUser}@${t.host}:${t.path}/
                ssh ${remoteUser}@${t.host} 'cd ${t.path} && php artisan config:cache'
            """
        } else if (cacheType in ['config', 'route']) {
            sh """
                ssh ${remoteUser}@${t.host} 'cd ${t.path} && php artisan ${cacheType}:cache'
            """
        } else {
            error("Invalid CACHE_TYPE: ${cacheType}")
        }
    }
}
