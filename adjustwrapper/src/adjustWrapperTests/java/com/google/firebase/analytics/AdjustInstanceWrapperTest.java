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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import com.adjust.sdk.AdjustInstance;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * API tests to verify wrapper functionality and calls are correctly routed to {@link
 * AdjustInstance}
 */
@RunWith(MockitoJUnitRunner.class)
public class AdjustInstanceWrapperTest {
  @Mock GoogleAnalyticsAdapter googleAnalyticsAdapter;
  @Mock AdjustInstance adjust;

  AdjustInstanceWrapper wrapper = new MockAdjustInstanceWrapper();

  @Before
  public void setup() {
    doReturn(googleAnalyticsAdapter)
        .when(googleAnalyticsAdapter)
        .registerContext(ArgumentMatchers.<Context>any());
  }

  @Test
  public void testTrackEvent_ValidTokenFormat_NoMap() {
    WrappedAdjustEvent event = new WrappedAdjustEvent("123456");
    wrapper.trackEvent(event);

    verify(adjust).trackEvent(ArgumentMatchers.eq(event));

    verify(googleAnalyticsAdapter)
        .logEvent(
            ArgumentMatchers.eq("123456"),
            ArgumentMatchers.eq(Collections.<String, Object>emptyMap()));
  }

  @Test
  public void testTrackEvent_InvalidTokenFormat_NoMap() {
    WrappedAdjustEvent event = new WrappedAdjustEvent("name");
    wrapper.trackEvent(event);

    verify(adjust).trackEvent(ArgumentMatchers.eq(event));

    verify(googleAnalyticsAdapter)
        .logEvent(
            ArgumentMatchers.eq("name"),
            ArgumentMatchers.eq(Collections.<String, Object>emptyMap()));
  }

  @Test
  public void testTrackEvent_ExplicitlyNullMap() {
    Context context = mock(Context.class);
    wrapper.initialize(context, null);

    WrappedAdjustEvent event = new WrappedAdjustEvent("123456");
    wrapper.trackEvent(event);

    verify(adjust).trackEvent(ArgumentMatchers.eq(event));

    verify(googleAnalyticsAdapter)
        .logEvent(
            ArgumentMatchers.eq("123456"),
            ArgumentMatchers.eq(Collections.<String, Object>emptyMap()));
  }

  @Test
  public void testTrackEvent_TokenInMap() {
    Context context = mock(Context.class);
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("123456", "level_up");
    tokenMap.put("234567", "add_to_cart");
    wrapper.initialize(context, tokenMap);

    WrappedAdjustEvent event = new WrappedAdjustEvent("123456");
    wrapper.trackEvent(event);

    verify(adjust).trackEvent(ArgumentMatchers.eq(event));

    verify(googleAnalyticsAdapter)
        .logEvent(
            ArgumentMatchers.eq("level_up"),
            ArgumentMatchers.eq(Collections.<String, Object>emptyMap()));
  }

  @Test
  public void testTrackEvent_TokenNotInMap() {
    Context context = mock(Context.class);
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("123456", "level_up");
    tokenMap.put("234567", "add_to_cart");
    wrapper.initialize(context, tokenMap);

    WrappedAdjustEvent event = new WrappedAdjustEvent("867530");
    wrapper.trackEvent(event);

    verify(adjust).trackEvent(ArgumentMatchers.eq(event));

    verify(googleAnalyticsAdapter)
        .logEvent(
            ArgumentMatchers.eq("867530"),
            ArgumentMatchers.eq(Collections.<String, Object>emptyMap()));
  }

  @Test
  public void testInitialize_ValidContext() {
    Context context = mock(Context.class);
    wrapper.initialize(context, null);
    verify(googleAnalyticsAdapter).registerContext(ArgumentMatchers.eq(context));
  }

  class MockAdjustInstanceWrapper extends AdjustInstanceWrapper {
    @Override
    GoogleAnalyticsAdapter getGoogleAnalytics() {
      return googleAnalyticsAdapter;
    }

    @Override
    AdjustInstance getAdjust() {
      return adjust;
    }
  }
}
