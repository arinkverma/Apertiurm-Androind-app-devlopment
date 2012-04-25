package org.apertium.transfer;

import static org.apertium.utils.IOUtils.openFile;
import static org.apertium.utils.IOUtils.getFilenameMinusExtension;
import static org.apertium.utils.IOUtils.addTrailingSlash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apertium.transfer.compile.ApertiumTransferCompile;
import org.apertium.transfer.compile.ParseTransferFile;
import org.xml.sax.SAXException;

import android.util.Log;

public class TransferClassLoader extends ClassLoader {

    public TransferClassLoader() {
        super(TransferClassLoader.class.getClassLoader());
    }

    @SuppressWarnings("unchecked")
    public Class loadClassFile(String filename) throws ClassNotFoundException, IOException {
        return loadClassFile(openFile(filename));
    }
    
    @SuppressWarnings("unchecked")
    public Class loadClassFile(File classFile) throws ClassNotFoundException, IOException {
        // System.err.println("filename = " + filename);
        /* The reason for the static import of openFile, when we could just
         * do new File(filename), is to allow for future code centered in IOUtils.
         * This code will help deal with issues of disparate path notations, specifically
         * in the case of running this Java code on a Windows installation that has
         * the C++ code running under cygwin.
         */
        InputStream input = new FileInputStream(classFile);
        byte data[] = new byte[(int) classFile.length()];
        input.read(data);
        input.close();
        return defineClass(null, data, 0, data.length);
    }

    @SuppressWarnings("unchecked")
    public static Class loadTxClass(File txOrClassFile, File binFile)
            throws ClassNotFoundException, IOException {
        return loadTxClass(txOrClassFile, binFile, new TransferClassLoader());
    }

    
    @SuppressWarnings("unchecked")
    public static Class loadTxClass(File txOrClassFile, File binFile, TransferClassLoader tcl)
            throws ClassNotFoundException, IOException {

        //System.err.println("binFile = " + binFile);
        //System.err.println("txFile = " + txOrClassFile);

        if (!txOrClassFile.exists()) {
            throw new FileNotFoundException("Loading TX Class txFile ("
                    + txOrClassFile + ")");
        }
        if (!binFile.exists()) {
            throw new FileNotFoundException("Loading TX Class binFile ("
                    + txOrClassFile + ")");
        }


        if (txOrClassFile.getName().endsWith(".class") && txOrClassFile.exists()) {
            return tcl.loadClassFile(txOrClassFile);
        } else {
            return buildAndLoadClass(txOrClassFile, binFile, tcl);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class buildAndLoadClass(File txFile, File binFile,
            TransferClassLoader tcl) throws ClassNotFoundException, IOException {
        
        String baseBinFilename = getFilenameMinusExtension(binFile.getName());
        String classFilename = baseBinFilename + ".class";

        /* If I made this a File, it would lose the slash or backslash at the end
         * and there's no reason to make this a File object, so keeping it just as
         * a string.
         */
        
        String tempDir = System.getProperty("java.io.tmpdir");
        tempDir = addTrailingSlash(tempDir);
        
        File classFile = openFile(addTrailingSlash(binFile.getParent()) + classFilename);
        Log.i("----000",""+classFile.exists());
        // If it doesn't exist in the binFile directory, try the temp directory
        if (!classFile.exists()) {
            classFile = openFile(tempDir + classFilename);
            Log.i("Info","---1");
        }
        
        // If it doesn't exist there either, switch back to the binFile
        // directory
        if (!classFile.exists()) {
            classFile = openFile(addTrailingSlash(binFile.getParent()) + classFilename);
            Log.i("Info","---2");
        }

        // If the class file exists already, try and load it.
        if (classFile.exists()) {
            return tcl.loadClassFile(classFile);
        }

        // Generate the java source.
        ParseTransferFile p = new ParseTransferFile();
        try {
            p.parse(txFile.getPath());
        } catch (ParserConfigurationException e) {
            throw new InternalError("TX File (" + txFile
                    + ") parsing failed -- " + e.getLocalizedMessage());
        } catch (SAXException e) {
            throw new InternalError("TX File (" + txFile
                    + ") parsing failed -- " + e.getLocalizedMessage());
        }

        File javaSource = openFile(addTrailingSlash(binFile.getParent()) + 
                p.className + ".java");
        File outputClass = openFile(addTrailingSlash(binFile.getParent()) 
                + p.className + ".class");

        // Write the source out to a file
        FileWriter fw = null;
        try {
            fw = new FileWriter(javaSource);
        } catch (IOException e) { //Unable to open output file for writing
            javaSource = openFile(tempDir +  p.className + ".java");
            outputClass = openFile(tempDir + p.className + ".class");
            fw = new FileWriter(javaSource);
        }
        fw.append(p.getOptimizedJavaCode());
        fw.close();

        // Compile the source file
        File actualClassFile = ApertiumTransferCompile.doMain(txFile, classFile, javaSource,
                outputClass, false);
        if(actualClassFile == null) {
            /* We weren't able to get the compiled output file to rename
             * to the desired classFile name, or to the same filename in
             * the system temp directory. Throw an exception.
             */
            throw new ClassNotFoundException(
                    "Unable to write compiled transfer class to specified " +
                    "or system temp directories.");
        }

        //Remove the java source file, as we don't need it anymore
        javaSource.delete();
        
        // Load and return the class file.
        return tcl.loadClassFile(actualClassFile);
    }
}
