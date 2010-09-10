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

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v13.AccountInfo;
import com.google.api.adwords.v13.AccountInterface;
import com.google.api.adwords.v13.ClientAccountInfo;
import com.google.api.adwords.v201003.cm.Campaign;
import com.google.api.adwords.v201003.cm.CampaignPage;
import com.google.api.adwords.v201003.cm.CampaignSelector;
import com.google.api.adwords.v201003.cm.CampaignServiceInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

/**
 * This is a servlet which initializes accounts within the adwords api
 * sandbox.
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

    AccountInterface accountService =
        user.getService(AdWordsService.V13.ACCOUNT_SERVICE);
    out.println("Obtained account service for user: " + user.getClientEmail());
    AccountInfo accountInfo = accountService.getAccountInfo();
    out.println(
        "Account with id " + accountInfo.getCustomerId()
        + " and name " + accountInfo.getDescriptiveName());

    CampaignServiceInterface campaignService =
        user.getService(AdWordsService.V201003.CAMPAIGN_SERVICE);

    CampaignSelector campaignSelector = new CampaignSelector();
    CampaignPage campaignPage = campaignService.get(campaignSelector);

    for (Campaign campaign : campaignPage.getEntries()) {
      out.println(
          "Campaign with name \"" + campaign.getName() + "\" "
          + "and id \"" + campaign.getId() + "\" was found.");
    }
  }

  /**
   * This method should be used with a sandbox login to initialize client
   * accounts.
   */
  private void dumpClientAccountInfos(PrintWriter out, AdWordsUser user)
      throws ServiceException, RemoteException {

    // This should output:
    // Account email address is: client_1+<login_id>
    // Account email address is: client_2+<login_id>
    // Account email address is: client_3+<login_id>
    // Account email address is: client_4+<login_id>
    // Account email address is: client_5+<login_id>

    AccountInterface accountService =
        user.getService(AdWordsService.V13.ACCOUNT_SERVICE);
    out.println("Obtained account service for user: " + user.getClientEmail());

    ClientAccountInfo[] clientAccountInfos =
        accountService.getClientAccountInfos();
    if (clientAccountInfos == null) {
      out.println("ClientAccountInfos are null");
    } else {
      out.println("Received account info for clients");
      for (ClientAccountInfo accountInfo : clientAccountInfos) {
        out.println("Account email address is: "
            + accountInfo.getEmailAddress());
      }
    }
  }
}
