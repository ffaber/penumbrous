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
import com.google.api.adwords.v201003.cm.DownloadFormat;
import com.google.api.adwords.v201003.cm.Operator;
import com.google.api.adwords.v201003.cm.ReportDefinition;
import com.google.api.adwords.v201003.cm.ReportDefinitionDateRangeType;
import com.google.api.adwords.v201003.cm.ReportDefinitionField;
import com.google.api.adwords.v201003.cm.ReportDefinitionOperation;
import com.google.api.adwords.v201003.cm.ReportDefinitionReportType;
import com.google.api.adwords.v201003.cm.ReportDefinitionServiceInterface;
import com.google.api.adwords.v201003.cm.Selector;

import java.util.Arrays;

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

    // Get report fields.
    ReportDefinitionField[] reportDefinitionFields =
        reportDefinitionService.getReportFields(
            ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT);
    // Display report fields.
    System.out.println("Available fields for report:");
    for (ReportDefinitionField reportDefinitionField : reportDefinitionFields) {
      String enumValues = reportDefinitionField.getEnumValues() == null ?
          "" : Arrays.asList(reportDefinitionField.getEnumValues()).toString();
      System.out.print("\t"
          + reportDefinitionField.getFieldName()
          + "EnumValues: " + enumValues
          + " (" + reportDefinitionField.getFieldType() + ")");
    }

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
