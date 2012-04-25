/*
 * Copyright (C) 2010 Stephen
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Stephen
 *
 */
public class ExternalTransferCompile extends BaseTransferCompile {

    /**
     * Calls the external javac compiler using exec.
     * @param input -- stdin InputStream for compilation process, 
     * may be <code>null</code>
     * @param output -- stdout OutputStream for compilation process, 
     * may be <code>null</code>
     * @param errOutput -- stderr OutputStream for compilation process, 
     * may be <code>null</code>
     * @param classPath -- A file/path to add to the classpath when compiling
     * @parem javaFile -- The source file to compile
     * @return An int representing the return value of the compiler, typically
     * with 0 meaning success, and any other value meaning failure.
     * @throws IOException
     */
    public int compile(InputStream input, OutputStream output, OutputStream errOutput,
            File classPath, File javaFile) throws IOException {
        Process p = null;
        verifyFilesExist(classPath, javaFile);
        String commandString = "javac -cp " + classPath + " " + javaFile;
        try {
            p = Runtime.getRuntime().exec(commandString);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Transfer compile (javac) --" + 
                    e.getLocalizedMessage());
        }
        while(true) {
            try {
                p.waitFor();
                /* If the process finishes before we are interrupted, then we'll 
                 * make it to the break. If not, then we'll loop and wait again.
                 */
                break;
            } catch (InterruptedException e) {
                //do nothing
            }
        }
        int currChar;
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        if(output != null) {
            while ((currChar = br.read()) != -1)
                output.write(currChar);
            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((currChar = br.read()) != -1)
                output.write(currChar);
        }
        return p.exitValue();

    }
    
}
