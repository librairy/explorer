FROM httpd:2.4
COPY ./bootstrap/ /usr/local/apache2/htdocs/bootstrap
COPY ./css/ /usr/local/apache2/htdocs/css
COPY ./js/ /usr/local/apache2/htdocs/js
COPY index.html /usr/local/apache2/htdocs/index.html
