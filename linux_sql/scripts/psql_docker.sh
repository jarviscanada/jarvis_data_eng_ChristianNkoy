#!/bin/sh

#Store CLI arguments
cmd=$1
db_username=$2
db_password=$3

#Starting docker
#If docker is already running then do nothing
#If docker is not running then start docker
sudo systemctl status docker || systemctl start docker

#Record container status
docker container inspect jrvs-psql
container_status=$?

#Swicth between cases (create, start, stop)
case $cmd in
  create)

  # If container already exists then exit with code 1
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	fi

  #Make sure there are 3 arguments for create cmd
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  #Create the container
	docker volume create pgdata
	export PGPASSWORD='password'
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
	exit $?
	;;

  start|stop)
  #Exit with code 1 if container doe snot exist
  if [ $container_status -ne 0 ]; then
    echo 'Cannot ' $cmd '. ' 'Container does not exist.'
    exit 1
  else
    #Run start | stop command
    docker container $cmd jrvs-psql
    exit $?
  fi
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac