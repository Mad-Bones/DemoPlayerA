package com.bsn.DemoPlayerA.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesAdapter

{
    private static final String SESSION_NAME = "com.bns.DemoPlayerA";
    private final SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    @SuppressLint("CommitPrefEdits")
    public PreferencesAdapter(Context context)
    {
        mSharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        prefsEditor = mSharedPreferences.edit();
    }

    public int trackNumber() {return mSharedPreferences.getInt("trackNumber", 0);}
    public void setTrackNumber(int trackNumber) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putInt("trackNumber", trackNumber);
        prefsEditor.apply();
    }

    public boolean isPlaying() {return mSharedPreferences.getBoolean("isPlaying", false);}
    public void setisPlaying(boolean isPlaying) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putBoolean("isPlaying", isPlaying);
        prefsEditor.apply();
    }

}
