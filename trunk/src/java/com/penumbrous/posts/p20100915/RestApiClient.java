/**
 * Copyright (C) 2010 Fred Faber.
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
 *
 */

package com.penumbrous.posts.p20100915;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * This class demonstrates some functionality of a
 * <a href="https://jersey.dev.java.net/">Jersey</a> REST client.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
class RestApiClient {

  private static final String USERNAME = "";
  private static final String PASSWORD = "";

  public static void main(String[] args) throws Exception {
    Client client = new Client();
    WebResource webResource = client
        .resource("http://api.foursquare.com/v1/user");
    client.addFilter(new HTTPBasicAuthFilter(USERNAME, PASSWORD));
    String result = webResource.get(String.class);
    System.err.println("result is: " + result);
  }
}
