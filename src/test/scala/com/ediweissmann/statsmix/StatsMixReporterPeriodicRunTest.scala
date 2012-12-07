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

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import java.util.concurrent.TimeUnit
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StatsMixReporterPeriodicRunTest extends FunSuite with ShouldMatchers with TestMetrics {

  val reporter = new StatsMixReporter(TestStatsMixClient)

  test("run every second for 3 seconds") {
    reporter.start(1, TimeUnit.SECONDS)
    Thread.sleep(3500)
    reporter.shutdown()

    TestStatsMixClient.callsCount() should be(5 /*metrics*/ * 3 /*seconds*/)
  }
}
