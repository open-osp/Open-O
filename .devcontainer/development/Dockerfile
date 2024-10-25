FROM tomcat:9.0.56-jdk8-openjdk

# Labels
LABEL author="chitrank dave"
LABEL version=0.0.1
LABEL description="Oscar Docker Image for Development Environment"

ARG DRUGREF_DOWNLOAD_URL=https://bitbucket.org/openoscar/drugref/downloads/drugref2.war
ARG OPENOSP_REPO=https://raw.githubusercontent.com/MagentaHealth/open-osp/master/docker/oscar
ARG TOMCAT_PATH=/usr/local/tomcat
ARG DOCS_PATH='/home/oscar/development/volumes'
ARG OSCAR_DOCUMENT="${DOCS_PATH}/OscarDocument"

WORKDIR /workspace

# Install required packages
RUN --mount=type=cache,target=/var/cache/apt \
    apt-get update && apt-get install -y --no-install-recommends dos2unix curl git wget apt-transport-https \
    ca-certificates gnupg lsb-release locales iputils-ping gettext fontconfig libc6 libfreetype6 \
    libjpeg62-turbo libpng16-16 libssl1.1 libstdc++6 libx11-6 libxcb1 libxext6 libxtst6 libxrender1 \
    xfonts-75dpi xfonts-base zlib1g maven mariadb-client

# Remove useless default apps
RUN rm -rf $TOMCAT_PATH/webapps/ROOT/* \
    && rm -rf $TOMCAT_PATH/webapps/docs \
    && rm -rf $TOMCAT_PATH/webapps/examples \
    && rm -rf $TOMCAT_PATH/webapps/host-manager \
    && rm -rf $TOMCAT_PATH/webapps/manager

# creating tomcat ROOT folder
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

# Taking backup of files
RUN mv $TOMCAT_PATH/conf/logging.properties $TOMCAT_PATH/conf/logging.properties.bak
RUN mv $TOMCAT_PATH/conf/tomcat-users.xml $TOMCAT_PATH/conf/tomcat-users.xml.bak
RUN mv $TOMCAT_PATH/conf/server.xml $TOMCAT_PATH/conf/server.xml.bak

# Download Depenency DrugRef2.war
RUN curl -sSL -o $TOMCAT_PATH/webapps/drugref2.war $DRUGREF_DOWNLOAD_URL


# Adding/Copy required files
ADD ./scripts /scripts
ADD ./setup/setup.sh /setup-dir/setup.sh
ADD ./config/tomcat/conf/server.xml $TOMCAT_PATH/conf/server.xml
COPY ./config/shared/volumes/*.properties /root/

# Download OpenOSP files
RUN curl -s $OPENOSP_REPO/index.jsp > $TOMCAT_PATH/webapps/ROOT/index.jsp
RUN curl -s $OPENOSP_REPO/openosp_favicon.ico > $TOMCAT_PATH/webapps/ROOT/openosp_favicon.ico

RUN curl -s $OPENOSP_REPO/conf/logging.properties > $TOMCAT_PATH/conf/logging.properties
RUN curl -s $OPENOSP_REPO/conf/logging-servlet.properties > $TOMCAT_PATH/conf/logging-servlet.properties
RUN curl -s $OPENOSP_REPO/conf/tomcat-users.xml > $TOMCAT_PATH/conf/tomcat-users.xml


# setting up Document folder
RUN mkdir -p $OSCAR_DOCUMENT/oscar/document \
    $OSCAR_DOCUMENT/oscar/billing/download \
    $OSCAR_DOCUMENT/oscar/billing/invoices \
    $OSCAR_DOCUMENT/oscar/eform/images \
    $OSCAR_DOCUMENT/oscar/form/records \
    $OSCAR_DOCUMENT/oscar/onEDTDocs/inbox \
    $OSCAR_DOCUMENT/oscar/onEDTDocs/outbox \
    $OSCAR_DOCUMENT/oscar/onEDTDocs/sent \
    $OSCAR_DOCUMENT/oscar/onEDTDocs/archive \
    $OSCAR_DOCUMENT/oscar/oscarEncounter/oscarMeasurements/styles \
    $OSCAR_DOCUMENT/oscar/incomingdocs

# Converting script files using dos2unix (Required for Windows)
RUN dos2unix /scripts/*
RUN dos2unix /setup-dir/setup.sh

# Change permissions
RUN chmod +x /setup-dir/setup.sh
# setting commands for development
RUN /setup-dir/setup.sh

# Expose Tomcat port
EXPOSE 8080

# Clean up
RUN apt-get autoclean \
    && apt-get clean \
    && apt-get autoremove

# setting up maven repository directory
RUN mkdir -p /root/.m2 && \
    chown -R root:root /root/.m2 && \
    chmod -R 777 /root/.m2

CMD ["tail", "-f", "/dev/null"]

