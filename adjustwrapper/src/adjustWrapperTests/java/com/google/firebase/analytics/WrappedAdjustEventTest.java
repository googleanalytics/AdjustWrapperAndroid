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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/** API tests to verify wrapper functionality */
@RunWith(MockitoJUnitRunner.class)
public class WrappedAdjustEventTest {

  WrappedAdjustEvent event = new WrappedAdjustEvent("123456");

  @Test
  public void testValidRevenueAndCurrency() {
    event.setRevenue(3.1D, "USD");

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(2, gaParams.size());
    assertEquals(3.1D, gaParams.get("value"));
    assertEquals("USD", gaParams.get("currency"));
  }

  @Test
  public void testValidRevenueNoCurrency() {
    event.setRevenue(3.1D, null);

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(1, gaParams.size());
    assertEquals(3.1D, gaParams.get("value"));
  }

  @Test
  public void testValidCurrencyThenSetToNull() {
    event.setRevenue(3.1D, "USD");
    event.setRevenue(4.2D, null);

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(1, gaParams.size());
    assertEquals(4.2D, gaParams.get("value"));
  }

  @Test
  public void testInvalidCurrency() {
    event.setRevenue(3.1D, "US");

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(2, gaParams.size());
    assertEquals(3.1D, gaParams.get("value"));
    assertEquals("US", gaParams.get("currency"));
  }

  @Test
  public void testValidOrderId() {
    event.setOrderId("1");

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(1, gaParams.size());
    assertEquals("1", gaParams.get("transaction_id"));
  }

  @Test
  public void testNullOrderId() {
    event.setOrderId(null);

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(0, gaParams.size());
  }

  @Test
  public void testValidOrderIdThenSetToNull() {
    event.setOrderId("1");
    event.setOrderId(null);

    Map<String, Object> gaParams = new HashMap<>(event.getGoogleAnalyticsParams());
    assertEquals(0, gaParams.size());
  }
}
