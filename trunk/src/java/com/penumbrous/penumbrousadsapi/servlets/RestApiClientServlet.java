/**
 * Copyright (C) 2010 Google Inc.
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

package com.penumbrous.penumbrousadsapi.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet demonstrates use of a rest api client.
 * 
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
public class RestApiClientServlet extends HttpServlet {

  // TODO(ffaber): make these properties
  private static final String USERNAME = "";
  private static final String PASSWORD = "";

  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");

    try {
      String result = getWebResource();
      response.getWriter().println("Response was:\n" + result);
    } catch (Exception e) {
      response.getWriter().println("Encountered exception");
      e.printStackTrace(response.getWriter());
    }

    response.flushBuffer();
  }

  private String getWebResource() {
    Client client = new Client();
    WebResource webResource = client
        .resource("http://api.foursquare.com/v1/user");
    client.addFilter(new HTTPBasicAuthFilter(USERNAME, PASSWORD));
    return webResource.get(String.class);
  }
}
