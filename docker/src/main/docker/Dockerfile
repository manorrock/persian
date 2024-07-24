FROM ghcr.io/piranhacloud/platform:latest
COPY target/wars/persian.war /home/piranha/piranha/webapps/repo.war
COPy target/wars/persian-ui.war /home/piranha/piranha/webapps/ui.war
USER root
RUN chown -R piranha:piranha /home/piranha
WORKDIR /home/piranha/piranha/bin
USER piranha
CMD ["/bin/bash", "./run.sh"]
