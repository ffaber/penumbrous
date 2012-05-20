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

import com.google.api.adwords.lib.AdWordsService.V201109_1;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201109_1.cm.Selector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a servlet which initializes accounts within the adwords api
 * sandbox.
 *
 * <p>Should be available at {@code http://localhost:8080/sandboxinit} once
 * it's running in dev appserver mode.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
public class SandboxInitServlet extends HttpServlet {

  private static final Logger logger =
      Logger.getLogger(SandboxInitServlet.class.getCanonicalName());

  // TODO(ffaber): make these properties
  private static final String LOGIN_EMAIL = "";
  private static final String PASSWORD = "";
  private static final String CLIENT_ID = "";
  private static final String DEV_TOKEN = "";

  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    try {
      innerService(response);
    } catch (Throwable e) {
      response.getWriter().println("Exception was encountered\n");
      e.printStackTrace(response.getWriter());
      while (e.getCause() != null) {
        response.getWriter().println();
        e.getCause().printStackTrace(response.getWriter());
        e = e.getCause();
      }
    }
    response.flushBuffer();
  }

  private void innerService(HttpServletResponse response) throws Exception {
    PrintWriter out = response.getWriter();

    AdWordsUser.useGaeHttpApi(true);
    AdWordsUser.useClasspathClientConfig(true);
    AdWordsUser user = new AdWordsUser(
        LOGIN_EMAIL,
        PASSWORD,
        CLIENT_ID,
        "Google",
        DEV_TOKEN,
        false);

    dumpCampaignsInV201109_1(user, out);
  }

  private void dumpCampaignsInV201109_1(AdWordsUser user, PrintWriter out)
      throws Exception {

    com.google.api.adwords.v201109_1.cm.CampaignServiceInterface campaignService =
        user.getService(V201109_1.CAMPAIGN_SERVICE);
    com.google.api.adwords.v201109_1.cm.Selector campaignSelector =
        new Selector();
    campaignSelector.setFields(new String[] {"Name", "Id"});
    com.google.api.adwords.v201109_1.cm.CampaignPage campaignPage =
        campaignService.get(campaignSelector);

    for (com.google.api.adwords.v201109_1.cm.Campaign campaign :
        campaignPage.getEntries()) {
      out.println(
          "Campaign with name \"" + campaign.getName() + "\" "
          + "and id \"" + campaign.getId() + "\" was found.");
    }
  }
}
