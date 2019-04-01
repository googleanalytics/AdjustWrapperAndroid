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

import android.content.Context;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.util.Log;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.AdjustInstance;
import java.util.Collections;
import java.util.Map;

/** Wraps {@link AdjustInstance} */
public class AdjustInstanceWrapper {

  @GuardedBy("this")
  private Map<String, String> tokenMap = null;

  @GuardedBy("this")
  private boolean contextProvided = false;

  /**
   * Initializes Adjust Wrapper by providing a {@link Context} and an event token to event name
   * mapping.
   *
   * @param context the context required to track custom events through Google Analytics for
   *     Firebase
   * @param tokenMap map to translate Adjust tokens into the untokenized name for Google Analytics
   *     for Firebase
   */
  public void initialize(@NonNull Context context, @NonNull Map<String, String> tokenMap) {
    synchronized (this) {
      if (context != null) {
        getGoogleAnalytics().registerContext(context);
        contextProvided = true;
      } else if (!contextProvided) {
        Log.w(
            AdjustGoogleAnalyticsAdapterProvider.LOG_TAG,
            "A valid context must be provided before the AdjustWrapper can track custom events through Google Analytics for Firebase.");
      }

      this.tokenMap = tokenMap;
      if (tokenMap == null) {
        Log.w(
            AdjustGoogleAnalyticsAdapterProvider.LOG_TAG,
            "A valid token map must be provided before the AdjustWrapper can track custom events through Google Analytics for Firebase.");
      }
    }
  }

  /** Wraps calls to {@link AdjustInstance#trackEvent(AdjustEvent)} */
  public void trackEvent(final WrappedAdjustEvent event) {
    synchronized (this) {
      if (!contextProvided) {
        Log.w(
            AdjustGoogleAnalyticsAdapterProvider.LOG_TAG,
            "AdjustWrapper.trackEvent() called before a valid context has been provided using AdjustWrapper.initialize().");
      }
      if (tokenMap == null) {
        Log.w(
            AdjustGoogleAnalyticsAdapterProvider.LOG_TAG,
            "AdjustWrapper.trackEvent() called before a token to event name map has been set using AdjustWrapper.initialize().");
      }

      if (event != null) {
        getGoogleAnalytics()
            .logEvent(
                event.getEventName(
                    tokenMap != null ? tokenMap : Collections.<String, String>emptyMap()),
                event.getGoogleAnalyticsParams());
      }
    }
    getAdjust().trackEvent(event);
  }

  /**
   * Wraps calls to {@link AdjustGoogleAnalyticsAdapterProvider#getGoogleAnalyticsAdapter()} to
   * allow mocking in tests.
   *
   * @hide
   */
  GoogleAnalyticsAdapter getGoogleAnalytics() {
    return AdjustGoogleAnalyticsAdapterProvider.getGoogleAnalyticsAdapter();
  }

  /**
   * Wraps calls to {@link Adjust#getDefaultInstance()} ()} to allow mocking in tests.
   *
   * @hide
   */
  AdjustInstance getAdjust() {
    return Adjust.getDefaultInstance();
  }
}
