// Copyright 2010 Google Inc.  All Rights Reserved 

package com.penumbrous.adwordsapi.prototypes;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201003.cm.DownloadFormat;
import com.google.api.adwords.v201003.cm.Operator;
import com.google.api.adwords.v201003.cm.ReportDefinition;
import com.google.api.adwords.v201003.cm.ReportDefinitionDateRangeType;
import com.google.api.adwords.v201003.cm.ReportDefinitionOperation;
import com.google.api.adwords.v201003.cm.ReportDefinitionReportType;
import com.google.api.adwords.v201003.cm.ReportDefinitionServiceInterface;
import com.google.api.adwords.v201003.cm.Selector;

/**
 * This class demonstrates how to dump an account using the
 * {@link ReportDefinitionServiceInterface}.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
class DumpAccountWithReportDefinitionService {

  public static void main(String[] args) throws Exception {
    // Log SOAP XML request and response.
    AdWordsServiceLogger.log();

    // Get AdWordsUser from "~/adwords.properties".
    AdWordsUser user = new AdWordsUser();

    ReportDefinitionServiceInterface reportDefinitionService =
        user.getService(AdWordsService.V201003.REPORT_DEFINITION_SERVICE);

    Selector selector = new Selector();
    selector.setFields(new String[]{ "AdGroupId", "Id", "KeywordText",
        "KeywordMatchType", "Impressions", "Clicks", "Cost" });

    // Create report definition.
    ReportDefinition reportDefinition = new ReportDefinition();
    reportDefinition.setReportName(
        "Keywords performance report #" + System.currentTimeMillis());
    reportDefinition.setDateRangeType(
        ReportDefinitionDateRangeType.ALL_TIME);
    reportDefinition.setReportType(
        ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT);
    reportDefinition.setDownloadFormat(DownloadFormat.XML);
    reportDefinition.setSelector(selector);

    // Create operations.
    ReportDefinitionOperation operation = new ReportDefinitionOperation();
    operation.setOperand(reportDefinition);
    operation.setOperator(Operator.ADD);
    ReportDefinitionOperation[] operations =
        new ReportDefinitionOperation[]{ operation };

    ReportDefinition[] result = reportDefinitionService.mutate(operations);

    if (result != null) {
      for (ReportDefinition reportDefinitionResult : result) {
        System.out.println(
            "Report definition with name \""
            + reportDefinitionResult.getReportName()
            + "\" and id \""
            + reportDefinitionResult.getId() + "\" was added.");
      }
    } else {
      System.out.println("No report definitions were added.");
    }
  }
}
