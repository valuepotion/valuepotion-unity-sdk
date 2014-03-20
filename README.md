
## SDK 설치

#### 프로젝트 요구 사항

* Unity4

#### 구성

ValuePotion Unity SDK는 다음과 같이 구성되어 있습니다.

```
ValuePotionUnity.unitypackage
|_ PlugIns
   |_ Android
   |  |_ libs
   |  |  |_ android-support-v4.jar
   |  |  |_ gcm.jar
   |  |  |_ unitygcmplugin.jar
   |  |  |_ valuepotion-X.X.X.jar
   |  |  |_ valuepotionunity-X.X.X.jar
   |  |_ README.md
   |
   |_ ValuePotion
      |_ Scripts
      |  |_ ValuePotionManager.cs
      |_ Samples
      |  |_ LoadingScene.cs
      |_ LitJson.dll

```

#### 통합 및 설정

##### SDK 파일 복사

###### 1. ValuePotionUnity.unitypackage 를 import 합니다.

![스크린샷](./images/1_import.png?raw=true =300x)

![스크린샷](./images/2_select_package.png?raw=true =300x)

![스크린샷](./images/3_import_result.png?raw=true =300x)

###### 2. Scene에 GameObject 삽입

![스크린샷](./images/4_create_gameobject.png?raw=true =300x)

![스크린샷](./images/5_result_create.png?raw=true =300x)

##### 4. GameObject 를 ValuePotionManager 로 변경
![스크린샷](./images/6_rename_object.png?raw=true =300x)

##### 5. ValuePotionManager 의 Script 연결
![스크린샷](./images/7_link_script.png?raw=true =300x)

##### 6. 모바일 OS별 Client Key 와 Secret Key 의 설정*
![스크린샷](./images/8_fill_info.png?raw=true =300x)

##### 7. ValuePotionManager를 Project Asset 창으로 끌어서 Prefab 으로 생성하여 Scene별로 재사용
![스크린샷](./images/9_make_prefab.png?raw=true =300x)

## 기본 연동

#### Target OS별 연동

ValuePotion SDK는 현재 Android와 iOS를 지원합니다.

###### Android 빌드 세팅

- PlugIns/Android/README.md 파일의 Activity 항목들과 Permission을 본 프로젝트의 AndroidManifest.xml 에 복사합니다.
- VPUnityActivity 를 바로 사용 또는 상속하거나,
- 기존 Acitivity 의 onStart(), onStop(), onActivityResult() 를 오버라이드 하여 아래와 같이 설정해줍니다.

```xml
    @Override
    protected void onStart() {
        VPUnityBinder.gInstance.onStart();
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        VPUnityBinder.gInstance.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( VPUnityBinder.gInstance.onActivityResult(requestCode, resultCode, data) ){
            // 밸류포션에서 발송한 push메세지를 처리한 상태
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
```

자세한 연동가이드는 PlugIns/Android/README.md 를 참고하시기 바랍니다.



###### iOS 빌드 세팅

Unity 프로젝트에서 Xcode 프로젝트로 export한 이후에는, 정상적으로 앱을 빌드하기 위해 추가 빌드 세팅이 필요합니다.
다음과 같이 Build Settings > Other Linker Flags 항목에서 \-ObjC 플래그를 추가합니다.
![스크린샷](./images/10_ios_build_setting.png?raw=true =920x)

#### SDK 초기화/종료

Assets/Plugins/ValuePotion/Samples/LoadingScene.cs 파일을 참고하여 초기화합니다.

```java
void start(){
        ...
        ValuePotionManager.Initialize();
        ...
}
```


종료시에는 Destroy() 를 호출하여 세션의 종료를 즉시 알리도록 합니다.

```java
        ValuePotionManager.Destroy();
        Application.Quit();
```

여기까지만 설정하셔도 기본적인 세션 트래킹과 install / update 이벤트 트래킹이 가능합니다.

