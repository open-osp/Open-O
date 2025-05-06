FROM tomcat:9.0.97-jdk21-temurin

# Set environment variables for Tomcat
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV CATALINA_OPTS="--add-opens java.base/java.net=ALL-UNNAMED -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

# Copy Tomcat installation from the official Tomcat image
COPY --from=tomcat:9.0.97 /usr/local/tomcat $CATALINA_HOME

# Labels
LABEL author="OpenOSP" \
      version="0.0.2" \
      description="OpenOSP Docker Image for Development Environment"

ARG DRUGREF_DOWNLOAD_URL=https://bitbucket.org/openoscar/drugref/downloads/drugref2.war
ARG TOMCAT_PATH=/usr/local/tomcat
ARG DOCS_PATH='/home/oscar/development/volumes'
ARG OSCAR_DOCUMENT="${DOCS_PATH}/OscarDocument"

WORKDIR /workspace

# Install required packages and clean up apt lists
RUN apt-get update && apt-get install -y --no-install-recommends \
    dos2unix curl git wget apt-transport-https ca-certificates gnupg lsb-release \
    locales iputils-ping gettext fontconfig libc6 libfreetype6 \
    libpng16-16 libssl-dev libstdc++6 libx11-6 libxcb1 libxext6 libxtst6 libjpeg62 \
    libxrender1 libxi6 xfonts-75dpi xfonts-base zlib1g maven mariadb-client \
    nano openssh-client vim-tiny \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Remove unnecessary default web apps in Tomcat
RUN rm -rf $TOMCAT_PATH/webapps/ROOT/* \
    $TOMCAT_PATH/webapps/docs \
    $TOMCAT_PATH/webapps/examples \
    $TOMCAT_PATH/webapps/host-manager \
    $TOMCAT_PATH/webapps/manager

# Create ROOT folder
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

# Download Dependency DrugRef2.war
RUN curl -sSL -o $TOMCAT_PATH/webapps/drugref2.war $DRUGREF_DOWNLOAD_URL

# Copy scripts and config files
COPY ./scripts /scripts
COPY ./setup/setup.sh /setup-dir/setup.sh
COPY ./config/tomcat/conf/server.xml $TOMCAT_PATH/conf/server.xml
COPY ./config/shared/volumes/*.properties /root/

# Copy configuration files
COPY ./openosp_files/index.jsp $TOMCAT_PATH/webapps/ROOT/index.jsp
COPY ./openosp_files/openosp_favicon.ico $TOMCAT_PATH/webapps/ROOT/openosp_favicon.ico
COPY ./openosp_files/logging.properties $TOMCAT_PATH/conf/logging.properties
COPY ./openosp_files/logging-servlet.properties $TOMCAT_PATH/conf/logging-servlet.properties
COPY ./openosp_files/tomcat-users.xml $TOMCAT_PATH/conf/tomcat-users.xml

# Set up document directories
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

# Convert script files from DOS to Unix format (if necessary)
RUN dos2unix /scripts/* \
    && dos2unix /setup-dir/setup.sh

# Make setup script executable and run it
RUN chmod +x /setup-dir/setup.sh && /setup-dir/setup.sh

# Expose Tomcat port
EXPOSE 8080

# Clean up
RUN apt-get autoclean && apt-get clean && apt-get autoremove

# Set up Maven repository directory
RUN mkdir -p /root/.m2 \
    && chown -R root:root /root/.m2 \
    && chmod -R 777 /root/.m2

# Development CMD
CMD ["tail", "-f", "/dev/null"]
