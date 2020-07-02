package com.valhalla.studiac.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * A singleton class used to access the android's share preferences
 * usage:
 * to get an instance: SharedPreference pref = SharedPreference.getInstance(context);
 * to save something: pref.saveData(YOUR_KEY,YOUR_VALUE);
 * to retrieve something: String value = pref.getData(YOUR_KEY);
 * currently supports: strings, booleans
 */
public class SharedPreference {
    private static SharedPreference customPreference;
    private SharedPreferences sharedPreferences;
    private final String TAG = "SharedPreference";

    public static SharedPreference getInstance(Context context) {
        if (customPreference == null) {
            customPreference = new SharedPreference(context);
        }
        return customPreference;
    }

    private SharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(Common.PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public void saveData(String dataType, String key, Object value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        if (dataType.equals(Boolean.class.getSimpleName())) {
            prefsEditor.putBoolean(key, (Boolean) value);
            Log.d(TAG, "saving bool to shared pref");

        } else if (dataType.equals(String.class.getSimpleName())) {
            prefsEditor.putString(key, (String) value);
        }


        prefsEditor.apply();
    }

    /**
     * @param key the key to retrieve value
     * @return string returns true/null if nothing found or no instances made, else returns the value
     */
    public Object getData(String dataType, String key) {
        if (sharedPreferences != null) {
            if (dataType.equals(Boolean.class.getSimpleName())) {
                return sharedPreferences.getBoolean(key, true);

            } else if (dataType.equals(String.class.getSimpleName())) {
                return sharedPreferences.getString(key, null);
            }
        }
        return null;
    }
}
