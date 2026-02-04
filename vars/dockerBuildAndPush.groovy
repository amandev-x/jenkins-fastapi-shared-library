#!/usr/bin/env groovy

/**
 * Build and push Docker image to registry
 * @param imageName - Name of the Docker image
 * @param tag - Image tag (default: BUILD_NUMBER)
 * @param registry - Docker registry URL (default: DockerHub)
 * @param credentialsId - Jenkins credentials ID
 */

 def call(Map config = [:]) {
    def imageName = config.imageName ?: error("Image name required")
    def tag = config.tag ?: env.BUILD_NUMBER
    def registry = config.registry ?: error("Image registry required")
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    echo "Building docker image: ${imageName}:${tag}"

    // Build image
    def build = docker.build("${imageName}:${tag}")

    // Also tagging as latest
    docker.build("${imageName}:latest")

    // Pushing to registry
    docker.withRegistry(registry, credentialsId) {
        image.push(tag)
        image.push("latest")
    }

    echo "Image pushed successfully: ${imageName}:${tag}"
 }