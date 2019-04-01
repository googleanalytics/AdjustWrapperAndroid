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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.adjust.sdk.AdjustEvent;
import java.util.HashMap;
import java.util.Map;

/** Wraps {@link AdjustEvent} */
public class WrappedAdjustEvent extends AdjustEvent {
  private final String gaEventToken;

  @GuardedBy("this")
  private final Map<String, Object> params;

  /** Wraps calls to {@link AdjustEvent#AdjustEvent(String)} */
  public WrappedAdjustEvent(String eventToken) {
    super(eventToken);
    this.gaEventToken = eventToken;
    this.params = new HashMap<>();
  }

  /** Wraps calls to {@link AdjustEvent#setRevenue(double, String)} */
  @Override
  public void setRevenue(double revenue, String currency) {
    super.setRevenue(revenue, currency);
    synchronized (this) {
      if (currency != null) {
        params.put(FirebaseAnalytics.Param.CURRENCY, currency);
      } else {
        params.remove(FirebaseAnalytics.Param.CURRENCY);
      }
      params.put(FirebaseAnalytics.Param.VALUE, revenue);
    }
  }

  /** Wraps calls to {@link AdjustEvent#setOrderId(String)} */
  @Override
  public void setOrderId(String orderId) {
    super.setOrderId(orderId);
    synchronized (this) {
      if (orderId != null) {
        params.put(FirebaseAnalytics.Param.TRANSACTION_ID, orderId);
      } else {
        params.remove(FirebaseAnalytics.Param.TRANSACTION_ID);
      }
    }
  }

  /**
   * Returns the event name to be logged with the Google Analytics SDK.
   *
   * @param tokenMap mapping between Adjust event tokens and the event names
   * @return event name (if in map), otherwise raw event token
   */
  @Nullable
  protected String getEventName(@NonNull Map<String, String> tokenMap) {
    if (tokenMap.containsKey(gaEventToken)) {
      return tokenMap.get(gaEventToken);
    }
    return gaEventToken;
  }

  /** Returns a map containing event parameters to be logged with the Google Analytics SDK. */
  @NonNull
  protected Map<String, Object> getGoogleAnalyticsParams() {
    synchronized (this) {
      return new HashMap<>(params);
    }
  }
}
