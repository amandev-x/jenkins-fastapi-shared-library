#!/usr/bin/env groovy
/**
 * Deploy Docker container to specified environment
 * @param environment - Environment name (dev/staging/prod)
 * @param imageName - Docker image name
 * @param tag - Image tag
 */

 def call(Map config = [:]) {
    def environment = config.environment ?: error("Environment is required")
    def imageName = config.imageName ?: error("Image name required")
    def tag = config.tag ?: env.BUILD_NUMBER 

    // Define port based environment
    def portMap = [
        "dev": "8100",
        "staging": "8200",
        "prod": "8300"
    ]

    def port = portMap[environment] ?: error("Unknown environment: ${environment}")
    def containerName = "fastapi:${environment}"

    echo "Deploying to ${environment} environment on port ${port}..."

    sh """
     # Stop and remove old containers
     docker stop ${containerName} || true
     docker rm -f ${containerName} || true

     # Pull latest image
     docker pull ${imageName}:${tag}

     # Run the new container
     docker run -d --name ${container_name} -p ${port}:8000 --restart unless-stopped ${imageName}:${tag}

     # Wait for container to start
     sleep 10

     # Healthcheck
     curl -f http://localhost:${port}/health || exit 1
    """
    echo "Deployed to ${environment}: http://localhost:${port}"

 }