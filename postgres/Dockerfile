FROM postgres:14.2

# Copy in the load-extensions script
COPY pg-init.sh /docker-entrypoint-initdb.d/
RUN chmod 755 /docker-entrypoint-initdb.d/pg-init.sh