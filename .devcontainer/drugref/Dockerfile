FROM tomcat:9.0.97-jdk11-temurin

# Set environment variables for Tomcat
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Labels
LABEL author="OpenOSP" \
      version="1.0.0" \
      description="OpenOSP DrugRef2 Container - Java 11 for legacy dependency compatibility"

ARG DRUGREF_REPO=https://github.com/open-osp/Open-Drugref.git
ARG DRUGREF_BRANCH=main
ARG TOMCAT_PATH=/usr/local/tomcat

WORKDIR /build

# Install required packages
RUN apt-get update && apt-get install -y --no-install-recommends \
    git maven curl \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Remove unnecessary default www apps in Tomcat
RUN rm -rf $TOMCAT_PATH/webapps/ROOT/* \
    $TOMCAT_PATH/webapps/docs \
    $TOMCAT_PATH/webapps/examples \
    $TOMCAT_PATH/webapps/host-manager \
    $TOMCAT_PATH/webapps/manager

# Clone and build Open-Drugref from source
RUN git clone --branch $DRUGREF_BRANCH $DRUGREF_REPO /build/drugref

WORKDIR /build/drugref

# Build drugref2.war (skip tests - they should pass in CI)
RUN mvn -Dmaven.test.skip=true clean package

# Deploy the WAR file
RUN cp /build/drugref/target/drugref2.war $TOMCAT_PATH/webapps/drugref2.war

# Copy drugref2.properties (will be mounted at runtime, but provide default location)
COPY drugref2.properties /root/drugref2.properties

# Clean up build artifacts to reduce image size
RUN rm -rf /build /root/.m2

WORKDIR $CATALINA_HOME

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
