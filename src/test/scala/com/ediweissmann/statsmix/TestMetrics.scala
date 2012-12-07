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

import java.util.concurrent.TimeUnit
import com.yammer.metrics.scala.Instrumented

trait TestMetrics extends Instrumented {

  // gauge
  metrics.gauge("GAUGE_NAME") {
    1
  }
  metrics.gauge("NON_NUMBER_GAUGE_NAME") {
    "non number value"
  }

  // counter
  def counter = metrics.counter("COUNTER_NAME")

  counter += 10

  // meter
  def meter = metrics.meter(name = "METER_NAME", eventType = "METER_TYPE", unit = TimeUnit.SECONDS)

  meter.mark()
  meter.mark()

  // histogram
  def histogram = metrics.histogram("HISTOGRAM_NAME")

  histogram += 100

  // timer
  def timer = metrics.timer("TIMER_NAME")

  timer.time {
    Thread.sleep(42)
  }
}
