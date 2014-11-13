# Valuepotion SDK for Unity - Getting Started

## Before You Begin

### 1. Register Your App
Visit [Valuepotion](https://valuepotion.com) website and register the information of your app. After that, you will be given a **Client ID** and a **Secret Key**.

### 2. Import the SDK into your Unity project
Import **ValuepotionUnitySDK.unitypackage** from `Assets` > `Import Package` > `Custom Package` menu.

### 3. Create **ValuePotionManager** Object

1. Create a GameObject from `GameObject` > `Create Empty` menu.
2. Find the GameObject created at Hierarchy panel, then rename it to **ValuePotionManager**.
3. Click Add Component button from Inspector panel to connect to **Value Potion Manager** script.

### 4. Setup Client ID and Secret Key

Select **ValuePotionManager** object and type in Client ID and Secret Key at Inspector panel. If you're going to support both Android and iOS platforms, you will have different Client ID and Secret Key for each platform. You should visit [Valuepotion](https://valuepotion.com) website and create more if needed.

After the setup, you can create a Prefab from this **ValuePotionManager** GameObject. Then you can reuse it at any scene by just dragging and dropping the Prefab to Hierarchy panel.

### 5. Additional Integration with Android

#### Basic Configuration

##### Add Required Files

First, check if your Android SDK contains `Android Support Library` and `Google Play Services`. If not, please install them from Android SDK Manager. After that, copy the following three files to your Unity Project.

File Name        | File Path          | Destination Path
-----------------|--------------------|-------------------------------
**google-play-services.jar** | ANDROID_SDK_HOME/extras/google/google_play_services/libproject/google-play-services_lib/libs/google-play-services.jar | Assets/Plugins/Android/libs
**android-support-v4.jar** | ANDROID_SDK_HOME/extras/android/support/v4/android-support-v4.jar | Assets/Plugins/Android/libs
**version.xml** | ANDROID_SDK_HOME/extras/google/google_play_services/libproject/google-play-services_lib/res/values/version.xml | Assets/Plugins/Android/res/values


##### AndroidManifest.xml

###### Add google_play_services_version.
```xml
<meta-data android:name="com.google.android.gms.version"
           android:value="@integer/google_play_services_version" />
```

###### Add permissions.
```xml
<!-- Valuepotion Plugin Permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<!-- Valuepotion Plugin Permissions end -->
```

###### Add components of Valuepotion.
```xml
<!-- Valuepotion Components -->
	<!-- for GCM push-notification interface -->
	<activity
	    android:name="com.valuepotion.sdk.VPPopupActivity"
	    android:launchMode="singleInstance"
	    android:theme="@android:style/Theme.Translucent" >
	</activity>

	<!-- for GCM Push notification interface -->
	<activity
	    android:name="com.valuepotion.sdk.VPInterstitialActivity"
	    android:theme="@android:style/Theme.Translucent" >
	</activity>

	<!-- for CPI tracking -->
	<receiver
	    android:name="com.valuepotion.sdk.VPInstallReceiver"
	    android:exported="true" >
	    <intent-filter>
	        <action android:name="com.android.vending.INSTALL_REFERRER" />
	    </intent-filter>
	</receiver>
<!-- Valuepotion Components End -->
```

#### onStart / onStop
Every activity has to include the following code:

```java
@Override
protected void onStart() {
  super.onStart();
  ValuePotion.getInstance().onStart(this);
}

@Override
protected void onStop() {
  super.onStop();
  ValuePotion.getInstance().onStop(this);
}
```

If you're using `UnityPlayerActivity` as your main activity, please create `MainActivity` extending `UnityPlayerActivity`. Replace it with `UnityPlayerActivity` from `AndroidManifest.xml` and add the code above into the `MainActivity`.



#### Integrate with GCM

Integrating with Unity GCM is based on [unity-gcm](https://github.com/kskkbys/unity-gcm) project. Via unity-gcm, we handle GCM Notification on Unity and integrate with Valuepotion.

You can check out source code on [unity-gcm-valuepotion](https://github.com/valuepotion/unity-gcm-valuepotion).

##### Add gcm.jar
Please put `gcm.jar` into `Assets/Plugins/Android/libs` directory of your Unity project. You can get `gcm.jar` from [here](https://code.google.com/p/gcm/source/browse/gcm-client/dist/gcm.jar?r=af0f427f11ec05c252d8424fffb9ff5521b59495).

##### AndroidManifest.xml

###### Add permissions for GCM

```xml
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
<!--
	Replace 'PACKAGE_NAME' to Your App-PackageName
	ex)
    If 'package' attribute in <application> tag is 'com.valuepotion.testapp',
    then set 'com.valuepotion.testapp.permission.C2D_MESSAGE'.
-->
<permission android:name="PACKAGE_NAME.permission.C2D_MESSAGE" android:protectionLevel="signature" />
<uses-permission android:name="PACKAGE_NAME.permission.C2D_MESSAGE" />
```

###### Add components for GCM integration

```xml
<!--
	Replace 'PACKAGE_NAME' to Your App-PackageName
	ex)
    If 'package' attribute in <application> tag is 'com.valuepotion.testapp',
    then set 'com.valuepotion.testapp.permission.C2D_MESSAGE'.
-->
<receiver android:name="com.kskkbys.unitygcmplugin.UnityGCMBroadcastReceiver"
	android:permission="com.google.android.c2dm.permission.SEND"
	android:exported="true">
    <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
        <category android:name="PACKAGE_NAME" />
    </intent-filter>
</receiver>
<receiver android:name="com.valuepotion.sdk.push.GcmBroadcastReceiver">
    <intent-filter>
      <action android:name="com.valuepotion.sdk.push.NOTIFICATION_OPENED" />
    </intent-filter>
</receiver>
<service android:name="com.kskkbys.unitygcmplugin.UnityGCMIntentService" />
```

##### Unity Project

###### Configure GCM Project-ID on Unity code.

If you don't configure `project-id`, you won't be able to use GCM Push Notification.

**This MUST be done**

```java
#if UNITY_ANDROID && !UNITY_EDITOR
	GCM.Initialize ();
	GCM.Register ("project-id(number format)");
#endif
```

- If you don't want to use unity-gcm and want to handle GCM Push Notification on Android manually, you should refer to [this document](android_guide_en.md#integrate-push-notification).
- To obtain Android GCM Project-ID and API-KEY, visit
[http://developer.android.com/intl/ko/google/gcm/gs.html](http://developer.android.com/intl/ko/google/gcm/gs.html).
- For the sample of AndroidManifest.xml, you can refer to `AndroidManifestSample.xml` from Valuepotion Unity SDK.


## Initialize SDK

Initializing SDK can be done with just one line code. The earlier the initialization code are executed, the better. If you've done all right so far, you should be able to see statistics of session, install and update events on Valuepotion dashboard.

```java
ValuePotionManager.Initialize();
```

## Integrate with Interstitial Ads

### 1. Display Interstitial Ads
If you've created a campaign at [Valuepotion](https://valuepotion.com), you can display it as an interstitial ad at your own app. Before displaying interstitial ads, you should set up a placement. Otherwise, "default" placement will be used by default.

**Placement** is a name to distinguish many points where you want to display ads. There's no restriction but it just should be a string.

```java
// Display ads at "default" placement.
ValuePotionManager.OpenInterstitial(null);

// Display ads at "main_menu" placement.
ValuePotionManager.OpenInterstitial("main_menu");
```


### 2. Cache Interstitial Ads
Using `ValuePotionManager.OpenInterstitial()` method, the SDK will download data for ads via HTTP and display on screen. So it takes some time. If you cache ads when your game launches, you can display the ads at any time with no delay.

```java
// If you cache an ad for "after_login" placement once,
ValuePotionManager.CacneInterstitial("after_login");

...

// Later on, you can display the ad with no delay.
ValuePotionManager.OpenInterstitial("after_login");
```

### 3. Display Interstitial Ads Only When Caches are Available
You can display interstitial ads only when caches are available.

```java
// Check if the cache for "item_shop" placement exists.
if (ValuePotionManager.HasCachedInterstitial("item_shop")) {
	// then, display the ad for "item_shop" placement.
	ValuePotionManager.OpenInterstitial("item_shop");
}
```


## Event Tracking
You can analyze your game with event tracking. And based on events you can create cohort to use for marketing. There are non-payment event and payment event.

### 1. Non-Payment Event
Non-payment event is not related to In-App Purchase. You can use non-payment event to analyze user behavior. To use non-payment event, you should define its name and values. The following code is an example to send non-payment event.

```java
// User has got 3 items.
ValuePotionManager.TrackEvent("get_item_ruby", 3);
```

If there's no specific value needed, you can use event name only.

```java
// User has visited "item shop" menu.
ValuePotionManager.TrackEvent("enter_item_shop");
```

If you want to build a hierarchy of events, you can specify that like following:

```java
String category = "item";
String action = "get_ruby";
String label = "reward_for_login";
double value = 30;
ValuePotionManager.TrackEvent(category, action, label, value);
```


### 2. Payment Event
Payment event is tracked when In-App Purchase(In-App Billing) has occurred. If you track payment events, you can check daily statistics of Revenue, ARPU, ARPPU, PPU, etc.
The following code is an example to send payment event occurred in your game.

```java
// User purchased $0.99 coin item.
String orderId = "23847983247018237";                 // The identifier of receipt after completing purchase.
String productId = "com.valuepotion.tester.item_01";  // The identifier of item.
ValuePotionManager.TrackPurchaseEvent("purchase_coin", 0.99, "USD", orderId, productId);
```

Valuepotion provides campaign of In-App Purchase (IAP) type. When a user makes revenue via an ad of IAP type, if you add extra info to payment event, you can get revenue report per campaign in detail. The following code is how to send payment event which occurred from IAP ad.

* To see more information about `ValuePotionManager.OnRequestPurchase`, please see **OnRequestPurchase** item under **Advanced: Event** section *

```java
ValuePotionManager.OnRequestPurchase += OnRequestPurchaseHandler;

public void OnRequestPurchaseHandler(string placement, string name, string productId, int quantity, string campaignId, string contentId) {
  // Proceed the requested payment

  ...

  // User purchased some Diamond item for KRW 1,200.
  String orderId = "23847983247018237";                 // The identifier of receipt after completing purchase.
  String productId = "com.valuepotion.tester.item_01";  // The identifier of item.
  ValuePotionManager.TrackPurchaseEvent("iap_diamond", 1200, "KRW", orderId, productId, campaignId, contentId);
}
```

#### Reference
* For accurate analysis, please specify real purchase amount and currency.
* We follow [ISO 4217](http://en.wikipedia.org/wiki/ISO_4217) for currency.

### 3. Test If Event Tracking Works
You can test if event tracking works by using test mode of the SDK. The following code will activate test mode.

```java
// Activate test mode
ValuePotionManager.SetTest(true);
```

If you send events from an app built with test mode, you should see the events on developer's console at [Valuepotion](https://valuepotion.com) at real time.

**Warning** : Before submitting your app to app store, please disable test mode. Events sent form test mode are only displayed on Developer's console but excluded from analysis.



## Integrate User Information
You can collect user information as well as events. Possible fields of user information are user id, server id which user belongs to, birthdate, gender, level, number of friends and type of user account. All of them are optional so you can choose which fields to collect.

You can use this information for marketing by creating user cohort. You can update your information when it changes to integrate with Valuepotion.

```java
ValuePotionManager.SetUserId("support@valuepotion.com");
ValuePotionManager.SetUserServerId("server1");
ValuePotionManager.SetUserBirth("19830328");
ValuePotionManager.SetUserGender("M");
ValuePotionManager.SetUserLevel(32);
ValuePotionManager.SetUserFriends(219);
ValuePotionManager.SetUserAccountType("guest");
```

The following is the detail on each field.

Field           | Description
--------------- | ------------
**userId**      | User account id used in game
**serverId**    | If you need to distinguish users by server which they belong to, you should set serverId.<br>Then you can get statistics based on serverId.
**birth**       | Date of birth in YYYYMMDD. <br>If you know only year of birth, fill last four digits with "0" like "19840000".<br>If you know only date of birth(but not year), fill first four digits with "0" like "00001109".
**gender**      | "M" for male, "F" for female.
**level**       | Level of user in game.
**friends**     | Number of user's friends.
**accountType** | Type of user account. (facebook, google, guest, etc.)


## Integrate Push Notification
If you integrate with Push Notification API, you can easily create campaigns of Push type and send message to users. So you can wake up users who haven't played game for long time, or you can also notify users new events in game, etc.

### 1. Register Certificate
Visit [Valuepotion](https://valuepotion.com) website and update your app information.

* **Android App** : Put **Push Token** at **App Edit** page.
* **iOS App** : Upload Push Notification Certificate file(*.pem) at **App Edit** page.

To obtain Android GCM Project-ID and API-KEY, visit
[http://developer.android.com/intl/ko/google/gcm/gs.html](http://developer.android.com/intl/ko/google/gcm/gs.html).

### 2. Set up Push Notification
Enable Push Notification at Unity project like following.

```java
// Enabling Push Notification
ValuePotionManager.SetPushEnable(true);
```

If you want to disable Push Notification, run the following code.

```java
// Disabling Push Notification
ValuePotionManager.SetPushEnable(false);
```

If you want to know whether Push Notification is now enabled, run the following code.

```java
// The following code returns boolean value indicating whether Push Notification is enabled.
ValuePotionManager.IsPushEnabled();
```

## Build

### 1. Android

* Open `File` > `Build Settings` menu from Unity project.
* Run `Build` or `Build And Run`,
* or create Android project and build it by checking `Google Android Project` and pressing `Export`.

### 2. iOS

* Create Xcode project for iOS from `File` > `Build Settings` menu at Unity project.
* Open Build Settings tab from Xcode project, and add `-ObjC` flag to Other Linker Flags item.
* Open Build Phases tab, and add `AdSupport.framework`, `CoreTelephony.framework` to Link Binary With Libraries item.
* Run app from `Product` > `Run` menu.


## Advanced: Event
ValuePotionManager has events defined to integrate with campaigns. All the event handlers are optional. You can choose and implement handlers as you need.

### 1. Events for Displaying Interstitial Ad
#### ValuePotionManager.OnOpenInterstitial
This event occurs when displaying interstitial ad is successfully done after calling `ValuePotionManager.OpenInterstitial()` method.

```java
ValuePotionManager.OnOpenInterstitial += OnOpenInterstitialHandler;
public void OnOpenInterstitialHandler(string placement) {
	// Put something you need to do when interstitial ad is displayed.
	// For example, you can pause game here.
}
```

#### ValuePotionManager.OnFailToOpenInterstitial
This event occurs when displaying interstitial ad is failed after calling `ValuePotionManager.OpenInterstitial()` method.

```java
ValuePotionManager.OnFailToOpenInterstitial += OnFailToOpenInterstitialHandler;
public void OnFailToOpenInterstitialHandler(string placement, string errorMessage) {
	// Put something you need to do when interstitial ad gets failed.
	// You can check reason of failure via errorMessage variable.
}
```

#### ValuePotionManager.OnCloseInterstitial
This event occurs when interstitial ad closes.

```java
ValuePotionManager.OnCloseInterstitial += OnCloseInterstitialHandler;
public void OnCloseInterstitialHandler(string placement) {
	// Put something you need to do when interstitial ad closes.
	// If you paused your game during ad is open, now you can resume it here.
}
```

### 2. Events for Caching Interstitial Ad
#### ValuePotionManager.OnCacheInterstitial
This event occurs when caching interstitial ad is successfully done after calling `ValuePotionManger.CacheInterstitial()` method.

```java
ValuePotionManager.OnCacheInterstitial += OnCacheInterstitialHandler;
public void OnCacheInterstitialHandler(string placement) {
	// Put something you need to do when caching interstitial ad is successfully done
}
```

#### ValuePotionManager.OnFailToCacheInterstitial
This event occurs when caching interstitial ad is failed after calling `ValuePotionManager.CacheIntestitial()` method.

```java
ValuePotionManager.OnFailToCacheInterstitial += OnFailToCacheInterstitialHandler;
public void OnFailToCacheInterstitialHandler(string placement, string errorMessage) {
	// Put something you need to do when caching interstitial ad is failed.
	// You can check reason of failure via errorMessage variable.
}
```

### 3. Events for Interstitial Ad Action
#### ValuePotionManager.OnRequestOpenUrl
This event occurs when user clicks external url while interstitial ad is displayed.

```java
ValuePotionManager.OnRequestOpenUrl += OnRequestOpenUrlHandler;
public void OnRequestOpenUrlHandler(string placement, string url) {
	// Put something you need to do when external url gets opened.
	// App soon goes background, so you can do something like saving user data, etc.
}
```

#### ValuePotionManager.OnRequestPurchase
This event occurs when user pressed 'Purchase' button while interstitial ad of IAP type is displayed.

```java
ValuePotionManager.OnRequestPurchase += OnRequestPurchaseHandler;
public void OnRequestPurchaseHandler(string placement, string name, string productId, int quantity, string campaignId, string contentId) {
	// Put codes to process real purchase by using parameters: productId, quantity.
	// After purchase, call ValuePotionManager.TrackPurchaseEvent() method for revenue report.
}
```

#### ValuePotionManager.OnRequestReward
This event occurs when interstitial ad of Reward type is displayed.

```java
ValuePotionManager.OnRequestReward += OnRequestRewardHandler;
public void OnRequestRewardHandler(string placement, Dictionary<string, object>[] rewards) {
	// Array 'rewards' contains rewards which ad is about to give users.
	// With this information you should implement actual code to give rewards to users.
	for (int i = 0; i < rewards.Length; i++) {
		// The names of quantities of rewards to give
		Debug.Log(rewards[i]["name"] + ", " + rewards[i]["quantity"].ToString());
	}
}
```

## Misc.
### 1. Push Notification LED (for Android)
When users receive push notification and their phone screen is off, you can make the LED blink.
```java
  // When push notification is received, LED lamp will blink every 0.5 seconds.
  ValuePotionManager.SetNotificationLights(0xff0000, 500, 500);
```
The code above will save its configuration in SharedPreference and the configuration will be used when user receives push notification. If you want to revert the configuration, please delete that code line. Then remove and install the app.

### 2. Push Notification Vibration (for Android)
You can customize vibration pattern when user receives push notification.
```java
  ValuePotionManager.SetNotificationVibrate(new long[] {0, 300, 500, 1000, 500, 400, 100});
```
The code above will save its configuration in SharedPreference and the configuration will be used when user receives push notification. If you want to revert the configuration, please delete that code line. Then remove and install the app. To see more about vibration pattern, please refer to [Android SDK document](http://developer.android.com/reference/android/os/Vibrator.html).

To use notification vibration, you need to add the following permission.

```xml
  <uses-permission android:name="android.permission.VIBRATE" />
```
