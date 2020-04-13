# Manorrock Persian

This project delivers a Maven repository server.

## Using the Docker image to deploy the Maven repository server.

```
  docker run --rm -d -p 8080:8080 -v $PWD:/mnt manorrock/persian:VERSION
```

And replace VERSION with the version you want to use.
