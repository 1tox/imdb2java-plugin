Maven plugin for handling features provided by <a href="https://github.com/1tox/imdb2java-core">imdb2java-core</a> API

Usage:
- First, get Maven <a href="http://maven.apache.org/download.cgi">here</a>
- Then, go to your project location path and type : <br>
<code>mvn install movies:download</code><br>
This will install the artifact in your local repository and start downloading files required to populate your database. Take care, it should take a while...<br>
If you are under a proxy, add the following parameters to your command line: <code>-DproxyHost=&lt;proxyHost&gt; -DproxyPort=&lt;proxyPort&gt;</code>
