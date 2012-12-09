metrics-statsmix [![Build Status](https://secure.travis-ci.org/ediweissmann/metrics-statsmix.png?branch=master)](http://travis-ci.org/ediweissmann/metrics-statsmix)
================

[Codahale metrics](https://github.com/codahale/metrics) reporter that sends metrics to [statsmix.com](http://statsmix.com)

Usage
----------------

Can be scheduled to report every hour.

    StatsMixReporter.enable(1, TimeUnit.HOURS, "your api key here")

Build and install
-----------------
Maven dependency is not published to Maven Central yet. If you wish to have it there, just raise an issue.
To generate the jar file, `git clone` the repository and run `mvn install`.

    git clone git://github.com/ediweissmann/metrics-statsmix.git
    cd metrics-statsmix && mvn clean install
