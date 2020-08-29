#!/usr/bin/env sh
docker-compose -f jenkins-docker-compose.yml up -d --build --force-recreate
echo "Starting local jenkins..."
sleep 7
echo "Jenkins can be accessed through web browser via [http://localhost:8092], no credentials needed"
echo "use [http://localhost:8092/job/master-seed] job to generate all jobs from seed scripts"
echo "please note, that after first run you may need to approve some scripts from job console"
