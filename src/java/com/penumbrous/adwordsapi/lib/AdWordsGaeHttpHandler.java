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

package com.penumbrous.adwordsapi.lib;

import com.google.api.adwords.lib.ServiceAccountantManager;

import com.penumbrous.axis.handlers.GaeHttpHandler;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;

/**
 * This handler simply delegates to an instance of {@link GaeHttpHandler}
 * to do the real "handling."  It exists to decorate this handling with
 * additional logic that executes the recording logic within
 * {@link ServiceAccountantManager}.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
public class AdWordsGaeHttpHandler extends BasicHandler {

  private final GaeHttpHandler gaeHttpHandler;

  /** This is created via reflection through axis. */
  @SuppressWarnings("UnusedDeclaration")
  public AdWordsGaeHttpHandler() {
    this(new GaeHttpHandler());
  }

  AdWordsGaeHttpHandler(GaeHttpHandler gaeHttpHandler) {
    this.gaeHttpHandler = gaeHttpHandler;
  }

  @Override
  public void invoke(MessageContext msgContext) throws AxisFault {
    gaeHttpHandler.invoke(msgContext);
    ServiceAccountantManager.getInstance().recordMessage(msgContext);
  }
}
