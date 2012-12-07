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

  // run it
  new StatsMixReporter(TestStatsMixClient).run()

  val reportedMetrics = TestStatsMixClient.trackedMetrics
  TestStatsMixClient.print()

  test("gauges") {
    reportedMetrics should contain key (asMetricKey("GAUGE_NAME.value"))
    reportedMetrics should contain value ("1")
  }

  test("gauges with non-number values") {
    reportedMetrics should not contain key(asMetricKey("NON_NUMBER_GAUGE_NAME"))
  }

  test("counters") {
    reportedMetrics should contain key (asMetricKey("COUNTER_NAME.count"))
    reportedMetrics should contain value ("10")
  }

  test("meters") {
    reportedMetrics should contain key (asMetricKey("METER_NAME.meanRate"))
    // how can I expect a fixed value?
  }

  test("Histograms") {
    reportedMetrics should contain key (asMetricKey("HISTOGRAM_NAME.95thPercentile"))
    reportedMetrics should contain value ("100.0")
  }

  test("timer") {
    reportedMetrics should contain key (asMetricKey("TIMER_NAME.mean"))
  }

  private def asMetricKey(name: String) = classOf[StatsMixReporterTest].getSimpleName + "." + name
}
