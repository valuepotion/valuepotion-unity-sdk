# Change Log

## v1.0.18
* Built with valuepotion-android-sdk-1.0.17 and valuepotion-ios-sdk-1.0.2

#### Android
* Fixed a bug app crashing when users' android device does not have Google Play Services.

## v1.0.17
* Built with valuepotion-android-sdk-1.0.16 and valuepotion-ios-sdk-1.0.2

#### iOS
* iOS 8 support.

## v1.0.16
* Built with valuepotion-android-sdk-1.0.16 and valuepotion-ios-sdk-1.0.1

#### Android
* Fixed a bug that WebView doesn’t work properly after showing interstitial ad.
* Fixed a bug that the unity binder delivers wrong event strings.

## v1.0.15
* Built with valuepotion-android-sdk-1.0.15 and valuepotion-ios-sdk-1.0.1
* Renamed “location” to “placement” from all variables and method names.

##### Android
* Use Advertising Id ( https://play.google.com/intl/en/about/developer-content-policy.html )

##### iOS
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
