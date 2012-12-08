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

import collection.mutable

object TestStatsMixClient extends StatsMixClient {
  private val tracked = mutable.Map[String, Double]()
  private var count = 0

  def callsCount() = count
  def trackedMetrics() = tracked

  override def track(name: String, value: Number) = {
    tracked.put(name, value.doubleValue())
    count += 1
    Left()
  }

  def print() {
    println (tracked mkString "\n")
  }
}
