# Librairy Explorer  [![Release Status](https://travis-ci.org/librairy/explorer.svg?branch=master)](https://travis-ci.org/librairy/explorer) [![Dev Status](https://travis-ci.org/librairy/explorer.svg?branch=develop)](https://travis-ci.org/librairy/explorer) [![Doc](https://raw.githubusercontent.com/librairy/resources/master/figures/interface.png)](https://rawgit.com/librairy/explorer/doc/report/index.html)

It provides a RESTful-API for **librairy** as well as a harvester module to retrieve the full-text content (and tokens) from local files.

## Data Model

![Data Model](https://dl.dropboxusercontent.com/u/299257/librairy/figures/data-modelv0.2.png)


## Get Start!

The only prerequisite to consider is to have installed [Docker-Compose](https://docs.docker.com/compose/) in your system.

Create a file named `docker-compose.yml` containing the following services:

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
and then, deploy it by typing:

```sh
$ docker-compose up
```

That's all!! **librairy explorer** should be run in your system now!. Check it by: [http://localhost:8080/api](http://localhost:8080/api).

Note that by using Docker from OS X, the host address is not `localhost`. See [here](https://docs.docker.com/engine/installation/mac/) for more details.

## Adding Files

Once **librairy** is running, files placed in the `/input` folder will be automatically processed and new *documents* and *items* will be created internally from them.
Also, new *domains* can be created to group existing *documents* or even new *sources* can also be created to define new repositories of *documents*.

Feel free to organize your data as your convenience, either by using the internal harvester to automatically process the files or directly creating resources by using the RESTful-API.

## Distributed Deployment

Instead of deploy all containers as a whole, you can deploy each of them independently. It is useful to run the service distributed in several host-machines.

#### Column-oriented Database

```sh
docker run -it --rm --name column-db -p 5010:8080 -p 5011:9042 -p 5012:9160 librairy/column-db:1.0
```

#### Document-oriented Database

```sh
docker run -it --rm --name document-db -p 5020:9200 -p 5021:9300 librairy/document-db:1.0
```

#### Graph-oriented Database

```sh
docker run -it --rm --name graph-db -p 5030:7474 librairy/graph-db:1.0
```

#### Message Broker

```sh
docker run -it --rm --name event-bus -p 5040:15672 -p 5041:5672 librairy/event-bus:1.0
```

#### Explorer

```sh
docker run -it --rm --name explorer -p 8080:8080 --link column-db --link document-db --link graph-db --link event-bus librairy/explorer
```



Remember that by using the flags: `-it --rm`, the services runs in foreground mode. Instead, you can deploy it in background mode as domain service by using: `-d --restart=always`