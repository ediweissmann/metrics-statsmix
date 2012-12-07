metrics-statsmix
================

[Codahale metrics](https://github.com/codahale/metrics) reporter that sends metrics to [statsmix.com](http://statsmix.com)

Can be scheduled to report every hour. Simple to use.

    StatsMixReporter.enable(1, TimeUnit.HOURS, "your api key here")

Use it in maven

    # No published version to maven central yet.
    # For now, checkout and run 
    mvn install
