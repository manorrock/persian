/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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
package com.manorrock.persian;

import com.manorrock.persian.FileModel;
import com.manorrock.persian.DirectoryModel;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * The JUnit test for the DirectoryModel class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DirectoryModelTest {

    /**
     * Test getFiles method.
     */
    @Test
    public void testGetFiles() {
        DirectoryModel model = new DirectoryModel();
        assertNull(model.getFiles());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DirectoryModel model = new DirectoryModel();
        assertNull(model.getName());
    }

    /**
     * Test setFiles method.
     */
    @Test
    public void testSetFiles() {
        List<FileModel> files = new ArrayList<>();
        DirectoryModel model = new DirectoryModel();
        model.setFiles(files);
        assertNotNull(model.getFiles());
    }

    /**
     * Test setName method.
     */
    @Test
    public void testSetName() {
        DirectoryModel model = new DirectoryModel();
        model.setName("TheName");
        assertEquals("TheName", model.getName());
    }
}
