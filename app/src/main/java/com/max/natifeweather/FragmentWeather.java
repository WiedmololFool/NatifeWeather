package com.max.natifeweather;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.max.natifeweather.model.Forecast;
import com.max.natifeweather.model.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class FragmentWeather extends Fragment
{
    private String apiCurrentWeather = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=ru";
    private String apiOneCall = "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&units=metric&exclude=hourly,minutely&appid=%s&lang=ru";
    private final String OpenWeatherKey = "edf78b3bb5490e1eb7c8b9e91db07374";
    private String AerisId = "wPXyx4EKVkWVFHSFJfjqi";
    private String AerisSecret = "uVLZCrMkFRCyAtv5KF0CYKM3fKgTxym4NuxxKSuK";
    private String apiAeris = "https://api.aerisapi.com/places/search?query=name:^%s&format=json&limit=10&client_id=%s&client_secret=%s";
    private String secondAerisID = "ECOuCsvX5Obl9O84xkAD8";
    private String secondAerisSecret = "qlTxgp0pE15Ax87Xum1G2JBwgeq4yvBiTZX7U8d1";
    private ArrayList<String> citiesList = new ArrayList<>();
    private TextView waitingField, updatedField, detailsField, currentTemperatureField, minMaxTempField,
            feelsLikeField, windSpeed, windDirect;
    private ListView forecastList;
    private ImageView windIcon, reloadIcon, weatherIcon;
    AutoCompleteTextView searchField;
    private ArrayList<Forecast> forecasts;
    private ForecastAdapter forecastAdapter;
    ArrayAdapter<String> autoCompleteTvAdapter;
    private ScrollView mainContent;
    private View toolBar;

    public FragmentWeather()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        Log.d("MyLog", "onCreateView");
        updatedField = view.findViewById(R.id.updated_field);
        detailsField = view.findViewById(R.id.details_field);
        currentTemperatureField = view.findViewById(R.id.current_temperature_field);
        weatherIcon = view.findViewById(R.id.weather_icon);
        minMaxTempField = view.findViewById(R.id.minmax_temp_field);
        feelsLikeField = view.findViewById(R.id.feels_like_field);
        windSpeed = view.findViewById(R.id.wind_speed);
        windIcon = view.findViewById(R.id.wind_icon);
        windDirect = view.findViewById(R.id.wind_direct);
        forecastList = (ListView) view.findViewById(R.id.forecast_list);
        waitingField = view.findViewById(R.id.waiting_field);
        toolBar = view.findViewById(R.id.toolbar);
        searchField = toolBar.findViewById(R.id.search_field);
        reloadIcon = toolBar.findViewById(R.id.reload_icon);
        mainContent = view.findViewById(R.id.main_content);

        CityPreferences cityPreferences = new CityPreferences(getActivity());

        forecasts = new ArrayList<>();


        new GetWeather().execute(cityPreferences.getCity());

        forecastAdapter = new ForecastAdapter(getContext(), R.layout.list_forecast, forecasts);
        forecastList.setAdapter(forecastAdapter);
        ListViewHelper.getListViewSize(forecastList);

        reloadIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(), R.string.weather_update_message, Toast.LENGTH_SHORT).show();
                new GetWeather().execute(cityPreferences.getCity());
            }
        });
        searchField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                new FindCity().execute(editable.toString());
            }
        });
        searchField.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    cityPreferences.setCity(searchField.getText().toString());
                    searchField.clearFocus();
                    new GetWeather().execute(cityPreferences.getCity());
                    hideKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        searchField.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId)
            {
                cityPreferences.setCity(searchField.getText().toString());
                searchField.clearFocus();
                new GetWeather().execute(cityPreferences.getCity());
                hideKeyboard(getActivity());
            }
        });
        return view;
    }

    public static void hideKeyboard(Activity activity)
    {
        View view = activity.findViewById(android.R.id.content);
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class GetWeather extends AsyncTask<String, String, JSONObject[]>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            waitingField.setText(R.string.waiting_text);
            mainContent.setVisibility(View.INVISIBLE);

        }

        @Override
        protected JSONObject[] doInBackground(String... strings)
        {
            JSONObject[] jsonArray = new JSONObject[2];
            try
            {
                URL urlCurrentWeather = new URL(String.format(apiCurrentWeather, strings[0], OpenWeatherKey));


                if (RemoteFetch.getJSON(getContext(), urlCurrentWeather) != null)
                {
                    JSONObject jsonObjectCurrentWeather = RemoteFetch.getJSON(getContext(), urlCurrentWeather);
                    jsonArray[0] = jsonObjectCurrentWeather;
                    String lon = jsonObjectCurrentWeather.getJSONObject("coord").getString("lon");
                    String lat = jsonObjectCurrentWeather.getJSONObject("coord").getString("lat");
                    Log.d("MyLog7", "lon: " + lon);
                    Log.d("MyLog7", "lat: " + lat);
                    URL urlOneCall = new URL(String.format(apiOneCall, lat, lon, OpenWeatherKey));
                    Log.d("MyLogUrlOneCall", urlOneCall.toString());
                    JSONObject jsonObjectOneCall = RemoteFetch.getJSON(getContext(), urlOneCall);

                    if (jsonObjectOneCall != null)
                    {
                        jsonArray[1] = jsonObjectOneCall;
                    }
                    return jsonArray;
                } else
                {
                    return null;
                }
            } catch (MalformedURLException | JSONException e)
            {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(JSONObject[] result)
        {
            super.onPostExecute(result);

            if (result != null)
            {
                if (result[0] != null && result[1] != null)
                {
                    JSONObject jsonObjectCurrentWeather = (JSONObject) result[0];
                    JSONObject jsonObjectOneCall = (JSONObject) result[1];
                    Log.d("MyLogCurrentWeather", String.valueOf(jsonObjectCurrentWeather));
                    Log.d("MyLogOneCallDaily", String.valueOf(jsonObjectOneCall));
                    renderCurrentForecast(jsonObjectCurrentWeather);
                    renderForecastList(jsonObjectOneCall);
                } else
                {
                    Log.d("MyLog", "Данные не получены");
                    mainContent.setVisibility(View.INVISIBLE);
                }
            } else
                waitingField.setText(R.string.data_load_fail);

        }
    }

    private class FindCity extends AsyncTask<String, String, ArrayList>
    {

        @Override
        protected ArrayList doInBackground(String... strings)
        {
            try
            {
                citiesList.clear();
                URL urlCitySearch = new URL(String.format(apiAeris, strings[0], AerisId, AerisSecret));
                Log.d("MyLogCitySearchURL", urlCitySearch.toString());
                JSONObject jsonObject = RemoteFetch.getJSON(getContext(), urlCitySearch);
                if (jsonObject != null)
                {
                    JSONArray jsonCities = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonCities.length(); i++)
                    {
                        JSONObject jsonCity = (JSONObject) jsonCities.get(i);
                        Log.d("MyLogCitySearch" + i, jsonCity.getJSONObject("place").toString());
                        citiesList.add(jsonCity.getJSONObject("place").getString("name") + ", " + jsonCity.getJSONObject("place").getString("country"));

                    }
                    return citiesList;
                } else return null;

            } catch (MalformedURLException | JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList result)
        {
            super.onPostExecute(result);
            if (result != null)
            {
                autoCompleteTvAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, result);
                searchField.setAdapter(autoCompleteTvAdapter);
                Log.d("MyLogCitySearchList", result.toString());
            }
        }
    }


    private void renderCurrentForecast(JSONObject jsonObject)
    {
        try
        {
            waitingField.setText("");
            mainContent.setVisibility(View.VISIBLE);
            JSONObject detailsJson = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject mainJson = jsonObject.getJSONObject("main");
            JSONObject windJson = jsonObject.getJSONObject("wind");
            Wind wind = new Wind(windJson.getDouble("speed"), windJson.getDouble("deg"), getContext());
            String city = jsonObject.getString("name") + ", " + jsonObject.getJSONObject("sys").getString("country");
            String description = detailsJson.getString("description").toUpperCase();
            if (description.contains(" "))
            {
                int countOfSpaces = description.length() - description.replaceAll(" ", "").length();
                if (countOfSpaces > 1)
                    description = description.replaceFirst(" ", "\n");
            }

            String details = description +
                    "\n" + getString(R.string.humidity) + " " + mainJson.getString("humidity") + "%" +
                    "\n" + getString(R.string.pressure) + " " + mainJson.getString("pressure") + " " + getString(R.string.pressure_units);


            SimpleDateFormat df = new SimpleDateFormat("E, dd MMM H:mm", new Locale("ru"));
            String updatedOn = df.format(new Date(jsonObject.getLong("dt") * 1000));
            Double temp = mainJson.getDouble("temp");
            Double minTemp = mainJson.getDouble("temp_min");
            Double maxTemp = mainJson.getDouble("temp_max");
            Double feelsLikeTemp = mainJson.getDouble("feels_like");
            Forecast currentForecast = new Forecast(city, updatedOn, Forecast.setForecastResourse(detailsJson.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000, true), details,
                    temp, feelsLikeTemp, maxTemp, minTemp, wind);

            searchField.setText(currentForecast.getCity());
            updatedField.setText(getString(R.string.last_update) + " " + currentForecast.getDate());
            detailsField.setText(currentForecast.getDetails());
            currentTemperatureField.setText(String.format("%.0f", currentForecast.getTemp()) + " ℃");
            minMaxTempField.setText(String.format(getString(R.string.max_temp) + " %.0f", currentForecast.getMaxTemp()) + " °" +
                    String.format(" · " + getString(R.string.min_temp) + " %.0f", currentForecast.getMinTemp()) + " °");
            feelsLikeField.setText(String.format(getString(R.string.feels_like) + " %.0f", currentForecast.getFeelsLikeTemp()) + " °");
            Log.d("MyLog", "Wind speed: " + windJson.getDouble("speed") + ", wind deg: " + windJson.getDouble("deg"));

            weatherIcon.setImageResource(currentForecast.getForecastResourse());
            Log.d("MyLogWindRes", String.valueOf(currentForecast.getWind().getWindSpeed()));
            windIcon.setImageResource(currentForecast.getWind().getWindResourse());
            windDirect.setText(currentForecast.getWind().getWindDirection());
            windSpeed.setText(String.format("%.1f", currentForecast.getWind().getWindSpeed()));

        } catch (Exception e)
        {
            Log.e("ErrorLog", "One or more fields not found in the JSON CurrentWeather data");
        }

    }

    private void renderForecastList(JSONObject jsonObject)
    {
        forecasts.clear();
        try
        {
            JSONArray daily = jsonObject.getJSONArray("daily");
            SimpleDateFormat df = new SimpleDateFormat("E,\ndd MMM", new Locale("ru"));
            for (int i = 0; i < 8; i++)
            {
                JSONObject day = (JSONObject) daily.get(i);
                String date = df.format(new Date(day.getLong("dt") * 1000));

                JSONObject detailsJson = (JSONObject) day.getJSONArray("weather").get(0);
                JSONObject temp = day.getJSONObject("temp");
                Double maxTemp = temp.getDouble("max");
                Double minTemp = temp.getDouble("min");
                String description = detailsJson.getString("description");
                if (description.contains(" "))
                {
                    description = description.replaceFirst(" ", "\n");
                }


                Double windSpeed = day.getDouble("wind_speed");
                Double windDeg = day.getDouble("wind_deg");
                Log.d("MyLogDay" + i, date + " " + detailsJson.getInt("id") +
                        " " + day.getLong("sunrise") * 1000 + " " + day.getLong("sunset") * 1000 + " " +
                        description + " " + maxTemp + " / " + minTemp + " wind speed:" + windSpeed + " wind deg: " + windDeg + "\n"
                        + detailsJson.toString());


                Wind wind = new Wind(windSpeed, windDeg, getContext());
                Log.d("MyLogWind", String.valueOf(wind.getWindDirection()));
                switch (wind.getWindResourse())
                {
                    case R.drawable.ic_icon_wind_n:
                        wind.setWindDirection(getString(R.string.wind_dir_n_short));
                        break;
                    case R.drawable.ic_icon_wind_ne:
                        wind.setWindDirection(getString(R.string.wind_dir_ne_short));
                        break;
                    case R.drawable.ic_icon_wind_e:
                        wind.setWindDirection(getString(R.string.win_dir_e_short));
                        break;
                    case R.drawable.ic_icon_wind_se:
                        wind.setWindDirection(getString(R.string.win_dir_se_short));
                        break;
                    case R.drawable.ic_icon_wind_s:
                        wind.setWindDirection(getString(R.string.wind_dir_s_short));
                        break;
                    case R.drawable.ic_icon_wind_sw:
                        wind.setWindDirection(getString(R.string.wind_dir_sw_short));
                        break;
                    case R.drawable.ic_icon_wind_w:
                        wind.setWindDirection(getString(R.string.wind_dir_w_short));
                        break;
                    case R.drawable.ic_icon_wind_nw:
                        wind.setWindDirection(getString(R.string.wind_dir_nw_short));
                        break;
                    default:
                        wind.setWindDirection("");
                        break;
                }

                Forecast dayForecast = new Forecast(date, Forecast.setForecastResourse(detailsJson.getInt("id"),
                        day.getLong("sunrise") * 1000,
                        day.getLong("sunset") * 1000, false), description,
                        maxTemp, minTemp, wind);
                forecasts.add(dayForecast);

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("ErrorLog", "One or more fields not found in the JSON OneCall data");
        }
        forecastAdapter.notifyDataSetChanged();
        ListViewHelper.getListViewSize(forecastList);
    }
}