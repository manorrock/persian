FROM piranhacloud/webprofile:v23.1.0
COPY target/persian.war ROOT.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "ROOT.war", "--webapp-directory", "ROOT", "--verbose"]
