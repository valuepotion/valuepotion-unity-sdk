# Valuepotion Unity SDK 통합 가이드

## 기본 설정

### 1. 앱 정보 등록
먼저 [밸류포션](https://valuepotion.com) 웹사이트에 방문하여 SDK를 적용할 앱의 정보를 등록합니다. 앱 정보 등록을 완료하면 Client ID 와 Secret Key 가 발급됩니다.

### 2. Unity 프로젝트에 SDK 임포트
`Assets` > `Import Package` > `Custom Package` 메뉴를 선택하여 **ValuepotionUnitySDK.unitypackage** 파일을 임포트 시킵니다.

### 3. ValuePotionManager 오브젝트 생성
`GameObject` > `Create Empty` 메뉴를 선택하여 게임 오브젝트를 생성합니다. 생성된 게임 오브젝트는 Hierarchy 패널에서 확인 가능합니다. 해당 오브젝트의 이름을 ValuePotionManager 로 변경한 후, Inspector 패널에서 Add Component 버튼을 클릭하여 Value Potion Manager 스크립트와 연결 하십시오.

### 4. Client ID, Secret Key 설정
ValuePotionManager 오브젝트를 선택한 후, Inspector 패널로 이동하여 앞서 발급받은 Client ID 와 Secret Key 를 입력합니다. 만약 Android / iOS 두 플랫폼을 모두 지원하고자 한다면, [밸류포션](https://valuepotion.com) 웹사이트에서 각 플랫폼 별로 앱을 등록하여 별도의 Client ID 와 Secret Key를 발급받아 사용해야 합니다.

이렇게 설정이 완료된 ValuePotionManager 오브젝트를 Prefab 으로 만들어 두면, 여러 Scene 에서 해당 Prefab 을 Hierarchy 패널에 끌어다 놓는 것 만으로 재사용이 가능해 편리합니다.

### 5. 안드로이드를 위한 추가 설정

#### 기본 설정

##### 필수 파일 추가
먼저 안드로이드 SDK에 `Android Support Library`, `Google Play Services` 패키지가 포함되어 있는지 확인하십시오. 만약 포함되어 있지 않다면, Android SDK Manager를 이용해 설치하시기 바랍니다. 그 다음 아래의 3가지 파일을 Unity 프로젝트에 추가합니다.

파일 명            | 파일 경로            | 복사할 경로
-----------------|--------------------|-------------------------------
**google-play-services.jar** | ANDROID_SDK_HOME/extras/google/google_play_services/libproject/google-play-services_lib/libs/google-play-services.jar | Assets/Plugins/Android/libs
**android-support-v4.jar** | ANDROID_SDK_HOME/extras/android/support/v4/android-support-v4.jar | Assets/Plugins/Android/libs
**version.xml** | ANDROID_SDK_HOME/extras/google/google_play_services/libproject/google-play-services_lib/res/values/version.xml | Assets/Plugins/Android/res/values

##### AndroidManifest.xml

###### google_play_services_version 등록
```xml
<application ...>
...
	<meta-data android:name="com.google.android.gms.version"
        	android:value="@integer/google_play_services_version" />
```

###### 퍼미션 등록

```xml
<!-- Valuepotion Plugin Permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
<!-- Valuepotion Plugin Permissions end -->

<application ...>
...
```

###### Valuepotion 컴포넌트 등록

```xml
<application ...>
...
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
앱 내에서 사용하는 모든 액티비티에 아래의 코드를 삽입해야 합니다.

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

이미 `UnityPlayerActivity` 나 `UnityPlayerNativeActivity` 를 직접 상속해서 만든 activity class 파일을 사용하고 있는 경우에는 그 java 파일에 위 코드를 삽입해주기만 하면 됩니다.
그렇지 않다면 `UnityPlayerActivity` 혹은 `UnityPlayerNativeActivity` 를 상속받는 java class 파일을 생성하고, 그 파일에 위 내용을 삽입해야 합니다. 하지만 보다 손쉽게 사용하실 수 있도록 `vpunityactivity.jar`를 제공해 드리기 때문에 그냥 `vpunityactivity.jar`를 사용하셔도 됩니다. `AndroidManifest.xml`에서 메인 액티비티의 tag 를 찾고, `android:name` 속성의 값을 `com.valuepotion.sdk.unity.android.VPUnityActivity` 로 교체해주세요.


#### GCM 지원

Valuepotion의 Unity GCM 지원은 [unity-gcm](https://github.com/kskkbys/unity-gcm) 프로젝트에 기반하고 있습니다. unity-gcm을 통해 Unity상에서 GCM Notification을 처리할 수 있고, 자동적으로 Valuepotion으로의 연동도 이루어집니다. 소스코드는 [unity-gcm-valuepotion](https://github.com/valuepotion/unity-gcm-valuepotion)에서 확인하실 수 있습니다.


##### gcm.jar 추가
Unity 프로젝트의 `Assets/Plugins/Android/libs` 디렉토리에 `gcm.jar` 파일을 추가하십시오.
해당 파일은 [이곳](https://code.google.com/p/gcm/source/browse/gcm-client/dist/gcm.jar?r=af0f427f11ec05c252d8424fffb9ff5521b59495)에서 받으실 수 있습니다.

##### AndroidManifest.xml

###### GCM 퍼미션 선언 및 등록

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

###### GCM 지원 컴포넌트 등록

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
<receiver android:name="com.valuepotion.sdk.push.NotificationOpenedReceiver">
    <intent-filter>
      <action android:name="com.valuepotion.sdk.push.NOTIFICATION_OPENED" />
    </intent-filter>
</receiver>
<service android:name="com.kskkbys.unitygcmplugin.UnityGCMIntentService" />
```

##### Unity Project

###### Unity 코드상에서 GCM Project-ID 설정
`project-id` 를 설정하지 않으면, GCM Push Notification을 이용할 수 없으므로, **반드시 설정 해야 합니다.**

```java
#if UNITY_ANDROID && !UNITY_EDITOR
	GCM.Initialize ();
	GCM.Register ("project-id(number format)");
#endif
```

- unity-gcm을 이용하지 않고, 직접 Android 소스코드에서 GCM Push Notification을 연동하고 싶은 경우에는 [이 문서](android_guide_ko.md#push-notification-%EC%97%B0%EB%8F%99)를 참고하십시오.
- Android GCM Project-ID와 API-KEY를 얻는 방법은 [이 문서](
http://developer.android.com/intl/ko/google/gcm/gs.html)를 참고하십시오.
- AndroidManifest.xml 예제는 Valuepotion Unity SDK에 포함된 `AndroidManifestSample.xml` 파일을 참고하십시오.


## SDK 초기화
SDK 의 초기화는 다음과 같이 단 한 줄의 코드만으로 완료됩니다. 초기화 코드는 가능하면 게임 실행 후 가장 먼저 호출될 수 있는 위치에 작성하십시오. 여기까지 설정하면 기본적인 session / install / update 이벤트 트래킹이 가능합니다.

```java
ValuePotionManager.Initialize();
```

## 인터스티셜 광고 연동

### 1. 인터스티셜 광고 노출하기
[밸류포션](https://valuepotion.com) 웹 사이트에서 생성한 캠페인을 인터스티셜 광고의 형태로 자신의 앱에 노출시킬 수 있습니다. 인터스티셜 광고를 화면에 띄우기 위해서는 플레이스먼트를 지정해야 하며, 지정하지 않는 경우 "default" 플레이스먼트가 사용됩니다.

플레이스먼트는 게임 내의 여러 지점에서 원하는 광고를 노출 시킬 수 있도록 하기 위해 부여하는 이름으로, 특별한 제약 없이 원하는 이름을
문자열로 지정하면 됩니다.

```java
// "default" 플레이스먼트에 대해 광고를 노출 합니다.
ValuePotionManager.OpenInterstitial(null);

// "main_menu" 플레이스먼트에 대해 광고를 노출 합니다.
ValuePotionManager.OpenInterstitial("main_menu");
```


### 2. 인터스티셜 광고 캐싱하기
`ValuePotionManager.OpenInterstitial()` 메소드를 사용하면 HTTP 를 통해 광고 데이터를 받아온 후 화면에 보여주기 때문에, 네트워크 상태에 따라 다소 지연이 발생할 수 있습니다. 최초 게임 구동 시 원하는 플레이스먼트에 대해 광고를 캐싱해두면,
이후 원하는 시점에 지연 없이 해당 광고를 화면에 노출시킬 수 있습니다.

```java
// 최초 "after_login" 플레이스먼트에 대해 광고를 캐싱합니다.
ValuePotionManager.CacheInterstitial("after_login");

...

// 원하는 시점에 "after_login" 플레이스먼트에 대해 광고를 노출합니다.
ValuePotionManager.OpenInterstitial("after_login");
```

### 3. 캐시가 있을 때만 인터스티셜 광고 노출하기
특정 플레이스먼트에 캐싱된 광고가 확실히 존재할 때에만 광고를 노출시킬 수도 있습니다.

```java
// "item_shop" 플레이스먼트에 캐싱된 광고가 존재하는지 체크합니다.
if (ValuePotionManager.HasCachedInterstitial("item_shop")) {
	// "item_shop" 플레이스먼트에 대해 광고를 노출합니다.
	ValuePotionManager.OpenInterstitial("item_shop");
}
```


## 이벤트 트래킹
이벤트 트래킹 기능을 통해 게임에 대한 보다 세밀한 분석이 가능합니다. 또한, 이를 기반으로 유저 코호트를 생성하여 마케팅에 활용할 수 있습니다. 이벤트는 크게 비결제 이벤트와 결제 이벤트로 나뉩니다.

### 1.비결제 이벤트 트래킹
비결제 이벤트는 게임 내 결제와 무관한 이벤트로, 주로 사용자 행태 분석을 위해 사용합니다. 비결제 이벤트 트래킹을 위해서는 이벤트의 이름과 값을 지정해야 합니다. 다음은 비결제 이벤트를 전송하는 예제입니다.

```java
// 사용자가 3개의 아이템을 획득
ValuePotionManager.TrackEvent("get_item_ruby", 3);
```

특별한 값이 필요치 않은 이벤트인 경우, 간단히 이벤트 이름만을 지정하여도 됩니다.

```java
// 사용자가 item shop 메뉴에 방문
ValuePotionManager.TrackEvent("enter_item_shop");
```

이벤트에 계층을 두어 구분하고 싶을 때는 다음과 같이 사용할 수 있습니다.

```java
String category = "item";
String action = "get_ruby";
String label = "reward_for_login";
double value = 30;
ValuePotionManager.TrackEvent(category, action, label, value);
```


### 2. 결제 이벤트 트래킹
결제 이벤트는 게임 내 구매(In App Billing 또는 In App Purchase)가 발생했을 때 전송하는 이벤트입니다. 결제 이벤트를 트래킹하면 매출액, ARPU, ARPPU, PPU 등 유용한 지표들의 추이를 매일 확인할 수 있습니다.
다음은 게임 내에서 발생한 결제 이벤트를 전송하는 예제입니다.

```java
// 0.99 달러의 코인 아이템 구매가 발생
String orderId = "23847983247018237";                 // 결제 성공 후 발행된 영수증 번호
String productId = "com.valuepotion.tester.item_01";  // 아이템의 식별자
ValuePotionManager.TrackPurchaseEvent("purchase_coin", 0.99, "USD", orderId, productId);
```

밸류포션은 In App Purchase (이하 IAP) 타입의 캠페인을 제공합니다. 게임 사용자가 IAP 타입의 광고를 통해 매출을 발생시킨 경우, 결제 이벤트에 추가 정보를 더해 전송하면 더욱 상세한 캠페인 별 매출 리포트를 제공 받으실 수 있습니다. 다음은 IAP 광고로부터 발생한 결제 이벤트를 전송하는 예제입니다.

*`ValuePotionManager.OnRequestPurchase` 이벤트에 대한 보다 자세한 정보는 **고급: 이벤트** 섹션의 **OnRequestPurchase** 항목을 참고하십시오.*

```java
ValuePotionManager.OnRequestPurchase += OnRequestPurchaseHandler;

public void OnRequestPurchaseHandler(string placement, string name, string productId, int quantity, string campaignId, string contentId) {
  // 요청받은 결제를 진행합니다.

  ...

  // 1,200 원의 다이아몬드 아이템 구매가 발생.
  String orderId = "23847983247018237";                 // 결제 성공 후 발행된 영수증 번호
  String productId = "com.valuepotion.tester.item_01";  // 아이템의 식별자
  ValuePotionManager.TrackPurchaseEvent("iap_diamond", 1200, "KRW", orderId, productId, campaignId, contentId);
}
```

비결제 이벤트처럼 category 와 label 을 지정할 수 있습니다.
```
ValuePotionManager.TrackPurchaseEvent(category, eventName, label, amount, currency, orderId, productId, campaignId, contentId);
```


#### 참고
* 정확한 집계를 위해, 결제 이벤트 전송 시에는 실제 발생한 결제 금액과 통화 코드를 지정해주십시오.
* 통화 코드는 [ISO 4217](http://en.wikipedia.org/wiki/ISO_4217) 표준을 따릅니다.

### 3. 이벤트 트래킹 테스트
SDK의 테스트 모드를 통해 정상적으로 이벤트가 전송되는지 여부를 쉽게 확인할 수 있습니다. 테스트 모드를 활성화 시키는 방법은 다음과 같습니다.

```java
// 테스트 모드로 설정
ValuePotionManager.SetTest(true);
```

테스트 모드로 빌드된 앱에서 전송되는 이벤트는 [밸류포션](https://valuepotion.com) 웹사이트의 개발자 콘솔 메뉴에서 실시간으로 확인 가능합니다.

**주의** : 앱 스토어에 제출하기 위한 최종 빌드 시에는 반드시 테스트 모드를 해제하십시오. 테스트 모드에서 전송된 이벤트는 개발자 콘솔 메뉴에서만 출력되고, 실제 집계에서는 제외됩니다.



## 사용자 정보 연동
이벤트 트래킹과는 별도로, 게임 사용자의 추가 정보에 대한 수집이 가능합니다. 현재 밸류포션에서 지원하는 사용자 정보는 사용자의 계정 ID, 사용자가 속한 게임 서버의 ID, 생년월일, 성별, 레벨, 친구 수, 계정 타입의 7가지입니다. 모든 항목은 선택적이므로, 필요치 않다면 어떤 것도 설정할 필요가 없습니다.

이 정보들을 이용해 유저 코호트를 생성하여 마케팅에 활용할 수있습니다. 사용자 정보는 게임의 진행 중 변경이 있을 때마다 새로이 설정하여 주시면 자동으로 밸류포션과 연동됩니다.

```java
ValuePotionManager.SetUserId("support@valuepotion.com");
ValuePotionManager.SetUserServerId("server1");
ValuePotionManager.SetUserBirth("19830328");
ValuePotionManager.SetUserGender("M");
ValuePotionManager.SetUserLevel(32);
ValuePotionManager.SetUserFriends(219);
ValuePotionManager.SetUserAccountType("guest");
```

각 사용자 정보 항목에 대한 세부 내용은 다음과 같습니다.

이름             | 설명
--------------- | ------------
**userId**      | 게임 내에서 사용되는 사용자의 계정 id를 설정합니다.
**serverId**    | 게임 유저를 서버 별로 식별해야 하는 경우 유저가 속한 서버의 id를 설정합니다.<br>serverId를 기준으로 서버별 통계를 확인할 수 있습니다.
**birth**       | 사용자의 생년월일 8자리를 문자열로 세팅합니다.<br>연도 정보만 아는 경우 "19840000"과 같이 뒤 4자리를 0으로 채웁니다.<br>생일 정보만 아는 경우 "00001109"와 같이 앞 4자리를 0으로 채웁니다.
**gender**      | 남성인 경우 "M", 여성인 경우 "F" 문자열로 설정합니다.
**level**       | 사용자의 게임 내 레벨을 설정합니다.
**friends**     | 사용자의 친구 수를 설정합니다.
**accountType** | 사용자의 로그인 계정 타입을 설정합니다. (facebook, google, guest 등)


## Push Notification 연동
밸류포션 Push Notification API와 연동하면, 손쉽게 Push 타입의 캠페인을 생성하여 사용자에게 메시지를 전송할 수 있습니다. 장기간 게임을 플레이 하지 않은 유저들이 다시 접속하도록 유도하거나, 게임 내 이벤트 소식을 알리는 등 다방면으로 활용이 가능합니다.

### 1. 인증서 등록
[밸류포션](https://valuepotion.com) 웹사이트에서 앞서 등록한 앱 정보를 업데이트 해야 합니다.

* **안드로이드 앱인 경우** : 앱 정보 수정 페이지에서 Push API-key 를 입력하십시오.
* **iOS 앱인 경우** : 앱 정보 수정 페이지에서 Push Notification 인증서 파일(*.pem)을 업로드 하십시오.

Android GCM Project-ID와 API-KEY를 얻는 방법은
http://developer.android.com/intl/ko/google/gcm/gs.html 를 참고하세요.

### 2. Push Notification 설정
Unity 프로젝트에서 다음과 같이 Push Notification을 활성화 시킵니다.

```java
// Push Notification 수신 가능한 상태가 됩니다.
ValuePotionManager.SetPushEnable(true);
```

만약 Push Notification 활성화 이후 다시 비활성화 시키고 싶다면, 다음과 같이 설정하면 됩니다.

```java
// Push Notification을 받지 않도록 변경됩니다.
ValuePotionManager.SetPushEnable(false);
```

현재 Push Notification 활성화 여부를 확인할 필요가 있을 때에는, 다음의 예제를 참고하십시오.

```java
// Push Notification 활성화 여부를 bool 타입으로 리턴합니다.
ValuePotionManager.IsPushEnabled();
```

## 빌드

### 1. Android

* Unity 프로젝트에서 `File` > `Build Settings` 메뉴를 엽니다.
* `Build` 또는 `Build And Run` 으로 바로 빌드하거나,
* `Google Android Project` 체크박스를 켜고 `Export`를 눌러, Android 프로젝트를 생성한 후 빌드합니다.

### 2. iOS

* Unity 프로젝트에서 `File` > `Build Settings` 메뉴를 통해 iOS 용 Xcode 프로젝트를 생성합니다.
* Xcode 프로젝트의 Build Settings 탭에서 Other Linker Flags 항목에 `-ObjC` 플래그를 추가합니다.
* Xcode 프로젝트의 Build Phases 탭에서 Link Binary With Libraries 항목에 `AdSupport.framework`, `CoreTelephony.framework`을 추가합니다.
* Xcode 프로젝트에서 `Product` > `Run` 메뉴를 선택하여 앱을 실행합니다.


## 고급: 이벤트
ValuePotionManager에는 캠페인 연동 시 활용 가능한 이벤트들이 정의되어 있습니다. 모든 이벤트에 대한 핸들러의 구현은 선택 사항입니다. 따라서 필요한 이벤트에 대한 핸들러만 구현하여 사용하시면 됩니다.

### 1. Interstitial 노출 이벤트
#### ValuePotionManager.OnOpenInterstitial
`ValuePotionManager.OpenInterstitial()` 메소드 호출 후, 인터스티셜 광고가 성공적으로 화면에 노출되는 시점에 발생합니다.

```java
ValuePotionManager.OnOpenInterstitial += OnOpenInterstitialHandler;
public void OnOpenInterstitialHandler(string placement) {
	// 인터스티셜 광고가 열릴 때 필요한 작업이 있다면 여기에 구현합니다.
	// 실행 중인 게임을 pause 시키는 등의 처리를 할 수 있습니다.
}
```

#### ValuePotionManager.OnFailToOpenInterstitial
`ValuePotionManager.OpenInterstitial()` 메소드 호출 후, 인터스티셜 광고가 화면에 노출되지 못하는 경우 발생합니다.

```java
ValuePotionManager.OnFailToOpenInterstitial += OnFailToOpenInterstitialHandler;
public void OnFailToOpenInterstitialHandler(string placement, string errorMessage) {
	// 인터스티셜 광고 노출에 실패했을 때 필요한 작업이 있다면 여기에 구현합니다.
	// 실패한 원인은 errorMessage 를 통해 확인할 수 있습니다.
}
```

#### ValuePotionManager.OnCloseInterstitial
인터스티셜 광고가 열려있는 상태에서 닫힐 때 발생합니다.

```java
ValuePotionManager.OnCloseInterstitial += OnCloseInterstitialHandler;
public void OnCloseInterstitialHandler(string locatoin) {
	// 인터스티셜 광고가 닫힐 때 필요한 작업이 있다면 여기에 구현합니다.
	// 광고가 열려있는 동안 게임을 pause 시켰다면, 여기서 resume 시키는 등의 처리를 할 수 있습니다.
}
```

### 2. Interstitial 캐싱 이벤트
#### ValuePotionManager.OnCacheInterstitial
`ValuePotionManger.CacheInterstitial()` 메소드 호출 후, 성공적으로 광고가 캐싱 되었을 때 발생합니다.

```java
ValuePotionManager.OnCacheInterstitial += OnCacheInterstitialHandler;
public void OnCacheInterstitialHandler(string placement) {
	// 인터스티셜 광고 캐싱이 완료된 후 필요한 작업이 있다면 여기에 구현합니다.
}
```

#### ValuePotionManager.OnFailToCacheInterstitial
`ValuePotionManager.CacheIntestitial()` 메소드 호출 후, 광고 캐싱에 실패했을 때 발생합니다.

```java
ValuePotionManager.OnFailToCacheInterstitial += OnFailToCacheInterstitialHandler;
public void OnFailToCacheInterstitialHandler(string placement, string errorMessage) {
	// 인터스티셜 광고 캐싱에 실패했을 때 필요한 작업이 있다면 여기에 구현합니다.
	// 실패한 원인은 errorMessage 를 통해 확인할 수 있습니다.
}
```

### 3. Interstitial 액션 이벤트
#### ValuePotionManager.OnRequestOpenUrl
인터스티셜 광고 노출 상태에서 사용자가 외부 링크를 클릭하는 경우 발생합니다.

```java
ValuePotionManager.OnRequestOpenUrl += OnRequestOpenUrlHandler;
public void OnRequestOpenUrlHandler(string placement, string url) {
	// 외부 링크를 열 때 필요한 작업이 있다면 여기에 구현합니다.
	// 앱이 Background로 진입하게 되므로, 사용자 데이터를 저장하는 등의 처리를 할 수 있습니다.
}
```

#### ValuePotionManager.OnRequestPurchase
IAP 타입의 인터스티셜 광고 노출 상태에서 사용자가 '결제하기'를 선택하는 경우 발생합니다.

```java
ValuePotionManager.OnRequestPurchase += OnRequestPurchaseHandler;
public void OnRequestPurchaseHandler(string placement, string name, string productId, int quantity, string campaignId, string contentId) {
	// 인자로 전달된 productId, quantity 정보를 가지고 실제 결제를 진행하도록 구현합니다.
	// 결제가 완료된 이후 ValuePotionManager.TrackPurchaseEvent() 메소드를 사용해
	// 결제 이벤트를 전송하면 매출 리포트가 집계됩니다.
}
```

#### ValuePotionManager.OnRequestReward
Reward 타입의 인터스티셜 광고가 노출될 때 발생합니다.

```java
ValuePotionManager.OnRequestReward += OnRequestRewardHandler;
public void OnRequestRewardHandler(string placement, Dictionary<string, object>[] rewards) {
	// rewards 배열에는 해당 광고를 통해 사용자에게 지급하고자 하는 리워드 정보들이 담겨있습니다.
	// 이 정보들을 가지고 사용자에게 리워드를 지급하는 코드를 구현합니다.
	for (int i = 0; i < rewards.Length; i++) {
		// 지급할 리워드 아이템의 이름과 수량.
		Debug.Log(rewards[i]["name"] + ", " + rewards[i]["quantity"].ToString());
	}
}
```

## 기타 설정
### 1. Push Notification LED 설정 (for Android)
스마트폰의 화면이 꺼져있는 상태에서 Push 메시지를 받은 경우, LED가 점멸하도록 해 푸시 메시지가 수신된 것을 유저에게 알릴 수 있습니다.
```java
  // 푸시 메시지 수신 시 램프가 0.5초 간격으로 붉은색으로 점멸합니다.
  ValuePotionManager.SetNotificationLights(0xff0000, 500, 500);
```
위와 같이 사용하면 해당 설정이 SharedPreference에 저장되어 계속 사용됩니다. 만약 설정을 되돌리고 싶다면, 위 구문을 제거하고 앱을 삭제한 후 다시 설치하십시오.

### 2. Push Notification 진동 설정 (for Android)
Push 메시지 수신 시 울리는 진동의 패턴을 커스터마이징 할 수 있습니다.
```java
  ValuePotionManager.SetNotificationVibrate(new long[] {0, 300, 500, 1000, 500, 400, 100});
```
위와 같이 사용하면 해당 설정이 SharedPreference에 저장되어 계속 사용됩니다. 만약 설정을 되돌리고 싶다면, 위 구문을 제거하고 앱을 삭제한 후 다시 설치하십시오. 진동 패턴에 대한 자세한 정보는 [안드로이드 SDK 공식 문서](http://developer.android.com/reference/android/os/Vibrator.html)를 참조하십시오.

진동 설정을 커스터마이징 하기 위해서는 AndroidManifest.xml 파일에 아래와 같이 퍼미션을 추가해야 합니다.
```xml
  <uses-permission android:name="android.permission.VIBRATE" />
```
