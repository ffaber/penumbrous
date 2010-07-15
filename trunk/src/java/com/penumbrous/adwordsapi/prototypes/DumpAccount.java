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
import com.google.api.adwords.v201003.cm.AdGroup;
import com.google.api.adwords.v201003.cm.AdGroupAd;
import com.google.api.adwords.v201003.cm.AdGroupAdPage;
import com.google.api.adwords.v201003.cm.AdGroupAdSelector;
import com.google.api.adwords.v201003.cm.AdGroupAdServiceInterface;
import com.google.api.adwords.v201003.cm.AdGroupAdStatus;
import com.google.api.adwords.v201003.cm.AdGroupPage;
import com.google.api.adwords.v201003.cm.AdGroupSelector;
import com.google.api.adwords.v201003.cm.AdGroupServiceInterface;
import com.google.api.adwords.v201003.cm.Campaign;
import com.google.api.adwords.v201003.cm.CampaignPage;
import com.google.api.adwords.v201003.cm.CampaignSelector;
import com.google.api.adwords.v201003.cm.CampaignServiceInterface;

/**
 * This class is a prototype of what it would look like to dump the entities
 * within an account.
 *
 * @author ffaber@google.com (Fred Faber)
 */
class DumpAccount {

  public static void main(String[] args) {
    try {
      // Log SOAP XML request and response.
      AdWordsServiceLogger.log();

      // Get AdWordsUser from "~/adwords.properties".
      AdWordsUser user = new AdWordsUser();

      // Get the CampaignService.
      CampaignServiceInterface campaignService =
          user.getService(AdWordsService.V201003.CAMPAIGN_SERVICE);

      // Get the AdGroupService.
      AdGroupServiceInterface adGroupService =
          user.getService(AdWordsService.V201003.ADGROUP_SERVICE);

      // Get the AdGroupAdService.
      AdGroupAdServiceInterface adGroupAdService =
          user.getService(AdWordsService.V201003.ADGROUP_AD_SERVICE);

      // Create selector.
      CampaignSelector campaignSelector = new CampaignSelector();

      // Get all campaigns.
      CampaignPage campaignPage = campaignService.get(campaignSelector);

      for (Campaign campaign : campaignPage.getEntries()) {
        System.out.println(
            "Campaign with name \"" + campaign.getName() + "\" "
            + "and id \"" + campaign.getId() + "\" was found.");

        long campaignId = campaign.getId();

        // Create selector.
        AdGroupSelector adGroupSelector = new AdGroupSelector();
        adGroupSelector.setCampaignIds(new long[] {campaignId});

        // Get all ad groups.
        AdGroupPage adGroupPage = adGroupService.get(adGroupSelector);

        for (AdGroup adGroup : adGroupPage.getEntries()) {
          long adGroupId = adGroup.getId();

          // Create selector.
          AdGroupAdSelector adGroupAdSelector = new AdGroupAdSelector();
          adGroupAdSelector.setAdGroupIds(new long[]{ adGroupId });
          // By default disabled ads aren't returned by the selector. To return
          // them include the DISABLED status in the statuses field.
          adGroupAdSelector.setStatuses(new AdGroupAdStatus[]{
              AdGroupAdStatus.ENABLED,
              AdGroupAdStatus.PAUSED,
              AdGroupAdStatus.DISABLED });

          // Get all ads.
          AdGroupAdPage adGroupAdPage = adGroupAdService.get(adGroupAdSelector);
          if (adGroupAdPage == null) {
            System.out.println("No ads found in camapign");
            continue;
          }

          // Display ads.
          for (AdGroupAd adGroupAd : adGroupAdPage.getEntries()) {
            System.out.println(
                "Found ad with id  \"" + adGroupAd.getAd().getId() + "\""
                + "\n\t and type \"" + adGroupAd.getAd().getAdType()
                + "\n\t and display url \"" + adGroupAd.getAd().getDisplayUrl()
                + "\n\t and url url \"" + adGroupAd.getAd().getDisplayUrl()
                + "\n\t and cost \"" + adGroupAd.getStats().getCost()
                + "");
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
