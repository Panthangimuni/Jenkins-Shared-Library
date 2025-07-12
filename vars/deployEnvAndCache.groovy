def call(String targetApp, String cacheType) {
    def remoteUser = "root"

    // Map all servers with their respective Laravel app paths
    def serverPaths = [
        "15.207.112.21": [
            "frontend": "/var/www-app/frontend",
            "backend": "/var/www-app/backend",
            "api-server": "/var/www-app/api-server",
            "report": "/var/www-app/report"
        ],
        "43.204.216.125": [
            "muni-server": "/var/www-muni/muni-server",
            "narayna-server": "/var/www-narayna/narayna-server",
            "ram-server": "/var/www-ram/ram-server",
            "rathnam-server": "/var/www-app/rathnam-server"
        ],
        "43.204.220.153": [
            "naresh-server": "/var/www-php/naresh-server",
            "ravi-server": "/var/www-php/ravi-server",
            "saravana-server": "/var/www-app/saravana-server",
            "sri-server": "/var/web-php/sri-server"
        ]
    ]

    def selectedPath = null
    def selectedHost = null

    // Look up which server contains the requested app path
    for (entry in serverPaths) {
        def host = entry.key
        def paths = entry.value
        if (paths.containsKey(targetApp)) {
            selectedHost = host
            selectedPath = paths[targetApp]
            break
        }
    }

    if (!selectedHost || !selectedPath) {
        error("Invalid TARGET_PATH selection: '${targetApp}' not found on any server.")
    }

    // Execute Laravel caching logic
    if (cacheType == 'env') {
        sh """
            rsync -avz --exclude='.git' .env ${remoteUser}@${selectedHost}:${selectedPath}/
            ssh ${remoteUser}@${selectedHost} 'cd ${selectedPath} && php artisan config:cache'
        """
    } else if (cacheType in ['config', 'route']) {
        sh """
            ssh ${remoteUser}@${selectedHost} 'cd ${selectedPath} && php artisan ${cacheType}:cache'
        """
    } else if (cacheType == 'all') {
        sh """
            ssh ${remoteUser}@${selectedHost} 'cd ${selectedPath} && php artisan config:cache && php artisan route:cache'
        """
    } else {
        error("Invalid CACHE_TYPE value: ${cacheType}")
    }
}
