FROM debian
ENV PIRANHA_VERSION=20.4.0
ENV ZULU_VERSION=zulu11.37.17-ca-jdk11.0.6
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y curl tar gzip && \
    cd /usr/local && \
    curl --insecure -L -O https://cdn.azul.com/zulu/bin/${ZULU_VERSION}-linux_x64.tar.gz && \
    tar xfvz ${ZULU_VERSION}-linux_x64.tar.gz && \
    mv ${ZULU_VERSION}-linux_x64 jdk && \
    rm ${ZULU_VERSION}-linux_x64.tar.gz && \
    curl --insecure -L -O https://repo1.maven.org/maven2/cloud/piranha/piranha-micro/${PIRANHA_VERSION}/piranha-micro-${PIRANHA_VERSION}-all.jar && \
    mkdir -p /usr/local/piranha && \
    mv piranha-micro-${PIRANHA_VERSION}-all.jar /usr/local/piranha/piranha-micro.jar && \
    rm -rf /var/lib/apt/lists/*
ENV PATH=$PATH:/usr/local/jdk/bin
WORKDIR /usr/local/piranha
CMD ["java", "-Djava.util.logging.config.file=/usr/local/piranha/logging.properties", "-jar", "piranha-micro.jar", "--war", "ROOT.war"]
COPY src/main/docker/logging.properties /usr/local/piranha/logging.properties
COPY target/persian.war /usr/local/piranha/ROOT.war