#### 사용자 추가 정보 설정

사용자의 추가 정보에 대한 수집이 가능합니다. 해당 정보를 이용해 유저 코호트를 생성하여 활용할 수 있습니다.
현재는 사용자의 계정 id, 서버 id, level, 성별, 생년월일, 친구수 정보를 추가로 설정하실 수 있습니다.
사용자 추가 정보는 게임의 진행에 따라 변경될 때마다 새로이 설정하여 주시면 됩니다.

사용자 추가 정보는 아래의 두가지 방식으로 설정이 가능합니다.

```java
ValuePotionManager.SetUserId("astrokil@gmail.com");
ValuePotionManager.SetUserServerId("server1");
ValuePotionManager.SetUserLevel(10);
ValuePotionManager.SetUserGender("M");
ValuePotionManager.SetUserBirth("19840000");
ValuePotionManager.SetUserFriends(12);
```

```java
Dictionary<string, string> userInfo = new Dictionary<string, string>();
userInfo.Add("userId", "astrokil@gmail.com");
userInfo.Add("serverId", "server1");
userInfo.Add("level", "10");
userInfo.Add("gender", "M");
userInfo.Add("birth", "19840000");
userInfo.Add("friends", "12");
ValuePotionManager.SetUserInfo(userInfo);
```


##### 추가 정보 항목

이름           | 설명
-------------- | ------------
userId         | 게임 내에서 사용되는 사용자의 계정 id를 설정합니다. 반드시 세팅되어야 합니다. (required)
serverId       | 게임 유저를 서버 별로 식별해야 하는 경우 유저가 속한 서버의 id를 설정합니다. (optional)
               | serverId를 기준으로 서버별 통계를 확인할 수 있습니다.
birth          | 사용자의 생년월일 8자리를 문자열로 세팅합니다. (optional)
               | 연도 정보만 아는 경우 "19840000"과 같이 생일 4자리를 0으로 채웁니다.
               | 생일 정보만 아는 경우 "00001109"와 같이 연도 4자리를 0으로 채웁니다.
gender         | 남성인 경우 "M", 여성인 경우 "F" 문자열로 할당합니다. (optional)
level          | 사용자의 게임 level을 설정합니다.(optional)
friends        | 사용자의 친구수를 설정합니다.(optional)

## 캠페인 연동

