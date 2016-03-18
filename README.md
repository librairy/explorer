# Librairy Explorer  [![Build Status](https://travis-ci.org/librairy/explorer.svg?branch=develop)](https://travis-ci.org/librairy/explorer)

Librairy Explorer deploy the storage systems as well as a REST-API to explore them.

## Data Model

In our opinion, the best documentation are examples, take a look at some address ones.

## Get Start!

The only prerequisite to consider is [Docker](https://www.docker.com/). You have to be installed the Docker daemon to deploy **librairy** in your system.

And then, run **explorer** by using the following `docker-compose.yml`:  

```yml
column-db:
  container_name: column-db
  image: librairy/column-db:1.0
  expose:
    - "9042"
    - "9160"
document-db:
  container_name: document-db
  image: librairy/document-db:1.0
  expose:
    - "9200"
    - "9300"
  ports:
    - "5020:9200"
graph-db:
  container_name: graph-db
  image: librairy/graph-db:1.0
  expose:
    - "7474"
  ports:
    - "5030:7474"
event-bus:
  container_name: event-bus
  image: librairy/event-bus:1.0
  expose:
    - "15672"
    - "5672"
explorer:
  container_name: explorer
  image: librairy/explorer:latest
  expose:
    - "8080"
  ports:
    - "8080:8080"
  links:
    - column-db
    - document-db
    - graph-db
    - event-bus
```



Instead of deploy all containers as a whole, you can deploy each of them independently. It is useful to run the service distributed in several host-machines.

## Column-oriented Database

```sh
docker run -it --rm --name column-db -p 5010:8080 -p 5011:9042 -p 5012:9160 librairy/column-db:1.0
```

Remember, by using the flags: `-it --rm`, the services runs in foreground mode, if you want to deploy it in background mode and even as a domain service you should use: `-d --restart=always`

## Document-oriented Database

```sh
docker run -it --rm --name document-db -p 5020:9200 -p 5021:9300 librairy/document-db:1.0
```

## Graph-oriented Database

```sh
docker run -it --rm --name graph-db -p 5030:7474 librairy/graph-db:1.0
```

## Message Broker

```sh
docker run -it --rm --name event-bus -p 5040:15672 -p 5041:5672 librairy/event-bus:1.0
```

## Explorer

```sh
docker run -it --rm --name explorer -p 8080:8080 --link column-db --link document-db --link graph-db --link event-bus librairy/explorer
```
