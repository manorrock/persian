FROM ghcr.io/piranhacloud/coreprofile:25.2.0
COPY target/persian.war persian.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-coreprofile.jar", "--war-file", "persian.war", "--verbose"]
