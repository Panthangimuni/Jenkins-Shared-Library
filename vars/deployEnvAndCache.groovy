def call(String targetApp, String cacheType) {
    def remoteUser = "root"

    // Define all servers and their Laravel app directories
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

    def targets = []

    if (targetApp == 'all') {
        // Prepare list of all apps across all servers
        serverPaths.each { host, apps ->
            apps.each { name, path ->
                targets << [host: host, path: path, name: name]
            }
        }
    } else {
        // Find matching path for specific app
        def found = false
        serverPaths.each { host, apps ->
            if (apps.containsKey(targetApp)) {
                targets << [host: host, path: apps[targetApp], name: targetApp]
                found = true
            }
        }
        if (!found) {
            error("Invalid TARGET_PATH: ${targetApp} not found on any server")
        }
    }

    // Loop through all targets and execute based on cacheType
    targets.each { t ->
        echo "Executing on ${t.name} at ${t.host}:${t.path} for ${cacheType}"
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
