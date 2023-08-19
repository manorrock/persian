# Manorrock Persian

[![build](https://github.com/manorrock/persian/actions/workflows/build.yml/badge.svg)](https://github.com/manorrock/persian/actions/workflows/build.yml)

This project delivers you with a Maven repository server.

## Deploy the WAR file

Deploy the WAR file to a Jakarta Web Profile 10 compatible runtime of your choice.

## Running Manorrock Persian using Piranha Web Profile

The command line below shows you how you can start running Manorrock Persian on Piranha Web Profile.

```shell
  java -jar piranha-dist-webprofile.jar --war-file persian.war
```

## Verify the server is up and running

 Note the instruction below assume you have deployed the WAR file using the root context. 
 
 If you deployed it with another context root, please adjust the `url` below

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
                    <url>http://localhost:8080/repository</url>
                    <layout>default</layout>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

Now pick any Maven project and execute the command below to upload the artifacts:

```
mvn deploy -DaltDeploymentRepository=default::default::http://localhost:8080/repository
```

You should see output similar to what you see below:

```
Uploaded to default: http://localhost:8080/repository/com/manorrock/persian/persian/maven-metadata.xml
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

Congratulations you are now running Manorrock Persian!

## More information about deploying to a Maven repository

See [Deploy](https://books.sonatype.com/mvnref-book/reference/lifecycle-sect-common-goals.html#lifecycle-sect-deploy-phase)
at the Maven: The Complete Reference. Or have a look at the [deploy:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/deploy-mojo.html)
goal of the Maven Deploy plugin.

## Important notice

Note if you file issues or answer questions on the issue tracker and/or issue 
pull requests you agree that those contributions will be owned by Manorrock.com
and that Manorrock.com can use those contributions in any manner Manorrock.com
so desires.

## Can I use a differen directory for the data

Yes, you can. Set the PERSIAN_REPOSITORY_DIRECTORY environment variable to a
directory of your choice and Manorrock Persian will store the data there.
