package com.koiy.playercompanion;

import android.content.Context;
import android.provider.Settings;

public class utils {

    public static String getAndroidID(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }



}
