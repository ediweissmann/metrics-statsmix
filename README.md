metrics-statsmix [![Build Status](https://secure.travis-ci.org/ediweissmann/metrics-statsmix.png?branch=master)](http://travis-ci.org/ediweissmann/metrics-statsmix)
================

[Codahale metrics](https://github.com/codahale/metrics) reporter that sends metrics to [statsmix.com](http://statsmix.com)

Usage
----------------

Can be scheduled to report every hour.

    StatsMixReporter.enable(1, TimeUnit.HOURS, "your api key here")

Build and install
-----------------
Maven release version dependency is not published to Maven Central yet. If you wish to have it there, just raise an issue.
To use the latest snapshot version, use the following repository and dependency:

```
<repository>
	<id>oss-sonatype-snapshots</id>
    <name>OSS Sonatype snapshots</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
    	<enabled>true</enabled>
    </snapshots>
</repository>	
```
    
```
<dependency>
	<groupId>com.ediweissmann.metrics</groupId>
    <artifactId>metrics-statsmix</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
