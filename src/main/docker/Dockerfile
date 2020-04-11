FROM debian
ENV PAYARA_VERSION=5.201
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y curl tar gzip && \
    cd /usr/local && \
    curl --insecure -L -O https://cdn.azul.com/zulu/bin/zulu11.35.15-ca-jdk11.0.5-linux_x64.tar.gz && \
    tar xfvz zulu11.35.15-ca-jdk11.0.5-linux_x64.tar.gz && \
    mv zulu11.35.15-ca-jdk11.0.5-linux_x64 jdk && \
    rm zulu11.35.15-ca-jdk11.0.5-linux_x64.tar.gz && \
    curl --insecure -L -O https://repo.maven.apache.org/maven2/fish/payara/extras/payara-micro/$PAYARA_VERSION/payara-micro-$PAYARA_VERSION.jar && \
    mkdir -p /usr/local/payara/deployments && \
    mv payara-micro-$PAYARA_VERSION.jar /usr/local/payara/payara-micro.jar && \
    rm -rf /var/lib/apt/lists/*
ENV PATH=$PATH:/usr/local/jdk/bin
CMD ["java", "-jar", "/usr/local/payara/payara-micro.jar", "--deploymentDir", "/usr/local/payara/deployments"]
WORKDIR /mnt
COPY target/persian.war /usr/local/payara/deployments/ROOT.war
