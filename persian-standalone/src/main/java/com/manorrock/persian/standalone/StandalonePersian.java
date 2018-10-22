/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
package com.manorrock.persian.standalone;

import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.DefaultWebApplicationServer;
import com.manorrock.piranha.api.HttpServer;
import javax.servlet.ServletRegistration;

/**
 * The boot strapper for a standalone version of Persian.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class StandalonePersian {

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(8080, server);
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServletMapping("Jersey", "/repo/*");
        webApp.addServlet("Jersey", "org.glassfish.jersey.servlet.ServletContainer");
        ServletRegistration registration = webApp.getServletRegistration("Jersey");
        registration.setInitParameter("jersey.config.server.provider.packages", "com.manorrock.persian");
        server.addWebApplication(webApp);
        server.initialize();
        server.start();
        httpServer.start();
        while(httpServer.isRunning()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {
            }
        }
        httpServer.stop();
        server.stop();
    }
}
