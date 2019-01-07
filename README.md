# Manorrock Persian

This project delivers a minimal Maven repository server.

## Using the WAR file to deploy the repository server

1. Download the WAR file.
2. Set the data directory to:

    a. Set the ROOT_DIRECTORY system property to point to the data directory.

    b. Or set the java:comp/env/rootDirectory JNDI entry to point to the data 
       directory of your choice.

3. Deploy it on a JakartaEE/JavaEE server
