#!/usr/bin/env bash
cd ..

if [ "$(docker ps -aq -f status=running -f name=dvt)" ]; then
	docker stop dvt
  docker rm dvt
	docker rmi dvt
fi

docker build -f Dockerfile -t dvt .
docker run -d --name dvt --restart always -p 8092:8092 dvt