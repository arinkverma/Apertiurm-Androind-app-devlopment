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

package org.apertium.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * @author Stephen Tigner
 *
 */
public class IOUtils {
    private static final boolean DEBUG = false;

    public static String getFilenameMinusExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return filename.substring(0, dotIndex);
    }

    /**
     * This method checks to see if there is a a trailing slash on the string
     * path given and if not, adds it. Checks for both forward and backslashes,
     * but only adds a forward slash.
     * @param path -- The filename (path) string to check
     * @return The path string with a trailing slash added if one was missing,
     * or the same string if it was already there.
     */
    public static String addTrailingSlash(String path) {
        char lastChar = path.charAt(path.length() - 1);
        if(lastChar != '/' && lastChar != '\\') {
            /* If there is not a forward slash (unix) or backward
             * slash (Windows) at the end of the path, add a slash.
             * Java can handle mixed slashes, so only need to worry
             * about adding a forward slash.
             * The reason we aren't just using the pathSeparator system
             * property is that we might have a unix-style path on a
             * Windows system in the case of cygwin.
             */
            path += "/";
        }
        return path;
    }
    
    public static String readFile(String path) throws IOException {
        return readFile(path, "UTF-8");
    }


    public static String readFile(String path, String encoding) throws IOException {
        File fileToRead = openFile(path);
        FileInputStream fis = new FileInputStream(fileToRead);
        byte[] byteArray = new byte[(int) fileToRead.length()];
        fis.read(byteArray);
        fis.close();
        /* If we don't do it this way, by explicitly setting UTF-8 encoding
         * when reading in a file, we get mojibake (scrambled character encodings).
         */
        String fileContents = new String(byteArray, encoding);
        return fileContents;
    }

    public static void writeFile(String path, String data) throws IOException {
        writeFile(path, data, "UTF-8");
    }
    
    public static void writeFile(String path, String data, String encoding) 
            throws IOException {
        Writer output = openOutFileWriter(path);
        output.write(data);
        output.close();
    }
    
    /**
     * 
     * @return A reader for System.in with the default encoding of UTF-8.
     * @throws UnsupportedEncodingException
     */
    public static Reader getStdinReader() throws UnsupportedEncodingException {
        return getStdinReader("UTF-8");
    }
    
    public static Reader getStdinReader(String encoding) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(System.in, encoding));
    }

    /**
     * 
     * @return A writer for System.out with the default encoding of UTF-8.
     * @throws UnsupportedEncodingException
     */
    public static Writer getStdoutWriter() throws UnsupportedEncodingException {
        return getStdoutWriter("UTF-8");
    }
    
    public static Writer getStdoutWriter(String encoding) throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(System.out, encoding));
    }
    
    /**
     * Takes a filename string and a command-line label and attempts to open an input
     * stream to the file given in the filename.
     * @param filename - A string with the filename to open
     * @return An InputStream for reading from the file specified.
     * @throws FileNotFoundException 
     */
    public static InputStream openInFileStream(String filename) 
            throws FileNotFoundException {
        File file = null;
        BufferedInputStream bis = null;
       
        try {
            file = openFile(filename);
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File: " + file.getPath() + 
                    " ( " + filename + ") -- " + e.getLocalizedMessage());
        }
        
        return bis;
    }

    /**
     * 
     * @param filename -- The file to open for reading.
     * @return A reader for the file with the default UTF-8 encoding.
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static Reader openInFileReader(String filename) 
            throws UnsupportedEncodingException, FileNotFoundException {
        return openInFileReader(filename, "UTF-8");
    }
    
    public static Reader openInFileReader(String filename, String encoding) 
            throws UnsupportedEncodingException, FileNotFoundException {
        return new InputStreamReader(openInFileStream(filename), encoding);
    }
    
    /**
     * Takes a filename string and a command-line label and attempts to open an output
     * stream to the file given in the filename.
     * @param filename - A string with the filename to open
     * @return An OutputStream for writing to the file specified.
     * @throws FileNotFoundException 
     */
    public static OutputStream openOutFileStream(String filename) throws FileNotFoundException {
        File file = null;
        BufferedOutputStream bos = null;
       
        try {
            file = openFile(filename);
            bos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File: " + file.getPath() + 
                    " ( " + filename + ") -- " + e.getLocalizedMessage());
        }
        
        return bos;
    }

    /**
     * 
     * @param filename -- The file to open for writing.
     * @return A writer for the file with the default UTF-8 encoding.
     * @throws FileNotFoundException
     */
    public static Writer openOutFileWriter(String filename) throws FileNotFoundException {
        return openOutFileWriter(filename, "UTF-8");
    }
    
    public static Writer openOutFileWriter(String filename, String encoding) throws FileNotFoundException {
        return new OutputStreamWriter(openOutFileStream(filename));
    }

    public static FilenameFilter getExtensionFilter(final String extension) {
        FilenameFilter filter = new FilenameFilter() {
            private String _extension = extension;
            
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(_extension);
            }
        };
        return filter;
    }

    public static String[] listFilesInDir(String path) {
        return listFilesInDir(path, null);
    }
    
    public static String[] listFilesInDir(String path, String extension) {
        File directory = openFile(path);
        String[] fileList;
        if(extension == null) {
            fileList = directory.list();
        } else {
            fileList = directory.list(getExtensionFilter(extension));
        }
        return fileList;
    }
    
    public static File openFile(String filename) {
        filename = filename.trim();
        File file = new File(filename);
        if(!file.exists() && System.getProperty("os.name").startsWith("Windows")) {
            if(DEBUG) {
                System.err.println("*** DEBUG: Trying cygwin path...");
            }
            filename = getWindowsPathFromCygwin(filename);
            if(DEBUG) {
                System.err.println("*** DEBUG: Cygwin path -- " + filename);
            }
            if(filename != null) {
                File winFile = new File(filename);
                if(DEBUG) {
                    System.err.println("*** DEBUG: winFile.exists() -- " + 
                            winFile.exists());
                    System.err.println("*** DEBUG: winFile.getAbsolutePath() -- " + 
                            winFile.getAbsolutePath());
                }
                if(winFile.exists()) {
                    file = winFile;
                }
                /* If trying to run it through cygwin fails, just return the
                 * original file object, created with the original path.
                 */
            }
        }
        return file;
    }
    
    /**
     * Given a cygwin unix-style path, this calls the external cygwin utility
     * "cygpath" to return the equivalent Windows path.
     * This assumes that "cygpath" is on the user's path. It should be if they have
     * cygwin installed, but if it is not on the user's path, then we can't run it.
     * @param filename -- The cygwin unix-style path and filename to convert
     * @return A windows-style path for that same filename that was input.
     */
    public static String getWindowsPathFromCygwin(String filename) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Process extProcess = Runtime.getRuntime().exec("cygpath -m " + filename);
            while(true) { //Keep waiting until process is finished.
                try {
                    extProcess.waitFor();
                    /* If external process is finished, we'll get to the break
                     * statement below. If we are interrupted, we won't.
                     */
                    break;
                } catch (InterruptedException e) {
                    /* We got interrupted. Run the loop again.
                     */
                }
            }
            if(extProcess.exitValue() != 0) { 
                /* Assume process follows convention of 0 == Success.
                 * Thus if the exit value is != 0, it failed
                 */
                return null;
            }
            int currByte;
            while((currByte = extProcess.getInputStream().read()) != -1 ) {
                output.write(currByte);
            }
        } catch (Exception e) { //catch all exceptions and discard them, returning null
            return null;
        }
        
        String outputString = null; 
        try {
             outputString = output.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            //For some reason, this system doesn't support UTF-8??
            System.err.println("Exception opening file/directory: " + filename + " -- " + 
                    StringTable.UNSUPPORTED_ENCODING);
            //If the system doesn't support UTF-8, we cannot continue.
            System.exit(1);
        }
        /* If the string isn't trimmed, calls to File.exists() will return false, even when
         * the file exists.
         */
        return outputString.trim();
    }
   
}
