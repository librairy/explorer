FROM elasticsearch:1.7.4
MAINTAINER Librairy <librairy.framework@gmail.com>

ENV ES_HEAP_SIZE 1g

RUN /usr/share/elasticsearch/bin/plugin -install royrusso/elasticsearch-HQ

EXPOSE 9200 9300

CMD ["elasticsearch"]