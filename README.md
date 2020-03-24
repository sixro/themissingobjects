the missing objects
=====================

Summary
---------

  * [Introduction](#introduction)
  * [Documentation](#documentation)
  * [Release](#release)


<a name="introduction" />Introduction
---------------------------------------

A small library containing Java objects I'm missing the most.


<a name="documentation" />Documentation
-----------------------------------------

You can read the Javadoc of:

  * [0.0.7](https://oss.sonatype.org/service/local/repositories/releases/archive/com/github/sixro/themissingobjects/0.0.7/themissingobjects-0.0.7-javadoc.jar/!/index.html): stable version (alpha)
  * [0.0.8-SNAPSHOT](https://oss.sonatype.org/service/local/repositories/snapshots/archive/com/github/sixro/themissingobjects/0.0.8-SNAPSHOT/themissingobjects-0.0.8-20200324.212928-1-javadoc.jar/!/index.html): unstable version


<a name="release" />Release
-----------------------------

In order to create a new release, you need a Sonatype account with an access to the repository.  
Follow the [OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html) provided by Sonatype in order to configure your environment.  

When your environment is configured, you can:

  * Release an unstable version using the command `mvn deploy -Prelease`
  * Release a stable version using the command `mvn -B release:prepare release:perform`

