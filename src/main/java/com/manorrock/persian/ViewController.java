/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.persian;

import java.io.File;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import com.manorrock.oyena.lifecycle.action.ActionMapping;

/**
 * The view controller.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named(value = "viewController")
@RequestScoped
public class ViewController {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RepoResource.class.getPackageName());
    
    /**
     * Stores the HTTP servlet request.
     */
    @Inject
    private HttpServletRequest request;

    /**
     * Stores the root directory.
     */
    private File rootDirectory;

    /**
     * Stores the root directory filename.
     */
    private String rootDirectoryFilename;
    
    /**
     * Stores the repository.
     */
    private String repository;
    
    /**
     * Stores the repository size in kilo bytes.
     */
    private double repositorySize;
    
    /**
     * Get the repository.
     * 
     * @return the repository.
     */
    public String getRepository() {
        return repository;
    }
    
    /**
     * Get the repository size.
     * 
     * @return the repository size.
     */
    public double getRepositorySize() {
        return repositorySize;
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        rootDirectoryFilename = System.getenv("REPOSITORIES_DIRECTORY");
        if (rootDirectoryFilename == null) {
            rootDirectoryFilename = System.getProperty("REPOSITORIES_DIRECTORY",
                    System.getProperty("user.home") + "/.manorrock/persian/repositories");
        }

        if (LOGGER.isLoggable(INFO)) {
            LOGGER.log(INFO, "Repositories directory: {0}", rootDirectoryFilename);
        }

        rootDirectory = new File(rootDirectoryFilename);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
        
        if (request.getParameter("repository") != null) {
            repository = request.getParameter("repository");
            File repositoryDirectory = new File(rootDirectory, repository);
            repositorySize = FileUtils.sizeOfDirectory(repositoryDirectory) / 1024;
        }
    }
    
    /**
     * Handle viewing a repository.
     * 
     * @return the repository view page.
     */
    @ActionMapping("/view")
    public String index() {
        return "/WEB-INF/ui/view.xhtml";
    }
}
