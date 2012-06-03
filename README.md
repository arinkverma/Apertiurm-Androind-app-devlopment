Apertium on your mobile
=========
Port Apertium to Android and possibly iPhone. Apertium has a Java port which doesn't currently work on mobile telephones. Lots of people have mobile telephones, and some of them would like to have a translator there.


Features [version 2.2.2]
=========
<ul>
	<li>Compatible with project <a href="http://apertium.svn.sourceforge.net/viewvc/apertium/branches/gsoc2012/artetxem/">GSoC'12 Embeddable lttoolbox</a> http://apertium.svn.sourceforge.net/viewvc/apertium/branches/gsoc2012/artetxem/</li>
<li>Share intent</li>
<li>Installation of language pair from local directory</li>
<li>Language list view</li>


Technical approach
========

For basic feature

<ol>
<li>Language pair
<ul>
<li>For language pair is I have used a zip-compress file which include Install.json in root. Install.xml contains information about installation and language pairs.
<br/>
Structure of <b>Install.json</b>
<pre>
{"pair": {
	"id":"eo-en",
	"version" : "2.0.13",
	"modes": {
		"modeitem" : [
			{"id" : "eo-en", "title" : "Esperanto-English"},
			{"id" : "en-eo", "title" : "English-Esperanto"}
		]
	}			
}}
</pre>
<a href="https://github.com/downloads/arinkverma/Apertiurm-Androind-app-devlopment/eo-en.zip" >Download dummy Language pair package (en-eo)</a> <br/>
[Note : It is en-eo language translation]

</li>
<li>Jar file can be also used to serve same purpose without extraction
</li><li>SVN to fetch content from remote server</li>
</ul>
</li>
<li>Share intent

Android share intent features inorder to connect with other installed application in device.
</li>
</ol>

Links
=====
<ol>
<li>http://apertium.org</li>
<li>http://wiki.apertium.org/wiki/User:Arinkverma</li>
</ol>
