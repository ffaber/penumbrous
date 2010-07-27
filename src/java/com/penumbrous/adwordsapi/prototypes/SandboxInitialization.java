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

package com.penumbrous.adwordsapi.prototypes;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v13.AccountInterface;
import com.google.api.adwords.v13.ClientAccountInfo;

/**
 * This class includes logic to initialize a quintuplet of sandbox accounts.
 * 
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
class SandboxInitialization {

  private final static String PROPERTY_FILE_LOCATION =
      System.getProperty("user.home") + "/adwords.sandbox.properties";

  public static void main(String[] args) throws Exception {
    AdWordsServiceLogger.log();

    AdWordsUser user = new AdWordsUser(PROPERTY_FILE_LOCATION);

    AccountInterface accountService =
        user.getService(AdWordsService.V13.ACCOUNT_SERVICE);

    // This should output:
    // Account email address is: client_1+<login_id>
    // Account email address is: client_2+<login_id>
    // Account email address is: client_3+<login_id>
    // Account email address is: client_4+<login_id>
    // Account email address is: client_5+<login_id>
    for (ClientAccountInfo accountInfo
        : accountService.getClientAccountInfos()) {
      System.out.println(
          "Account email address is: " + accountInfo.getEmailAddress());
    }
  }
}
