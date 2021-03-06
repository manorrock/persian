# Manorrock Persian

This project delivers you with a Maven repository server.

## Deploy the server using Docker

```
  docker run --rm -d -p 8080:8080 -v $PWD:/root/.manorrock/persian manorrock/persian:VERSION
```

And replace VERSION with the version you want to use.

> _Note_ in the command line above we have mapped the 
> `/root/.manorrock/persian` directory to point to the current directory so
> we can persist the Maven repositories outside of the container.

## Verify the server is up and running

Create a `settings.xml` file with the content from the snippet below, or rename
the `settings.xml.template` in the root directory of this project to 
`settings.xml`:

```
<?xml version="1.0" encoding="UTF-8"?>

<settings 
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd" 
    xmlns="http://maven.apache.org/SETTINGS/1.1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <profiles>
        <profile>
            <id>default</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <repositories>
                <repository>
                    <id>localhost</id>
                    <name>localhost</name>
                    <releases>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>ignore</checksumPolicy>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>ignore</checksumPolicy>
                    </snapshots>
                    <url>http://localhost:8080/repositories</url>
                    <layout>default</layout>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

Now pick any Maven project and execute the command below to upload the artifacts:

```
mvn deploy -DaltDeploymentRepository=default::default::http://localhost:8080/repositories/test
```

The end of the output should be similar to what you see below:

```
Uploaded to default: http://localhost:8080/repositories/test/com/manorrock/persian/persian/maven-metadata.xml (578 B at 95 B/s)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  44.961 s
[INFO] Finished at: 2020-04-14T22:48:32-06:00
[INFO] ------------------------------------------------------------------------
```

Congratulations you are now running Manorrock Persian!

## More information about deploying to a Maven repository

See [Deploy](https://books.sonatype.com/mvnref-book/reference/lifecycle-sect-common-goals.html#lifecycle-sect-deploy-phase)
at the Maven: The Complete Reference. Or have a look at the [deploy:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/deploy-mojo.html)
goal of the Maven Deploy plugin.

## Testing SNAPSHOT versions

Every night we push a SNAPSHOT version to Docker Hub. If you want to give the
version under development a test drive use `snapshot` as the version for the
instructions above.

## Important notice

Note if you file issues or answer questions on the issue tracker and/or issue 
pull requests you agree that those contributions will be owned by Manorrock.com
and that Manorrock.com can use those contributions in any manner Manorrock.com
so desires.
