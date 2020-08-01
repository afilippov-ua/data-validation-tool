#!/usr/bin/env bash
cd ..
docker stop dvt
docker rm dvt
docker rmi dvt
docker build -f Dockerfile -t dvt .
docker run -d --name dvt --restart always -p 8092:8092 dvt