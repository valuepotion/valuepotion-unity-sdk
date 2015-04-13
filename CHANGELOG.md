# Change Log

## v1.0.30
* Built with valuepotion-android-sdk-1.0.26 and valuepotion-ios-sdk-1.0.7
* Minor bug fixes

## v1.0.29
* Built with valuepotion-android-sdk-1.0.25 and valuepotion-ios-sdk-1.0.7
* Minor bug fixes
* Improved stability

### Android
* If the following code is already in your AndroidManifest.xml,

 ```xml
        <receiver
            android:name="com.valuepotion.sdk.push.GcmBroadcastReceiver">
            <intent-filter>
                <action android:name="com.valuepotion.sdk.push.NOTIFICATION_OPENED" />
            </intent-filter>
        </receiver>
 ```
 then replace it with the code below:
 ```xml
        <receiver
            android:name="com.valuepotion.sdk.push.NotificationOpenedReceiver">
            <intent-filter>
                <action android:name="com.valuepotion.sdk.push.NOTIFICATION_OPENED" />
            </intent-filter>
        </receiver>
 ```

## v1.0.28
* Built with valuepotion-android-sdk-1.0.24 and valuepotion-ios-sdk-1.0.6
* You can now specify category and label for payment events.
```
ValuePotionManager.TrackPurchaseEvent(category, eventName, label, amount, currency, orderId, productId, campaignId, contentId);
```

## v1.0.27
* Built with valuepotion-android-sdk-1.0.23 and valuepotion-ios-sdk-1.0.5

### iOS
* Improved stability of interstitial view.
* Fixed a bug in campaign blocking function.

## v1.0.26
* Built with valuepotion-android-sdk-1.0.23 and valuepotion-ios-sdk-1.0.4

### Android
* Registering gcm push token has been stabilized.
* Improved performance of tracking custom events.

## v1.0.25
* Built with valuepotion-android-sdk-1.0.22 and valuepotion-ios-sdk-1.0.4

### Android
* Fixed a bug regarding push open.

