FROM manorrock/zulu:11.0.5
ENV PAYARA_VERSION=5.201
RUN apt-get update && \
    apt-get install -y curl && \
    curl -O https://repo.maven.apache.org/maven2/fish/payara/extras/payara-micro/$PAYARA_VERSION/payara-micro-$PAYARA_VERSION.jar && \
    mkdir -p /usr/local/payara/deployments && \
    mv payara-micro-$PAYARA_VERSION.jar /usr/local/payara/payara-micro.jar && \
    rm -rf /var/lib/apt/lists/*
CMD ["java", "-jar", "/usr/local/payara/payara-micro.jar", "--deploymentDir", "/usr/local/payara/deployments"]
WORKDIR /mnt
COPY target/persian.war /usr/local/payara/deployments/ROOT.war
