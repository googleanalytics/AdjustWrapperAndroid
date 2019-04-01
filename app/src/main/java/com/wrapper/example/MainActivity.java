/**
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wrapper.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.google.firebase.analytics.AdjustWrapper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.WrappedAdjustEvent;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Initializing Wrapper
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("abcde1", FirebaseAnalytics.Event.SIGN_UP);
    tokenMap.put("abcde2", FirebaseAnalytics.Event.TUTORIAL_BEGIN);
    tokenMap.put("fghijk", "your_custom_event");
    AdjustWrapper.initialize(this, tokenMap);

    // Initializing Adjust
    Adjust.onCreate(new AdjustConfig(this, "your-app-token", AdjustConfig.ENVIRONMENT_SANDBOX));

    // Tracking Events
    WrappedAdjustEvent event = new WrappedAdjustEvent("abcde1");
    event.setRevenue(0.01D, "EUR");
    AdjustWrapper.trackEvent(event);
  }
}
