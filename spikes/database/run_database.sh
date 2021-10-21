#!/bin/bash
docker build -t postgres-spl-fantasy .
docker container stop spl-fantasy-db
docker container rm spl-fantasy-db
docker run -d --name spl-fantasy-db -p 5555:5432 postgres-spl-fantasy
