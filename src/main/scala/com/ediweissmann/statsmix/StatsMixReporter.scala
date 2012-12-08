/*
 * Copyright 2012 by Eduard Weissmann (edi.weissmann@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ediweissmann.statsmix

import java.math.BigDecimal
import com.yammer.metrics.reporting.AbstractPollingReporter
import scala.collection.JavaConversions._
import com.yammer.metrics.Metrics
import com.yammer.metrics.core._
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

/**
 * Reports metrics to statsmix.com
 * @param client statsmix.com api client
 */
class StatsMixReporter(client: StatsMixClient) extends AbstractPollingReporter(Metrics.defaultRegistry(), "statsMixReporter") with MetricProcessor[String] {

  val logger = LoggerFactory.getLogger(classOf[StatsMixReporter])

  override def run() {
    logger.debug("Reporting metrics to statsmix...")
    for ((key, value) <- getMetricsRegistry.groupedMetrics()) {
      for ((metricName, metric) <- value) {
        metric.processWith(this, metricName, shorten(key))
      }
    }
  }

  private def shorten(key: String) = {
    key.split("\\.").reverse.head
  }

  override def processMeter(metricName: MetricName, meter: Metered, context: String) {
    track(meter.meanRate(), "meanRate", metricName, context)
  }

  override def processCounter(metricName: MetricName, counter: Counter, context: String) {
    track(counter.count(), "count", metricName, context)
  }

  override def processHistogram(metricName: MetricName, histogram: Histogram, context: String) {
    track(histogram.count(), "count", metricName, context)
    track(histogram.min(), "min", metricName, context)
    track(histogram.max(), "max", metricName, context)
    track(histogram.getSnapshot.get75thPercentile(), "75thPercentile", metricName, context)
  }

  override def processTimer(metricName: MetricName, timer: Timer, context: String) {
    track(timer.min(), "min", metricName, context)
    track(timer.max(), "max", metricName, context)
    track(timer.mean(), "mean", metricName, context)
  }

  override def processGauge(metricName: MetricName, gauge: Gauge[_], context: String) {
    track(toNumber(gauge.value()), "value", metricName, context)
  }

  private def toNumber(input: Any): Number = {
    try {
      new BigDecimal(input.toString)
    } catch {
      case _ => 0
    }
  }

  /**
   * @param value 0.2 or 42
   * @param name "count" or "meanRate"
   * @param metricName "requests-per-second" or "failed-attempts"
   * @param key "FooBarController" or "MyService"
   */
  private def track(value: Number, name: String, metricName: MetricName, key: String) {
    val fullName = "%s.%s.%s".format(key, metricName.getName, name)
    val shouldTrack = value.doubleValue() != 0.0

    logger.trace("track(%s, %s, %s)".format(value, metricName, name))

    if (shouldTrack) {
      client.track(fullName, value).right.map {
        ex =>
          logger.error("Failed to track metrics", ex)
      }
    }
  }
}

object StatsMixReporter {
  def enable(period: Long, unit: TimeUnit, apiKey: String) = {
    new StatsMixReporter(
      new DefaultStatsMixClient(apiKey)
    ).start(period, unit)
  }
}


