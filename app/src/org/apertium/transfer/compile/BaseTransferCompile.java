/*
 * Copyright (C) 2010 Stephen Tigner
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */

package org.apertium.transfer.compile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apertium.utils.StringTable;

/**
 * @author Stephen Tigner
 *
 */
public abstract class BaseTransferCompile {
    //None of these are static because you can't have static abstract methods
    
    /**
     * A convenience method for {@link BaseTransferCompiler#compile
     * (InputStream, OutputStream, OutputStream, String[])}.<br>
     * Equivalent to <code>compile(null, null, null, args)</code>
     * may be <code>null</code>
     * @param classPath -- A file/path to add to the classpath when compiling
     * @parem javaFile -- The source file to compile
     * @throws IOException 
     */
    public int compile(File classPath, File javaFile) throws IOException {
        return compile(null, null, null, classPath, javaFile);
    }

    /**
     * A convenience method for {@link BaseTransferCompiler#compile
     * (InputStream, OutputStream, OutputStream, String[])}.<br>
     * Equivalent to <code>compile(input, output, null, args)</code>
     * @param input -- stdin InputStream for compilation process, 
     * may be <code>null</code>
     * @param output -- stdout OutputStream for compilation process, 
     * may be <code>null</code>
     * @param classPath -- A file/path to add to the classpath when compiling
     * @parem javaFile -- The source file to compile
     * @throws IOException 
     */
    public int compile(InputStream input, OutputStream output, File classPath, 
            File javaFile) throws IOException {
        return compile(input, output, null, classPath, javaFile);
    }
    
    public abstract int compile(InputStream input, OutputStream output, OutputStream errOutput,
            File classPath, File javaFile) throws IOException;
    
    protected static void verifyFilesExist(File classPath, File javaFile) 
            throws FileNotFoundException {
        if(!classPath.exists()) {
            throw new FileNotFoundException("Transfer compile classpath (" + 
                    classPath + " -- " + StringTable.FILE_NOT_FOUND);
        }
        if(!javaFile.exists()) {
            throw new FileNotFoundException("Transfer compile source file (" + 
                    javaFile + " -- " + StringTable.FILE_NOT_FOUND);
        }
    }
}
