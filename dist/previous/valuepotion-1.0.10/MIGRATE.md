#### SDK 초기화/종료

Assets/Plugins/ValuePotion/Samples/LoadingScene.cs 파일을 참고하여 초기화합니다.

```java
void start(){
        ...
        ValuePotionManager.Initialize();
        ...
}
```

ValuePotionManager.cs 에는 OnApplicationQuit()를 정의하여 Destroy()로 세션을 종료시키고 있습니다.
```java
// Added since 1.0.10
void OnApplicationQuit() {
	    Debug.Log ("OnApplicationQuit");
	    ValuePotionManager.Destroy();
}
```

ValuePotionManager.Destroy()를 원하는 위치에서 사용하기를 원하는 경우에는,
위 코드를 ValuePotionManager.cs 에서 주석처리하고,
Application.Quit()의 호출전에 Destroy() 를 호출하여 세션의 종료를 즉시 알리도록 합니다.

```java
        ValuePotionManager.Destroy();
        Application.Quit();
```

#### Push 기능의 On/Off

```java
    public static void SetPushEnabled(bool enable)
    public static bool IsPushEnabled()
```

Push기능을 활성화 시키려면 true, 아니면 false를 넣어서 SetPushEnabled를 호출해주시면 되고,
현재의 Push 활성화 상태는 IsPushEnabled 를 통해서 true/false를 얻을 수 있습니다.

내부구현 : 실제로는 Push를 받지만, Popup이나 Notification을 띄우지 않게끔 구현되어 있습니다.
그래서 Push 프로모션의 발송대상에서 제외되지는 않습니다.

