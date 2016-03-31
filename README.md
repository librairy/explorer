# Librairy Explorer  [![Release Status](https://travis-ci.org/librairy/explorer.svg?branch=master)](https://travis-ci.org/librairy/explorer) [![Dev Status](https://travis-ci.org/librairy/explorer.svg?branch=develop)](https://travis-ci.org/librairy/explorer) [![Doc](https://raw.githubusercontent.com/librairy/resources/master/figures/interface.png)](https://rawgit.com/librairy/explorer/doc/report/index.html)

It provides programmatic access to functionality and content of **librairy**. Different facades are offered based on the type of the operation:
- **Management**: Create/Read/Update/Delete (CRUD) resources.
- **Read**: Get a resource by key, e.g. by `uri`.
- **Search**: Get a sorted list of resources based on internal criterias, i.e. based only on the content of the resource. For example, resources containing the word `graphic` in their title.
- **Exploration**: Find a path or get resources from their relationships.

Each of them is presented as a *RESTful-API* using [JSON](http://json.org) as the return format.

## Data Model

Data is organized internally as follows:

![Data Model](https://dl.dropboxusercontent.com/u/299257/librairy/figures/data-modelv0.2.png)

The next figure tries to clarify the distinction between `documents` and `items`.
A `document` describes a file not the content, and may aggregates more `documents` as far as contains new files inside.
Instead, an `item` is an abstract entity containing only one kind of data (e.g. text, image, workflow, etc) retrieved from a `document`.   

![paper](https://dl.dropboxusercontent.com/u/299257/epnoi/images/paper-to-resources.png)


## Get Start!

The only prerequisite to consider is to have installed [Docker-Compose](https://docs.docker.com/compose/) in your system.

Once it is installed, create a file named `docker-compose.yml` containing the following services:

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

That's all!! **librairy explorer** should be run in your system now.

Verify that it works on: [http://localhost:8080/api](http://localhost:8080/api).

Note that by using Docker from OS X, the host address is not `localhost`. See [here](https://docs.docker.com/engine/installation/mac/) for more details.

## Endpoints

As previously mentioned, several facades are deployed to support different type of requests.

### Management

Oriented to manage data. This service, along with [librairy-boot](https://github.com/librairy/boot), are the entry point for external services.

- **swagger-endpoint**: [http://localhost:8080/api](http://localhost:8080/api).
- **ws-endpoint**: [http://localhost:8080/api/<version>](http://localhost:8080/api/<version>). *the version number is displayed at the bottom left on swagger-endpoint*

Feel free to organize your data as your convenience, either by using the librairy-harvester or directly creating resources by using this RESTful-API.

### Read

Oriented to get detailed information of a resource.

- **swagger-endpoint**: [http://localhost:8080/api](http://localhost:8080/api).
- **ws-endpoint**: [http://localhost:8080/api/<version>](http://localhost:8080/api/<version>). *the version number is displayed at the bottom left on swagger-endpoint*
- **CQl-HTTP-endpoint**: [http://localhost:5011](http://localhost:5011) . More info about Cassandra Query Language [here](http://cassandra.apache.org/doc/cql3/CQL.html)
- **CQl-Thrift-endpoint**: [http://localhost:5012](http://localhost:5012) . More info about Thrift Serialization [here](https://thrift.apache.org/)

### Search

Oriented to find resources based on their content.

- **rest-endpoint**: [http://localhost:5020/research/_search](http://localhost:5020/research/_search). More info [here](https://www.elastic.co/guide/en/elasticsearch/guide/current/_talking_to_elasticsearch.html)
- **binary-endpoint**: [http://localhost:5020](http://localhost:5020). More info [here](https://www.elastic.co/guide/en/elasticsearch/guide/current/_talking_to_elasticsearch.html)


### Exploration

Oriented to find path based on relationships.

- **rest-endpoint**: [http://localhost:5030/db/data/transaction/commit](http://localhost:5030/db/data/transaction/commit). More info [here](http://neo4j.com/docs/stable/rest-api.html)
- **CYPHER-endpoint**: [http://localhost:5030](http://localhost:5030). More info [here](http://neo4j.com/developer/cypher-query-language/)


## Distributed Deployment

Instead of deploy all containers as a whole, you can deploy each of them independently. It is useful to run the service in a distributed way deployed in several host-machines.

- **Column-oriented Database**:
    ```sh
    $ docker run -it --rm --name column-db -p 5010:8080 -p 5011:9042 -p 5012:9160 librairy/column-db:1.0
    ```

- **Document-oriented Database**:
    ```sh
    $ docker run -it --rm --name document-db -p 5020:9200 -p 5021:9300 librairy/document-db:1.0
    ```

- **Graph-oriented Database**:
    ```sh
    $ docker run -it --rm --name graph-db -p 5030:7474 librairy/graph-db:1.0
    ```

- **Message Broker**:
    ```sh
    $ docker run -it --rm --name event-bus -p 5040:15672 -p 5041:5672 librairy/event-bus:1.0
    ```
- **Explorer**:
    ```sh
    $ docker run -it --rm --name explorer -p 8080:8080 --link column-db --link document-db --link graph-db --link event-bus librairy/explorer
    ```

Remember that by using the flags: `-it --rm`, the services runs in foreground mode. Instead, you can deploy it in background mode as a domain service by using: `-d --restart=always`