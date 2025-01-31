FROM ghcr.io/piranhacloud/webprofile:25.1.0
COPY target/persian.war persian.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "persian.war", "--verbose"]
