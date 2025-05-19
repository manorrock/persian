# Manorrock Persian

## ⚠️ Project Archival Notice

This project is part of the Manorrock Sustainability Initiative. We are seeking new maintainers to take over this project. If no maintainers step forward by December 31, 2025, this repository will be archived and moved to the manorrock-attic organization.

### Project Timeline

- **Through December 31, 2025**: Repository remains active while seeking maintainers
- **After December 31, 2025**: If no maintainers found, project moves to manorrock-attic
- **Until December 31, 2030**: Project remains available read-only in the attic
- **After December 31, 2030**: Project may be removed

### Interested in Maintaining This Project?

If you're interested in becoming a maintainer, please see [this GitHub issue](https://github.com/manorrock/persian/issues/264) for details on how to express your interest and what's involved. Note that new maintainers will need to migrate the project to a new namespace, as the Manorrock branding will remain with Manorrock.com.

**After December 31, 2025**: If this project moves to the manorrock-attic, GitHub issues will no longer be available. If you become interested in maintaining this project after it's archived, please email info@manorrock.com with the subject "Revival Request: [Project Name]".

### More Information

For more information about the Manorrock Projects Sustainability Initiative, please visit our [blog post](https://www.manorrock.com/blog/2025/04/14/manorrock_sustainability_initiative.html).

---

[![build](https://github.com/manorrock/persian/actions/workflows/build.yml/badge.svg)](https://github.com/manorrock/persian/actions/workflows/build.yml)

This project delivers you with a Maven repository server.

## Running using the container image from GHCR

In an EMPTY directory of your choice use the following command line to start 
Manorrock Persian:

```shell
  docker run --rm -d -it -p 8080:8080 -v $PWD:/mnt ghcr.io/manorrock/persian
```

## Verify the server is up and running

Create a `settings.xml` file with the content from the snippet below, or rename
the `settings.xml.template` in the root directory of this project to 
`settings.xml` and make adjustments if necessary:

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
                    <url>http://localhost:8080/persian/repositories/myrepo</url>
                    <layout>default</layout>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

Now pick any Maven project and execute the command below to upload the 
artifacts:

```
mvn deploy -DaltDeploymentRepository=default::default::http://localhost:8080/persian/repositories/myrepo
```

You should see output similar to what you see below:

```
Uploaded to default: http://localhost:8080/persian/repositories/myrepo/com/manorrock/persian/persian/maven-metadata.xml
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

Congratulations you are now running Manorrock Persian!

## More information about deploying to a Maven repository

See [Deploy](https://books.sonatype.com/mvnref-book/reference/lifecycle-sect-common-goals.html#lifecycle-sect-deploy-phase)
at the Maven: The Complete Reference. Or have a look at the [deploy:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/deploy-mojo.html)
goal of the Maven Deploy plugin.

## How do I contribute?

See [Contributing](CONTRIBUTING.md)

## Our code of Conduct

See [Code of Conduct](CODE_OF_CONDUCT.md)

## Important notice

Note if you file issues or answer questions on the issue tracker and/or issue 
pull requests you agree that those contributions will be owned by Manorrock.com
and that Manorrock.com can use those contributions in any manner Manorrock.com
so desires.
