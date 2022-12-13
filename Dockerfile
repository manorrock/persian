FROM piranhacloud/servlet:v22.12.0
COPY target/persian.war ROOT.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-servlet.jar", "--war-file", "ROOT.war", "--verbose"]