[valuepotion.com](http://valuepotion.com)에서 생성한 캠페인을 interstitial 형태로 앱에 노출시킬 수 있습니다.
모든 interstitial은 로케이션 단위로 관리되며, 로케이션 이름은 정해진 규칙 없이 자유롭게 할당하실 수 있습니다.

#### interstitial 캐싱하기

앱 실행 초기에 미리 캠페인 데이터를 받아 캐싱하고, 이후 필요한 시점에 즉시 interstitial을 보여줄 수 있습니다.
다음의 코드는 main_menu, item_shop 이라는 2가지 로케이션에 대해 interstitial을 캐싱하는 예제입니다.

```java
ValuePotionManager.CacheInterstitial("main_menu");
ValuePotionManager.CacheInterstitial("item_shop");
```

#### interstitial 노출하기

interstitial을 화면에 보여주고자 할 때에는 OpenInterstitial() 메소드를 사용합니다.
OpenInterstitial() 메소드는 지정된 로케이션에 캐시가 존재하는 경우 해당 캐시를 가지고 즉시 화면에 보여줍니다.
만약 해당 로케이션에 캐시가 존재하지 않으면 서버와 통신하여 데이터를 가져와 화면에 노출시키게 됩니다.
다음은 main_menu 로케이션에서 interstitial을 띄우는 예제입니다.

```java
ValuePotionManager.OpenInterstitial("main_menu");
```

#### 캐시가 있을 때만 interstitial 노출하기

interstitial 데이터를 필요 시마다 서버로부터 받아와서 화면에 보여주게 되면 다소 딜레이가 발생할 수 있습니다.
따라서 앱 실행 초기에 모든 로케이션에 대해 캐싱해 두고, 캐시가 존재하는 로케이션에 대해서만 interstitial을 노출하고 싶을 수도 있습니다.
다음은 item_shop 로케이션에 캐시가 존재하는 경우에만 interstitial을 노출하도록 처리한 예제입니다.

```java
if ( ValuePotionManager.HasInterstitial("item_shop") ) {
    ValuePotionManager.OpenInterstitial("item_shop");
}
```

#### Delegate 메소드

ValuePotionDelegate 프로토콜에는 캠페인 연동 시 활용 가능한 delegate 메소드들이 정의되어 있습니다.
모든 delegate 메소드는 optional이므로, 필요하지 않은 경우 구현할 필요가 없습니다.

##### 캐싱 관련 delegate

interstitial의 캐싱 성공 / 실패에 대한 delegate 처리를 할 수 있습니다.

```java
public void OnCacheInterstitialHandler(string location) {
    // interstitial 캐싱이 완료되었을 때 필요한 작업을 추가합니다.
}
public void OnFailToCacheInterstitialHandler(string location, string errorMessage) {
    // interstitial 캐싱에 실패했을 때 필요한 작업을 추가합니다.
}

ValuePotionManager.OnCacheInterstitial += OnCacheInterstitialHandler;
ValuePotionManager.OnFailToCacheInterstitial += OnFailToCacheInterstitialHandler;
```

##### 노출 관련 delegate

interstitial의 노출 성공 / 실패 / 종료에 대한 delegate 처리를 할 수 있습니다.
이후에 연결되는 작업이 있을시 이 세가지를 모두 구현하여야 합니다.

```java
public void OnOpenInterstitialHandler(string location) {
    // interstitial이 노출될 때 필요한 작업을 추가합니다.
    // interstitial 뷰는 modal 상태로 열리므로, 게임이 실행중인 경우라면 pause 시키는 등의 처리를 할 수 있습니다.
    if( location == "SOME_LOCATION" ){
        // codes to continue
    }
}
public void OnFailToOpenInterstitialHandler(string location, string errorMessage) {
    // interstitial 노출에 실패했을 때 필요한 작업을 추가합니다.
    if( location == "SOME_LOCATION" ){
        // codes to continue
    }
}
public void OnCloseInterstitialHandler(string location) {
    // interstitial이 닫힐 때 필요한 작업을 추가합니다.
    // 만약 interstitial이 열릴 때 게임을 pause 시켰다면, 여기서 resume 시키도록 하면 됩니다.
    if( location == "SOME_LOCATION" ){
        // codes to continue
    }
}

ValuePotionManager.OnOpenInterstitial += OnOpenInterstitialHandler;
ValuePotionManager.OnFailToOpenInterstitial += OnFailToOpenInterstitialHandler;
ValuePotionManager.OnCloseInterstitial += OnCloseInterstitialHandler;
```

##### 액션 관련 delegate

사용자가 interstitial 내부에서 발생시킨 액션에 대한 delegate 처리를 할 수 있습니다.

```java
public void OnRequestOpenUrlHandler(string location, string url) {
    // interstitial에서 외부 링크에 대한 클릭이 발생했을 때 호출됩니다.
    // 일반적으로 외부 링크를 클릭하면 현재 앱은 background로 들어가게 되므로, 이 경우 필요한 처리를 여기서 구현합니다.
}
public void OnRequestPurchaseHandler(string location, string name, string productId, int quantity, string campaignId, string contentId) {
    // In App Purchase 캠페인의 interstitial 내부에서 사용자가 구매하기를 선택했을 때 호출됩니다.
    // 인자로 받은 productId, quantity 정보를 가지고 실제 결제 진행을 위한 코드를 실행하면 됩니다.
    // 결제 완료 이후 TrackPurchaseEvent() 메소드를 통해 결제 이벤트를 전송하면 매출 리포트 집계가 가능합니다.
}
public void OnRequestRewardsHandler(string location, Dictionary<string, object>[] rewards) {
    // 리워드 캠페인의 interstitial이 화면에 보여질 때 호출됩니다.
    // rewards 배열에는 사용자에게 지급할 리워드 정보들이 담겨있습니다.
    // 각 리워드 정보는 Dictionary<string, object> 타입의 객체이며, name, quantity 키를 가지고 있습니다.
    // 이 정보를 가지고 사용자에게 리워드 아이템을 사용자에게 지급하는 코드를 여기에서 구현합니다.
    for (int i = 0; i < rewards.Length; i++) {
        Debug.Log(rewards[i]["name"] + ", " + rewards[i]["quantity"].ToString());    // 지급할 리워드 아이템의 이름과 수량.
    }
}

ValuePotionManager.OnRequestOpenUrl += OnRequestOpenUrlHandler;
ValuePotionManager.OnRequestPurchase += OnRequestPurchaseHandler;
ValuePotionManager.OnRequestReward += OnRequestRewardHandler;
```

## 커스텀 이벤트 연동

커스텀 이벤트 전송 기능을 통해 앱에 대한 보다 세밀한 분석이 가능합니다.
또한, 커스텀 이벤트를 활용해 유저 코호트를 생성할 수도 있습니다.
커스텀 이벤트는 크게 비결제 이벤트와 결제 이벤트로 나뉩니다.

#### 비결제 이벤트 전송하기

비결제 이벤트는 게임 내 결제와 무관한 이벤트로, 이벤트 이름과 값을 인자로 받습니다.
이벤트 이름은 자유롭게 선언하여 사용하면 되며, 값은 double 또는 Dictionary<string, double> 타입의 객체만 사용 가능합니다.
다음은 비결제 이벤트를 전송하는 예제입니다.

```java
// 하나의 값을 가지는 이벤트
ValuePotionManager.TrackEvent("stage_clear", 3);

// 여러개의 값을 가지는 이벤트
Dictionary<string, double> eventInfo = new Dictionary<string, double>();
eventInfo.Add("level", 30);
eventInfo.Add("play_time", 67.71);
ValuePotionManager.TrackEvent("play_end", eventInfo);
```

특별한 값이 존재하지 않는 이벤트인 경우 이벤트 이름만 넘기면 됩니다.

```java
ValuePotionManager.TrackEvent("test_event2");
```

#### 결제 이벤트 전송하기

결제 이벤트는 게임 내 구매(In App Billing / In App Purchase)가 발생했을 때 사용되는 이벤트입니다.
결제 이벤트를 전송하기 위해서는 TrackPurchaseEvent() Method를 사용합니다.
기본적으로 eventName 과 revenueAmount, currency 의 3가지 인자(Arguments)가 필요한 Method와
IAP 캠페인을 통한 트래킹일 경우에 사용하는 productId, campaignId, contentId의 3개의 인자를 추가로 전송하는 6개 인자 Method가 있습니다.
결제 이벤트를 전송하면 앱 별 매출 리포트 집계가 가능합니다.
다음은 결제 이벤트를 전송하는 예제입니다.

```java
ValuePotionManager.TrackPurchaseEvent("gold_purchase", 0.99, "USD");
ValuePotionManager.TrackPurchaseEvent("gold_purchase", 2500, "KRW");
```

IAP 캠페인의 interstitial로부터 구매가 발생한 경우, 추가 인자를 전달하면 더욱 상세한 리포트를 확인할 수 있습니다. (아이템/캠페인/소재 별 매출 리포트)
추가 인자는 총 3가지로, productId(구매 아이템의 id), campaignId(IAP 캠페인의 id), contentId(IAP 캠페인 소재의 id)입니다.
productId, campaignId, contentId 값은 "액션 관련 delegate" 항목의 OnRequestPurchase() 델리게이트 메소드를 통해 전달받게 됩니다.

```java
string lastProductId;
string lastCampaignId;
string lastContentId;
public void OnRequestPurchaseHandler(string location, string name, string productId, int quantity, string campaignId, string contentId) {
    lastProductId = productId;
    lastCampaignId = campaignId;
    lastContentId = contentId;

    // start purchase process here
}
...

//after purchase complete
ValuePotionManager.TrackPurchaseEvent("wing_purchase", 1.99, "USD", lastProductId, lastCampaignId, lastContentId);
```

만약 IAP 캠페인을 거치지 않은 경우에 productId 별로 데이터를 보내고 싶을때에는
아래와 같이 productId를 직접 전달하면 됩니다.
```java
ValuePotionManager.TrackPurchaseEvent("wing_purchase", 1.99, "USD", "item_02_wing_2ea", null, null);
```



## Push Notification 연동

Push Notification API를 연동하면, 손쉽게 Push 타입의 캠페인을 생성하여 사용자에게 메시지를 전송할 수 있습니다.
또한, 사용자 Push 메시지를 클릭하여 게임을 실행한 경우 특정 캠페인의 interstitial을 노출시키도록 하는 것도 가능합니다.

#### Android GCM

##### 간편연동

README.md 에 있는 GCM 관련 항목들도 함께 AndroidManifest.xml 에 넣습니다.

```xml
    <permission android:name="PACKAGE_NAME.permission.C2D_MESSAGE" android:protectionLevel="signature" />
    <uses-permission android:name="PACKAGE_NAME.permission.C2D_MESSAGE" />
```

```xml
  <receiver android:name="com.kskkbys.unitygcmplugin.UnityGCMBroadcastReceiver" android:permission="com.google.android.c2dm.permission.SEND"
  android:exported="true" 
    >
      <intent-filter>
          <action android:name="com.google.android.c2dm.intent.RECEIVE" />
          <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
          <category android:name="PACKAGE_NAME" />
      </intent-filter>
  </receiver>
  <service android:name="com.kskkbys.unitygcmplugin.UnityGCMIntentService" />
```

##### 수동연동

이미 GCM연동이 되어있는 경우에는
안드로이드 플러그인 프로젝트에서 valuepotion.jar 와 valuepotionunity.jar를 클래스패스에 추가한 뒤,

README.md 에 있는 GCM 관련 항목들은 AndroidManifest.xml 에 넣지 않습니다.

1. GCMIntentService 클래스에서 다음과 같이 구현합니다.

```java
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null ){
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                if( VPUnityBinder.gInstance.treatPushMessage(this, extras) ){
                  // 밸류포션에서 발송한 GCM 처리성공
                }
                else{
                    // 그외의 경우를 여기서 처리하시면 됩니다.
                }

            }
        }
    }
```

2. 다음과 같이 regID를 얻는 시점에 밸류포션으로 regID를 전송하도록 합니다.

```java
  String regid = gcm.register(GCMIntentService.PROJECT_ID);
  VPUnityBinder.gInstance.registerPushToken(regid);
```


#### iOS

Unity 프로젝트를 Xcode 프로젝트로 export 한 후, UnityAppController.mm 파일을 열어 다음과 같이 추가합니다.
```objc
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    ...

    [[ValuePotion sharedInstance] registerForPushNotification];      // SDK에서 Push Notification을 사용하도록 설정
    [[ValuePotion sharedInstance] forwardPushInfo:launchOptions];    // Push 메시지를 클릭하여 앱이 실행된 경우 해당 정보를 SDK에 전달

    return YES;
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [[ValuePotion sharedInstance] forwardPushInfo:userInfo];         // Push 메시지를 받으면 해당 정보를 SDK에 전달
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    [[ValuePotion sharedInstance] registerPushToken:deviceToken];    // device token을 SDK에 등록
}
```

