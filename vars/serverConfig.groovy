// vars/serverConfig.groovy
def getServerPaths() {
    return [
        "52.66.138.178": [
            "frontend": "/var/www-app/frontend",
            "backend": "/var/www-app/backend",
            "api-server": "/var/www-app/api-server",
            "report": "/var/www-app/report"
        ],
        "65.0.61.156": [
            "muni-server": "/var/www-muni/muni-server",
            "narayna-server": "/var/www-narayna/narayna-server",
            "ram-server": "/var/www-ram/ram-server",
            "rathnam-server": "/var/www-app/rathnam-server"
        ],
        "65.0.87.184": [
            "naresh-server": "/var/www-php/naresh-server",
            "ravi-server": "/var/www-php/ravi-server",
            "saravana-server": "/var/www-app/saravana-server",
            "sri-server": "/var/web-php/sri-server"
        ]
    ]
}
return this
