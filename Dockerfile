FROM eclipse-temurin:17
ENV TOMCAT_VERSION=10.0.17
RUN cd /usr/local && \
    curl --insecure -L -O https://archive.apache.org/dist/tomcat/tomcat-10/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz && \
    tar xfvz apache-tomcat-$TOMCAT_VERSION.tar.gz && \
    mv apache-tomcat-$TOMCAT_VERSION tomcat && \
    rm apache-tomcat-$TOMCAT_VERSION.tar.gz && \
    rm -rf tomcat/webapps/*docs* && \
    rm -rf tomcat/webapps/*examples* && \
    rm -rf tomcat/webapps/*manager* && \
    rm -rf tomcat/webapps/ROOT*
WORKDIR /usr/local/tomcat
COPY target/persian.war /usr/local/tomcat/webapps/ROOT.war
CMD ["bin/catalina.sh", "run"]
