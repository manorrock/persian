package com.manorrock.persian.rest;

import org.apache.maven.cli.MavenCli;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

@RunWith(Arquillian.class)
public class UploadJarIT {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(UploadJarIT.class);
    }

    @ArquillianResource
    private URL baseURL;

    @Test
    public void testUploadJar() throws Exception {
        MavenCli cli = new MavenCli();
        int result = cli.doMain(new String[]{"deploy", "-DaltDeploymentRepository=release::default::http://localhost:8080/persian/repositories/release/"}, ".", System.out, System.out);
        assert result == 0 : "Failed to deploy JAR, Maven deploy goal returned: " + result;
    }
}
