# Google Analytics for Firebase Wrapper for Adjust

_Copyright (c) 2019 Google Inc. All rights reserved._

The __Google Analytics for Firebase__ wrapper for Adjust allows developers to
easily send information to both the Adjust and Google Analytics for Firebase
backends.

## Using the Wrapper

In order to use the Adjust wrapper:

1.  [Follow the steps here](https://firebase.google.com/docs/analytics/android/start)
    to set up the Google Analytics for Firebase SDK in your app.
2.  Copy the source files inside the AdjustWrapper directory into your project.
3.  Register your Adjust token to event mapping and application `Context` using
    `AdjustWrapper.initialize()` after Firebase initialization. This mapping
    allows the wrapper to log events to the Google Analytics SDK with the real
    name (not just the token).
4.  Replace supported references to `Adjust` API with `AdjustWrapper` and
    `AdjustInstance` with `AdjustInstanceWrapper`
5.  Replace references to `AdjustEvent` with `WrappedAdjustEvent`.

Some methods are not supported by the wrapper. For these methods, directly call
the base implementation on the `Adjust` object or your `AdjustInstance`
reference.

### Registering the Event Mapping and Application Context

Instead of using event names within the SDK, Adjust uses tokens that map to
event names on the backend. Since the Google Analytics for Firebase SDK is
unaware of this mapping, it should be provided to the wrapper to ensure that the
event names show up properly in the Firebase console. Register it by passing a
map into the `AdjustWrapper.initialize()` API. The map should reflect the token
to event name mapping provided by Adjust. Make sure that this mapping is
established before the first Adjust event is logged, or event name shown in the
Firebase console will be incorrect.

In addition, a `Context` is required before the wrapper can log custom events
through Google Analytics for Firebase. Events logged before a `Context` is
provided will be queued on the device until provided using the
`AdjustWrapper.initialize()` API.

Example:

```
Map<String, String> tokenMap = new HashMap<>();
tokenMap.put("abcde1", FirebaseAnalytics.Event.SIGN_UP);
tokenMap.put("abcde2", FirebaseAnalytics.Event.TUTORIAL_BEGIN);
tokenMap.put("fghijk", "your_custom_event");

AdjustWrapper.initialize(context, tokenMap);
```

### Supported Methods

The following API methods and properties are supported in the AppsFlyer wrapper.
Use the wrapper by replacing `Adjust` in these instances with `AdjustWrapper`:

*   `Adjust.trackEvent(AdjustEvent)` ->
    `AdjustWrapper.trackEvent(WrappedAdjustEvent)`

Replace `AdjustEvent` with `WrappedAdjustEvent` in these instances:

*   `new AdjustEvent(token)` -> `new WrappedAdjustEvent(token)`
