/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.maven.repos;

import com.manorrock.repos.model.DirectoryModel;
import com.manorrock.repos.model.FileModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * A repo resource
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("{repositoryName}")
public class RepoResource {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RepoResource.class.getName());

    /**
     * Stores the root directory.
     */
    private File rootDirectory;

    /**
     * Stores the root directory filename.
     */
    private String rootDirectoryFilename;

    /**
     * Constructor.
     */
    public RepoResource() {
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        if (rootDirectoryFilename == null) {
            try {
                InitialContext initialContext = new InitialContext();
                rootDirectoryFilename = (String) initialContext.lookup("java:comp/env/mavenReposDirectory");
            } catch (NamingException ne) {
                rootDirectoryFilename = null;
            }
        }

        if (rootDirectoryFilename == null || "".equals(rootDirectoryFilename.trim())) {
            rootDirectoryFilename = System.getenv("MAVEN_REPOS_DIRECTORY");
        }

        if (rootDirectoryFilename == null || "".equals(rootDirectoryFilename.trim())) {
            rootDirectoryFilename = System.getProperty("MAVEN_REPOS_DIRECTORY",
                    System.getProperty("user.home") + "/.manorrock/maven/repos");
        }

        rootDirectory = new File(rootDirectoryFilename);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
    }

    /**
     * Get the base directory listing for the repository.
     *
     * @param repositoryName the repository name
     * @return the director listing.
     */
    @GET
    @Path("")
    public Response directory0(@PathParam("repositoryName") String repositoryName) {
        Response result;

        try {
            File file = new File(rootDirectory, repositoryName);

            if (file.exists()) {
                DirectoryModel model = new DirectoryModel();
                model.setName(repositoryName);
                ArrayList<FileModel> files = new ArrayList<>();
                for (File currentFile : file.listFiles()) {
                    String name = currentFile.getAbsolutePath().substring(file.getCanonicalPath().length() + 1);
                    name = name.replaceAll("&", "&amp;");
                    boolean show = true;

                    if (System.getProperty("os.name").toLowerCase().contains("mac os")
                            && name.startsWith(".")) {
                        show = false;
                    }

                    if (show) {
                        FileModel fileModel = new FileModel();
                        fileModel.setName(name);
                        fileModel.setDirectory(currentFile.isDirectory());
                        files.add(fileModel);
                    }
                }
                model.setFiles(files);
                result = Response.ok(model).build();
            } else {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Unable to read directory", ioe);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    /**
     * Get the directory listing for the given path.
     *
     * @param repositoryName the repository name.
     * @param path the path.
     * @return the view showing the directory listing.
     */
    @GET
    @Path("{path : .+}")
    public Response directory1(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("path") String path) {
        Response result;

        try {
            File file = new File(rootDirectory, repositoryName + File.separator + path);

            if (file.exists()) {
                DirectoryModel model = new DirectoryModel();
                model.setName(repositoryName + File.separator + path);
                ArrayList<FileModel> files = new ArrayList<>();
                for (File currentFile : file.listFiles()) {
                    String name = currentFile.getAbsolutePath().substring(file.getCanonicalPath().length() + 1);
                    name = name.replaceAll("&", "&amp;");
                    boolean show = true;

                    if (System.getProperty("os.name").toLowerCase().contains("mac os")
                            && name.startsWith(".")) {
                        show = false;
                    }

                    if (show) {
                        FileModel fileModel = new FileModel();
                        fileModel.setName(name);
                        fileModel.setDirectory(currentFile.isDirectory());
                        files.add(fileModel);
                    }
                }
                model.setFiles(files);
                result = Response.ok(model).build();
            } else {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Unable to read directory", ioe);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    /**
     * Download the artifact.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version
     * @param packaging the packaging.
     * @return the artifact.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/{artifactId2}-{version2}.{packaging}")
    @Produces({"application/octet-stream"})
    public StreamingOutput downloadArtifact(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            @PathParam("packaging") String packaging) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + artifactId + "-" + version + "." + packaging);
            try (InputStream inputStream = new FileInputStream(file)) {
                int nextByte;
                while ((nextByte = inputStream.read()) != -1) {
                    outputStream.write(nextByte);
                }
                outputStream.flush();
                outputStream.close();
            }
        };
    }

    /**
     * Download the artifact MD5.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version
     * @param packaging the packaging.
     * @return the artifact MD5.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/{artifactId2}-{version2}.{packaging}.md5")
    @Produces({"application/octet-stream"})
    public StreamingOutput downloadArtifactMd5(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            @PathParam("packaging") String packaging) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + artifactId + "-" + version + "." + packaging + ".md5");
            try (InputStream inputStream = new FileInputStream(file)) {
                int nextByte;
                while ((nextByte = inputStream.read()) != -1) {
                    outputStream.write(nextByte);
                }
                outputStream.flush();
                outputStream.close();
            }
        };
    }

    /**
     * Download the artifact SHA1.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version
     * @param packaging the packaging.
     * @return the artifact SHA1.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/{artifactId2}-{version2}.{packaging}.sha1")
    @Produces({"application/octet-stream"})
    public StreamingOutput downloadArtifactSha1(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            @PathParam("packaging") String packaging) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + artifactId + "-" + version + "." + packaging + ".sha1");
            try (InputStream inputStream = new FileInputStream(file)) {
                int nextByte;
                while ((nextByte = inputStream.read()) != -1) {
                    outputStream.write(nextByte);
                }
                outputStream.flush();
                outputStream.close();
            }
        };
    }

    /**
     * Download the maven-metadata.xml.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @return the maven-metadata.xml.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml")
    @Produces({"text/xml"})
    public StreamingOutput downloadMetadata(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + "maven-metadata.xml");

            if (file.exists()) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    int nextByte;
                    while ((nextByte = inputStream.read()) != -1) {
                        outputStream.write(nextByte);
                    }
                    outputStream.flush();
                    outputStream.close();
                }
            }
        };
    }

    /**
     * Download the maven-metadata.xml MD5.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @return the maven-metadata.xml,md5.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml.md5")
    @Produces({"text/xml"})
    public StreamingOutput downloadMetadataMd5(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + "maven-metadata.xml.md5");

            if (file.exists()) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    int nextByte;
                    while ((nextByte = inputStream.read()) != -1) {
                        outputStream.write(nextByte);
                    }
                    outputStream.flush();
                    outputStream.close();
                }
            }
        };
    }

    /**
     * Download the maven-metadata.xml SHA-1.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @return the maven-metadata.xml.
     */
    @GET
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml.sha1")
    @Produces({"text/xml"})
    public StreamingOutput downloadMetadataSha1(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version) {

        return (OutputStream outputStream) -> {
            File repoDir = new File(rootDirectory, repositoryName);
            File file = new File(repoDir, groupId + File.separator + artifactId + File.separator + version
                    + File.separator + "maven-metadata.xml.sha1");

            if (file.exists()) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    int nextByte;
                    while ((nextByte = inputStream.read()) != -1) {
                        outputStream.write(nextByte);
                    }
                    outputStream.flush();
                    outputStream.close();
                }
            }
        };
    }

    /**
     * Upload an artifact.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifactId.
     * @param packaging the packaging.
     * @param version the version.
     * @param data the actual artifact binary data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId1}/{version1}/{artifactId2}-{version2}.{packaging}")
    @Consumes({"application/octet-stream"})
    public void uploadArtifact(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId1") String artifactId,
            @PathParam("version1") String version,
            @PathParam("packaging") String packaging,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName
                + File.separator
                + groupId.replaceAll("/", File.separator)
                + File.separator
                + artifactId
                + File.separator
                + version);

        File file = new File(directory, artifactId + "-" + version + "." + packaging);

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }

    /**
     * Upload an artifact's MD5.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifactId.
     * @param packaging the packaging.
     * @param version the version.
     * @param data the actual artifact binary data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId}/{version}/{artifactId2}-{version2}.{packaging}.md5")
    @Consumes({"application/octet-stream"})
    public void uploadArtifactMd5(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            @PathParam("packaging") String packaging,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName + File.separator
                + groupId.replaceAll("/", File.separator) + File.separator
                + artifactId + File.separator
                + version);

        File file = new File(directory, artifactId + "-" + version + "." + packaging + ".md5");

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }

    /**
     * Upload an artifact's SHA-1.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifactId.
     * @param packaging the packaging.
     * @param version the version.
     * @param data the actual artifact binary data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId}/{version}/{artifactId2}-{version2}.{packaging}.sha1")
    @Consumes({"application/octet-stream"})
    public void uploadArtifactSha1(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            @PathParam("packaging") String packaging,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName
                + File.separator
                + groupId.replaceAll("/", File.separator)
                + File.separator
                + artifactId + File.separator
                + version);

        File file = new File(directory, artifactId + "-" + version + "." + packaging + ".sha1");

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }

    /**
     * Upload the maven-metadata.xml.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @param data the data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml")
    @Consumes({"text/xml"})
    public void uploadMetadata(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName
                + File.separator
                + groupId.replaceAll("/", File.separator)
                + File.separator
                + artifactId);

        File file = new File(directory, "maven-metadata.xml");

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }

    /**
     * Upload the maven-metadata.xml,md5.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @param data the data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml.md5")
    @Consumes({"text/xml"})
    public void uploadMetadataMd5(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName
                + File.separator
                + groupId.replaceAll("/", File.separator)
                + File.separator
                + artifactId);

        File file = new File(directory, "maven-metadata.xml.md5");

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }

    /**
     * Upload the maven-metadata.xml SHA-1.
     *
     * @param repositoryName the repository name.
     * @param groupId the group id (in slash format).
     * @param artifactId the artifact id.
     * @param version the version.
     * @param data the data.
     */
    @PUT
    @Path("{groupId : .+}/{artifactId}/{version}/maven-metadata.xml.sha1")
    @Consumes({"text/xml"})
    public void uploadMetadataSha1(
            @PathParam("repositoryName") String repositoryName,
            @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId,
            @PathParam("version") String version,
            byte[] data
    ) {

        File directory = new File(rootDirectory, repositoryName
                + File.separator
                + groupId.replaceAll("/", File.separator)
                + File.separator
                + artifactId);

        File file = new File(directory, "maven-metadata.xml.sha1");

        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
            } catch (IOException ex) {
                throw new WebApplicationException(ex, 500);
            }
        }
    }
}
