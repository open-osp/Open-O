FROM mariadb:10.5

# Labels
LABEL author="chitrank dave"
LABEL version=0.0.1
LABEL description="Oscar-db Docker Image for Development Environment"

# Install required packages
RUN apt-get update && apt-get install -y dos2unix  \
    && apt-get autoclean \
    && apt-get clean \
    && apt-get autoremove

# Adding required files
ADD ./.devcontainer/db/scripts/populate_db.sh /docker-entrypoint-initdb.d/populate_db.sh
ADD  ./database /database
ADD ./.devcontainer/development/config/shared/my.cnf /etc/mysql/my.cnf

# Change permissions
RUN chmod 644 /etc/mysql/my.cnf

# Converting script files using dos2unix (Required for Windows)
RUN dos2unix /docker-entrypoint-initdb.d/populate_db.sh
RUN dos2unix /database/mysql/*.sh

# Ports
EXPOSE 3306
