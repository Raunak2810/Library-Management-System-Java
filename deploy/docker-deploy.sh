#!/usr/bin/env bash
# ------------------------------------------------------------------
# docker-deploy.sh
# Builds the Docker image and runs the container interactively,
# mounting host folders for data/ and logs/ so records survive
# container restarts.
# Usage: ./deploy/docker-deploy.sh
# ------------------------------------------------------------------
set -e
IMAGE_NAME="library-management-system"
TAG="1.0.0"
echo "==> Building Docker image ${IMAGE_NAME}:${TAG}..."
docker build -t "${IMAGE_NAME}:${TAG}" .
echo "==> Running container (interactive CLI)..."
docker run -it --rm \
  -v "$(pwd)/data:/app/data" \
  -v "$(pwd)/logs:/app/logs" \
  "${IMAGE_NAME}:${TAG}"
