-dontwarn **

-keepclassmembers class ** {
    void <init>(android.content.Context);
}

-keep class com.rhul.fyp.asecav.maxlock.hooks.** {*;}

-keep class com.rhul.fyp.asecav.maxlock.ui.actions.ActionsHelper {
    public static void clearImod();
}

-keepclassmembers class ** {
    void onAuthenticationSucceeded();
}

-keepclassmembers class  com.haibison.android.lockpattern.widget.LockPatternView {
    private int mRegularColor;
    private int mSuccessColor;
}

-assumevalues class android.os.Build$VERSION {
    int SDK_INT return 21..2147483647;
}

-assumevalues class com.rhul.fyp.asecav.maxlock.BuildConfig {
    boolean DEBUG return false;
}