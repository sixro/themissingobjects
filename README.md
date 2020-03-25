the missing objects
=====================

![](https://github.com/sixro/themissingobjects/workflows/Java%20CI%20with%20Maven/badge.svg)


Summary
---------

  * [Introduction](#introduction)
  * [Criteria used](#criteria-used)
  * [Documentation](#documentation)
  * [Release](#release)


<a name="introduction" />Introduction
---------------------------------------

A small library containing Java objects I'm missing the most.  
I wrote these objects at least twice in all my life... sometimes more.

I remember the time Java 8 and the new "time" package was out when I realised that they forgot to add
the object [Interval](https://www.joda.org/joda-time/apidocs/org/joda/time/Interval.html) found for example in
JodaTime.

I am pretty sure I wrote an object to represent `Money` at least 10 times and I was really disappointed when I read
the reference implementation [Moneta](https://javamoney.github.io/api.html) of of JSR-354: so many objects for what? 

On the other hand, I don't mind if I had to write again and again those objects. I think it has been a good exercise...
a sort of [Object Calisthenics](https://pragprog.com/book/twa/thoughtworks-anthology).

I hope you'll find something interesting in this library that you can use in your projects.

<a name="criteria-used" />Criteria used
-----------------------------------

The main characteristic of the objects contained in this library is that they represent a specific unique concept 
in the real world. For other objects (e.g. `Money`), the representation used in this library can be used in a lot of 
[bounded context](https://martinfowler.com/bliki/BoundedContext.html).

Another criteria used to consider an object suitable for this library is that all objects provided by 
`themissingobjects` have to be written in pure java (no additional dependencies allowed).


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

