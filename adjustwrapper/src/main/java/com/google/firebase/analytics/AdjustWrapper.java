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
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import java.util.Map;

/** Wraps {@link Adjust} */
public class AdjustWrapper {

  @GuardedBy("AdjustWrapper.class")
  private static volatile AdjustInstanceWrapper instance;

  /** Wraps calls to {@link Adjust#getDefaultInstance()} */
  public static AdjustInstanceWrapper getDefaultInstance() {
    if (instance == null) {
      synchronized (AdjustWrapper.class) {
        if (instance == null) {
          instance = new AdjustInstanceWrapper();
        }
      }
    }
    return instance;
  }

  /**
   * Initializes Adjust wrapper by providing a {@link Context} and an event token to event name
   * mapping.
   *
   * @param context the context required to track custom events through Google Analytics for
   *     Firebase
   * @param tokenMap map to translate Adjust tokens into the untokenized name for Google Analytics
   *     for Firebase
   */
  public static void initialize(Context context, Map<String, String> tokenMap) {
    getDefaultInstance().initialize(context, tokenMap);
  }

  /** Wraps calls to {@link Adjust#trackEvent(AdjustEvent)} */
  public static void trackEvent(WrappedAdjustEvent event) {
    getDefaultInstance().trackEvent(event);
  }
}
