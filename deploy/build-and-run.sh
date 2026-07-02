#!/usr/bin/env bash
# ------------------------------------------------------------------
# build-and-run.sh
# Builds the Library Management System with Maven and launches it.
# Usage: ./deploy/build-and-run.sh
# ------------------------------------------------------------------
set -e
echo "==> Cleaning and building project with Maven..."
mvn clean package
echo "==> Build complete. Launching application..."
java -jar target/library-management-system.jar
