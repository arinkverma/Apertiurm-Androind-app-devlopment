/*
 * Copyright (C) 2012 Arink Verma
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

package org.apertium.android.Internet;

public class ManifestRow{
	/*PairName	JarURL FileName ModeID
	 * apertium-af-nl	https://apertium.svn.sourceforge.net/svnroot/apertium/builds/apertium-af-nl/apertium-af-nl.jar	file:apertium-af-nl-0.2.0.tar.gz	af-nl, nl-af*/
	
	private String packageName = null;
	private String JarURL = null;
	private String ZipURL = null;
	private String packageMode = null;
	
	public ManifestRow(String Name,String JAR,String ZIP,String Code){
		packageName = Name;
		JarURL		= JAR;
		ZipURL		= ZIP;
		packageMode	= Code;
	}
	
	public String getName(){
		return packageName;
	}
	
	public String getJarURL(){
		return JarURL;
	}
	
	public String getJarFileName(){
		return JarURL.substring(JarURL.lastIndexOf("/")+1,JarURL.length());
	}
	
	public String getZipURL(){
		return ZipURL;
	}
	
	public String getpackageMode(){
		return packageMode;
	}
}

