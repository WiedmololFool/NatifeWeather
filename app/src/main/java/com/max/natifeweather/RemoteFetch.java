package com.max.natifeweather;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteFetch
{

    public static JSONObject getJSON(Context context, URL url)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer json = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                json.append(line).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            if (!(url.toString().contains("onecall")) && url.toString().contains("openweathermap"))
            {
                if (data.getInt("cod") != 200)
                {
                    return null;
                }
            }
            return data;
        } catch (IOException | JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }



}
