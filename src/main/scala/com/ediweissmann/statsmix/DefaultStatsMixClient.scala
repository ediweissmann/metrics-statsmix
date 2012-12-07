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

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URLEncodedUtils
import scala.collection.JavaConverters._
import org.apache.http.HttpResponse

/**
 * statsmix.com api client. Track stats by provided name and value
 */
trait StatsMixClient {
  def track(name: String, value: Number): Either[Unit, Exception]
}

/**
 * Default implementation of a statsmix.com api client
 * @param apiKey your api key
 * @param httpClient optionally, provide your own baked http client
 */
class DefaultStatsMixClient(val apiKey: String,
                            val httpClient: HttpClient = new DefaultHttpClient()) extends StatsMixClient {

  val base = "http://www.statsmix.com/api/v2/track?"

  override def track(name: String, value: Number): Either[Unit, Exception] = {

    def createRequest() = {
      val params = List(
        new BasicNameValuePair("api_key", apiKey),
        new BasicNameValuePair("name", name),
        new BasicNameValuePair("value", value.toString)
      )
      new HttpGet(base + URLEncodedUtils.format(params.asJava, "utf-8"))
    }

    def notSuccessful(response: HttpResponse) = {
      response.getStatusLine.getStatusCode >= 400
    }

    try {
      val response = httpClient.execute(createRequest())
      if (notSuccessful(response)) {
        Right(new RuntimeException("API call failed. Response was: " + response.getStatusLine.toString))
      } else {
        Left()
      }
    } catch {
      case ex: Exception => Right(ex)
    }
  }
}
