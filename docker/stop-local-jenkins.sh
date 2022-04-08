#!/usr/bin/env sh
docker-compose down --rmi all -v --remove-orphans
echo "Stoping local jenkins..."