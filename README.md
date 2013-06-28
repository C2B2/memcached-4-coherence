memcached-4-coherence
=====================

A Memcached Interface to Oracle Coherence released under the LGPL.

This code only supports the binary interface to memcached.

To use Oracle Coherence as a backing store for memcached add the following two lines to your Java code and build and add the jar to your classpath.

        MemcachedServer server = new MemcachedServer(port, namedCache);
        server.bootStrap();
        
This will create a memcached server listening on the specified port which will store the binary data into the specified
Oracle Coherence Named Cache.

BUILDING INSTRUCTIONS:
* Download Oracle Coherence 3.7.1 from <a href="http://www.oracle.com/technetwork/middleware/coherence/downloads/index.html">http://www.oracle.com/technetwork/middleware/coherence/downloads/index.html</a>
* Unzip the Coherence archive and install coherence.jar to your local Maven repository by running:
<pre>
mvn install:install-file  \
      -DgroupId=com.oracle.coherence  \
      -DartifactId=coherence  \
      -Dversion=3.7.1  \
      -Dfile=coherence.jar  \
      -Dpackaging=jar \
      -DgeneratePom=true
</pre>
* Build the memcached-4-coherence by mvn install
* Enjoy!

This code has been tested using the memccapable utility and passes all the tests for binary protocol compliance.

This code is brought to you by <a href="http://www.c2b2.co.uk/oracle_coherence">C2B2 The Leading Independent middleware Experts</a>




