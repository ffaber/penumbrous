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

package com.penumbrous.penumbrousadsapi.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a holding pattern servlet in the sense that it does nothing except
 * for stall for time while useful servlets are developed.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
public class HoldingPatternServlet extends HttpServlet {

  private static final Logger logger =
      Logger.getLogger(HoldingPatternServlet.class.getCanonicalName());

  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    logger.info("Received request for holding pattern servlet");
    response.setContentType("text/plain");
    response.getWriter().println("Holding while waiting for functionality");
    response.flushBuffer();
  }
}
