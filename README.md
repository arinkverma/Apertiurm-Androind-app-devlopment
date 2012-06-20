Apertium on your mobile
=========
Port Apertium to Android and possibly iPhone. Apertium has a Java port which doesn't currently work on mobile telephones. Lots of people have mobile telephones, and some of them would like to have a translator there.


Features [version 2.3.5]
=========
<b>Download Activity</b>
<br/>
![Alt download](http://1.bp.blogspot.com/-w-rQ9v5mQfo/T-JBRusABDI/AAAAAAAAAug/QPbf9qApmh0/s1600/device-2012-06-21-023024.png "Download Activity")
<ul>
<li>User can download language pair from SVN over internet.</li>
<li>Progress bar showing status of package being downloaded.</li>
<li>At present only shipped with temp SVN</li>
</ul>

<b>Desktop Widget</b>
<br/>
![Alt widget](http://1.bp.blogspot.com/-4fbKyCbz0j8/T9PI4Dzta4I/AAAAAAAAAtc/RPZEOZYm_LA/s1600/device-2012-06-10-031043.png "Desktop Widget")
<ul>
<li>User can place at maximum of five translation mode shortcut for quick translation.</li>
<li>Easy to config widget from setting panel of application</li>
<li>At present only support single instance.</li>
<li><b>Future work</b>, to create multiple instance of widget</li>
</ul>


<b>Caching support</b>
<br/>
![Alt cache](http://4.bp.blogspot.com/-lAzs4khWnq4/T-JTtoOFUiI/AAAAAAAAAu4/zHPi6oJzmMY/s1600/device-2012-06-21-024705.png "Cache in setting")
	<ul>
<li>The cache feature developed by <a href="http://wiki.apertium.org/wiki/User:Mikel/GSoC_2012_Application">Mikel</a> on lttoolbox, has been added to application</li>
<li>This make subsequent translation instantaneously</li>
<li>Can easliy be enable/disable from setting panel</li>
</ul>

<b>New setting panel</b><br />
In earlier version there was absence of standard setting panel

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
<a href="https://github.com/arinkverma/Apertiurm-Androind-app-devlopment/blob/master/language_pair/android-eo-en.zip?raw=true" >Download dummy Language pair package (en-eo)</a> <br/>
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
<li>http://www.arinkverma.in/2012/06/apertium-android-23.html</li>
</ol>
