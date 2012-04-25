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

/**
 * @author Stephen Tigner
 *
 */
public class StringTable {

    public static final String UNSUPPORTED_ENCODING = 
        "Your system does not support UTF-8 " + 
        "encoding. Cannot continue. Please enable UTF-8 support, or find " +
        "a system that supports UTF-8 and try again.";

    public static final String FILE_NOT_FOUND = 
        "Unable to find and/or open the specified file, please check " +
        "the filename and try again.";

    public static final String GENERIC_EXCEPTION =
        "An exception occured during execution.";

    public static final String IO_EXCEPTION =
        "An I/O exception occured during execution.";

    public static final String UNEXPECTED_FILE_NOT_FOUND =
        "Got a FileNotFoundException whle parsing the commandLine from " +
        "the modes file. This should not happen, so you must have found " + 
        "a bug. Here's a stack trace to help track that down.";

    public static final String UNEXPECTED_UNSUPPORTED_ENCODING =
        "Got an UnsupportedEncodingException whle parsing the commandLine " +
        "from the modes file. This should not happen, so you must have " + 
        "found a bug. Here's a stack trace to help track that down.";

    public static final String COMPILATION_FAILURE =
        "Prechunk/Interchunk/Transfer compilation failed.";

}
