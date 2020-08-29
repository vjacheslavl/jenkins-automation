#!/usr/bin/env sh
docker-compose -f jenkins-docker-compose.yml down --rmi all -v --remove-orphans
echo "Stoping local jenkins..."