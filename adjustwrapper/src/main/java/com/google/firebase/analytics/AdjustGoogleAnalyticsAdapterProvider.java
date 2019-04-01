// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.analytics;

import android.support.annotation.GuardedBy;

/**
 * Initializes and provides {@link GoogleAnalyticsAdapter} singleton for {@link
 * AdjustInstanceWrapper}.
 */
class AdjustGoogleAnalyticsAdapterProvider {
  static final String LOG_TAG = "FA-W";

  private static final String ADJUST_PREFIX = "aj_";

  private static final String EMPTY_EVENT_NAME = "aj_unnamed_event";
  private static final String EMPTY_PARAM_NAME = "aj_unnamed_parameter";
  private static final String EMPTY_USER_PROPERTY_NAME = "aj_unnamed_user_property";

  private static final String WRAPPER_PARAM_VALUE = "aj";

  @GuardedBy("AdjustGoogleAnalyticsAdapterProvider.class")
  private static GoogleAnalyticsAdapter adapter;

  static GoogleAnalyticsAdapter getGoogleAnalyticsAdapter() {
    synchronized (AdjustGoogleAnalyticsAdapterProvider.class) {
      if (adapter == null) {
        adapter =
            new GoogleAnalyticsAdapter.Builder()
                .setWrappedSdkName(WRAPPER_PARAM_VALUE)
                .setSanitizedNamePrefix(ADJUST_PREFIX)
                .setEmptyEventName(EMPTY_EVENT_NAME)
                .setEmptyParamName(EMPTY_PARAM_NAME)
                .setEmptyUserPropertyName(EMPTY_USER_PROPERTY_NAME)
                .build();
      }
    }
    return adapter;
  }
}
