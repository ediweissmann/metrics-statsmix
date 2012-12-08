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

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StatsMixReporterTest extends FunSuite with ShouldMatchers with TestMetrics with BeforeAndAfterEach {

  // run the reporter
  new StatsMixReporter(TestStatsMixClient).run()
  TestStatsMixClient.print()

  val reportedMetrics = TestStatsMixClient.trackedMetrics

  test("gauges") {
    shouldContainMetric("GAUGE_NAME.value", 1)
  }

  test("gauges with non-number values") {
    reportedMetrics should not contain key(asMetricKey("NON_NUMBER_GAUGE_NAME.value"))
  }

  test("counters") {
    shouldContainMetric("COUNTER_NAME.count", 10)
  }

  test("meters") {
    shouldContainMetric("METER_NAME.meanRate", 3.0 plusOrMinus (0.5))
  }

  test("Histograms") {
    shouldContainMetric("HISTOGRAM_NAME.75thPercentile", 500)
    shouldContainMetric("HISTOGRAM_NAME.min", 100)
    shouldContainMetric("HISTOGRAM_NAME.max", 500)
    shouldContainMetric("HISTOGRAM_NAME.count", 2)
  }

  test("timer") {
    shouldContainMetric("TIMER_NAME.min", 200.0 plusOrMinus (2.0))
    shouldContainMetric("TIMER_NAME.max", 400.0 plusOrMinus(2.0))
    shouldContainMetric("TIMER_NAME.mean")
  }

  private def asMetricKey(name: String) = {
    classOf[StatsMixReporterTest].getSimpleName + "." + name
  }

  private def shouldContainMetric(shortKey: String) {
    reportedMetrics should contain key (asMetricKey(shortKey))
  }

  private def shouldContainMetric(shortKey: String, value: Number) {
    shouldContainMetric(shortKey)
    reportedMetrics(asMetricKey(shortKey)) should be(value.doubleValue())
  }

  private def shouldContainMetric(shortKey: String, tolerance: StatsMixReporterTest.this.type#DoubleTolerance) {
    shouldContainMetric(shortKey)
    reportedMetrics(asMetricKey(shortKey)) should be(tolerance)
  }

}
