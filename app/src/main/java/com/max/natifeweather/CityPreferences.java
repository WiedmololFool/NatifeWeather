package com.max.natifeweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CityPreferences
{
    SharedPreferences prefs;

    CityPreferences(Activity activity)
    {
        prefs = activity.getPreferences(Context.MODE_PRIVATE);
    }

    String getCity()
    {
        return prefs.getString("city", "Kiev");
    }

    void setCity(String city)
    {
        prefs.edit().putString("city",city).apply();
    }
}
