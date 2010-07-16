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
import com.google.api.adwords.v201003.cm.Ad;
import com.google.api.adwords.v201003.cm.AdGroup;
import com.google.api.adwords.v201003.cm.AdGroupAd;
import com.google.api.adwords.v201003.cm.AdGroupAdPage;
import com.google.api.adwords.v201003.cm.AdGroupAdSelector;
import com.google.api.adwords.v201003.cm.AdGroupAdServiceInterface;
import com.google.api.adwords.v201003.cm.AdGroupAdStatus;
import com.google.api.adwords.v201003.cm.AdGroupCriterion;
import com.google.api.adwords.v201003.cm.AdGroupCriterionBids;
import com.google.api.adwords.v201003.cm.AdGroupCriterionIdFilter;
import com.google.api.adwords.v201003.cm.AdGroupCriterionPage;
import com.google.api.adwords.v201003.cm.AdGroupCriterionSelector;
import com.google.api.adwords.v201003.cm.AdGroupCriterionServiceInterface;
import com.google.api.adwords.v201003.cm.AdGroupPage;
import com.google.api.adwords.v201003.cm.AdGroupSelector;
import com.google.api.adwords.v201003.cm.AdGroupServiceInterface;
import com.google.api.adwords.v201003.cm.BiddableAdGroupCriterion;
import com.google.api.adwords.v201003.cm.Campaign;
import com.google.api.adwords.v201003.cm.CampaignPage;
import com.google.api.adwords.v201003.cm.CampaignSelector;
import com.google.api.adwords.v201003.cm.CampaignServiceInterface;
import com.google.api.adwords.v201003.cm.Criterion;
import com.google.api.adwords.v201003.cm.DateRange;
import com.google.api.adwords.v201003.cm.Stats;
import com.google.api.adwords.v201003.cm.StatsSelector;
import com.google.api.adwords.v201003.cm.TextAd;
import com.google.api.adwords.v201003.cm.UserStatus;

/**
 * This class is a prototype of what it would look like to dump the entities
 * within an account.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
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

      // Get the AdGroupCriterionService.
      AdGroupCriterionServiceInterface adGroupCriterionService =
          user.getService(AdWordsService.V201003.ADGROUP_CRITERION_SERVICE);
      
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
          // TODO(ffaber): determine why we need to check for null here.  Twice.
          if (adGroupAdPage == null || adGroupAdPage.getEntries() == null) {
            System.out.println("No ads found in camapign");
            continue;
          }

          // Display ads.
          for (AdGroupAd adGroupAd : adGroupAdPage.getEntries()) {
            Ad ad = adGroupAd.getAd();
            System.out.println(
                "Found ad with id  \"" + ad.getId() + "\""
                + "\n\t and type \"" + ad.getAdType()
                + "\n\t and display url \"" + ad.getDisplayUrl()
                + "\n\t and url url \"" + ad.getDisplayUrl()
                + "\n\t and cost \"" + adGroupAd.getStats().getCost()
                + "");
            if (TextAd.class.isInstance(ad)) {
              TextAd textAd = (TextAd) ad;
              System.out.println(
                  "Text ad has text: "
                  + "\n\t" + textAd.getHeadline()
                  + "\n\t" + textAd.getDescription1()
                  + "\n\t" + textAd.getDescription2()
                  + "");
            }
          }

          // Criterion logic below
          AdGroupCriterionSelector adGroupCriterionSelector
              = new AdGroupCriterionSelector();
          adGroupCriterionSelector.setUserStatuses(
              new UserStatus[] {UserStatus.ACTIVE});
          StatsSelector statsSelector = new StatsSelector();
          statsSelector.setDateRange(new DateRange("19700101", "20380101"));
          adGroupCriterionSelector.setStatsSelector(statsSelector);

          // Create id filter.
          AdGroupCriterionIdFilter idFilter = new AdGroupCriterionIdFilter();
          idFilter.setAdGroupId(adGroupId);
          adGroupCriterionSelector.setIdFilters(
              new AdGroupCriterionIdFilter[] {idFilter});

          // Get all active ad group criteria.
          AdGroupCriterionPage adGroupCriterionPage
              = adGroupCriterionService.get(adGroupCriterionSelector);

          // Get all criteria.
          if (adGroupCriterionPage == null
              || adGroupCriterionPage.getEntries() == null) {
            System.out.println("Nocriteria found in camapign");
            continue;
          }

          for (AdGroupCriterion adGroupCriterion
              : adGroupCriterionPage.getEntries()) {
            if (BiddableAdGroupCriterion.class.isInstance(adGroupCriterion)) {
              BiddableAdGroupCriterion biddableAdGroupCriterion =
                  (BiddableAdGroupCriterion) adGroupCriterion;

              Criterion criterion = biddableAdGroupCriterion.getCriterion();
              AdGroupCriterionBids adGroupCriterionBids =
                  biddableAdGroupCriterion.getBids();
              Stats stats = biddableAdGroupCriterion.getStats();

              System.out.println("Found ad group criterion: "
                  + "\n\t" + "\"criterion id \""
                  + "\n\t" + criterion.getId()
                  + "\n\t" + "\"type \""
                  + "\n\t" + criterion.getCriterionType()
                  + "\n\t" + "\"user status \""
                  + "\n\t" + biddableAdGroupCriterion.getUserStatus()
                  + "\n\t" + "\"dest url \""
                  + "\n\t" + biddableAdGroupCriterion.getDestinationUrl()
                  + "\n\t" + "\"first page cpc\""
                  + "\n\t" + biddableAdGroupCriterion.getFirstPageCpc()
                      .getAmount().getMicroAmount()
                  + "\n\t" + "\"system status\""
                  + "\n\t" + biddableAdGroupCriterion.getSystemServingStatus()
                  + "\n\t" + "\"bid type\""
                  + "\n\t" + adGroupCriterionBids.getAdGroupCriterionBidsType()
                  + "\n\t" + "\"stat start date\""
                  + "\n\t" + stats.getStartDate()
                  + "\n\t" + "\"stat end date\""
                  + "\n\t" + stats.getEndDate()
                  + "\n\t" + "\"stat type\""
                  + "\n\t" + stats.getStatsType()
                  + "\n\t" + "\"average cpc\""
                  + "\n\t" + stats.getAverageCpc().getMicroAmount()
                  + "\n\t" + "\"average position\""
                  + "\n\t" + stats.getAveragePosition()
                  + "\n\t" + "\"clicks\""
                  + "\n\t" + stats.getClicks()
                  + "\n\t" + "\"cost\""
                  + "\n\t" + stats.getCost().getMicroAmount()
                  + "\n\t" + "\"ctr\""
                  + "\n\t" + String.format("%.4f", stats.getCtr())
                  + "\n\t" + "\"impressions\""
                  + "\n\t" + stats.getImpressions()
                  + "\n\t" + "\"network\""
                  + "\n\t" + stats.getNetwork().getValue()
                  + "");
              biddableAdGroupCriterion.getStats();
            }
          }
         System.exit(0); 
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
