/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package com.manorrock.persian.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**
 * The index bean.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named(value = "indexController")
@RequestScoped
public class UIIndexController {
    
    /**
     * Stores the application bean.
     */
    @Inject
    private UIApplicationBean application;

    /**
     * Stores the repositories.
     */
    private List<UIMavenRepositoryModel> repositories;
    
    /**
     * Get the repositories.
     * 
     * @return the repositories.
     */
    public List<UIMavenRepositoryModel> getRepositories() {
        return repositories;
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        
        File[] repositoryFilenames = application.getRootDirectory().listFiles();
        repositories = new ArrayList<>();
        if (repositoryFilenames != null) {
            for (File repositoryFilename : repositoryFilenames) {
                repositories.add(loadMavenRepository(repositoryFilename));
            }
        }
    }
    
    /**
     * Load a Maven repository.
     * 
     * @param file the file.
     * @return the Maven repository.
     */
    public UIMavenRepositoryModel loadMavenRepository(File file) {
        UIMavenRepositoryModel repository = new UIMavenRepositoryModel();
        repository.setName(file.getName());
        long size = -1L;
        try {
            size = Files.walk(file.toPath())
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();
        } catch(IOException ioe) {
        }
        repository.setSize(size);
        return repository;
    }
}
