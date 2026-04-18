FROM postgres:16
COPY 01_schema.sql /docker-entrypoint-initdb.d/
COPY 02_data.sql /docker-entrypoint-initdb.d/