## v1.0.24
* Built with valuepotion-android-sdk-1.0.21 and valuepotion-ios-sdk-1.0.4
* Improve session measuring accuracy for Android app.
* Additional configuration guide added. Please check [`onStart / onStop`](https://github.com/valuepotion/valuepotion-unity-sdk/blob/master/README.md#onstart--onstop) section in [README](https://github.com/valuepotion/valuepotion-unity-sdk/blob/master/README.md) file.

## v1.0.23
* Built with valuepotion-android-sdk-1.0.21 and valuepotion-ios-sdk-1.0.4
* Minor bug fix regarding plugin packaging.

## v1.0.22
* Built with valuepotion-android-sdk-1.0.21 and valuepotion-ios-sdk-1.0.4

### Android
#### Upgrading Issue

* If you're upgrading SDK from older version to v1.0.22, you must add the following code into AndroidManifest.xml.
  ```java
    <application ...>
        ...

        <receiver
            android:name="com.valuepotion.sdk.push.GcmBroadcastReceiver">
            <intent-filter>
                <action android:name="com.valuepotion.sdk.push.NOTIFICATION_OPENED" />
            </intent-filter>
        </receiver>

        ...
    </application>
 ```

### iOS
* Fixed a bug that incorrect session measurements occasionally occur.

## v1.0.21
* Built with valuepotion-android-sdk-1.0.20 and valuepotion-ios-sdk-1.0.3
* New APIs
```java
  public static void TrackEvent(string eventName)
  public static void TrackEvent(string category, string eventName, string label, double eventValue)
  public static void TrackPurchaseEvent(string eventName, double revenueAmount, string currency, string orderId, string productId)
  public static void SetUserAccountType(string accountType)
```
* Deprecated APIs
```java
  public static void TrackEvent(string eventName, Dictionary<string, double> eventValues)
  public static void TrackPurchaseEvent(string eventName, double revenueAmount, string currency)
  public static void SetUserInfo(Dictionary<string, string> userInfo)
```

## v1.0.20
* Built with valuepotion-android-sdk-1.0.19 and valuepotion-ios-sdk-1.0.2
* New APIs
```java
  public static void SetNotificationLights(int argb, int onMs, int offMs)
  public static void SetNotificationVibrate(long[] pattern)
```

## v1.0.19
* Built with valuepotion-android-sdk-1.0.18 and valuepotion-ios-sdk-1.0.2

### Android
* Fixed a bug that push token is not registered very occasionally.

## v1.0.18
* Built with valuepotion-android-sdk-1.0.17 and valuepotion-ios-sdk-1.0.2

### Android
* Fixed a bug app crashing when users' android device does not have Google Play Services.

## v1.0.17
* Built with valuepotion-android-sdk-1.0.16 and valuepotion-ios-sdk-1.0.2

### iOS
* iOS 8 support.

## v1.0.16
* Built with valuepotion-android-sdk-1.0.16 and valuepotion-ios-sdk-1.0.1

### Android
* Fixed a bug that WebView doesn’t work properly after showing interstitial ad.
* Fixed a bug that the unity binder delivers wrong event strings.

## v1.0.15
* Built with valuepotion-android-sdk-1.0.15 and valuepotion-ios-sdk-1.0.1
* Renamed “location” to “placement” from all variables and method names.

#### Android
* Use Advertising Id ( https://play.google.com/intl/en/about/developer-content-policy.html )

#### iOS
* Remove unneeded device identifier.

## v1.0.10
* 로그포맷에 CheckPoint 로그를 추가하여 연동체크를 수월하도록 수정함
* push On/Off 기능추가

## v1.0.9
* 로그포맷 정리
* setUserId, setUserServerId 호출시 바로 Ping 보내게 수정.(server-sdk 이용시 필수)

## v1.0.8
* Push Popup가 여러개 나타날때 확인안한 기존 Push들은 Notification으로 남기기
* Install 이벤트 전송 Timestamp를 Session Start와 일치시키기
* 복수개의 인터스티셜 요청처리.
* Thread-Safe처리.
* 초기화 완료전에 이벤트 트래킹을 시도하는 문제 수정. 게임SDK는 완벽히 Thread-safe 해야한다.

## v1.0.7
* 2.3.3 이하 버전 지원
* push 오류시 방어코드 추가
* Unity Thread-safe 처리

## v1.0.6
* 영문 Readme 추가
* Init 시에 UserInfo를 덮어씌우는 문제 제거
* push 처리시 Runtime Exception 처리
* push 메세지 파싱시에 null 체크 추가
* 1.0.5 의 버그 수정
* Country 가 전송되지 않던 문제 수정
* Push Open시 campaignId 가 전달되지 않던 문제 수정
* 2.3.3 이하에서 VPHttpClient 의 분기문이 작동하지 않아 crash되던 문제 수정

## v1.0.5
* UserInfo 를 인터페이스에서 채우는 메소드 추가
* setUserLevel : 레벨
* setUserId : 사용자 식별자
* setUserServerId : 사용자 식별자2
* setUserGender : 성별 M 또는 F
* setUserBirth : 생년월일
* setUserFriends : 친구의 수
* OnStart, OnStop 을 ValuePotionManager.cs 에 추가
* Android Activity 통합없이 강제로 작동시킬 수 있는 기능
* 임시로만 사용하여야 하며, 반드시 Activity통합으로 만들어야 함.

## v1.0.4
* 통합시 우선 Unity에서 작동확인을 할수 있도록 OnStart(), OnStop 추가

## v1.0.3
* Push Token을 등록시 clientId도 함께 전송하도록 수정되었습니다.

## v1.0.2
* ValuePotionManager.Destroy() 추가
* 유니티에서 앱 종료시,
* 즉시 session종료이벤트를 보내는 Destroy()를 호출하도록 해야합니다.
* ValuePotionManager.Destroy();
* Application.Quit();
