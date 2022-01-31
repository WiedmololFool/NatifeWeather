package com.max.natifeweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.max.natifeweather.model.Forecast;

import java.util.List;

public class ForecastAdapter extends ArrayAdapter<Forecast>
{
    private LayoutInflater inflater;
    private int layout;
    private List<Forecast> forecasts;

    public ForecastAdapter(@NonNull Context context, int resource, @NonNull List<Forecast> forecasts)
    {
        super(context, resource, forecasts);
        this.forecasts = forecasts;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view = inflater.inflate(this.layout, parent, false);

        TextView dateView = view.findViewById(R.id.date_list);
        ImageView forecastResourseView = view.findViewById(R.id.forecast_icon_list);
        TextView descriptionView = view.findViewById(R.id.description_field_list);
        TextView temperatureView = view.findViewById(R.id.temperature_field_list);
        TextView windSpeedView = view.findViewById(R.id.wind_speed_list);
        ImageView windResourseView = view.findViewById(R.id.wind_icon_list);
        TextView windDirectView = view.findViewById(R.id.wind_direct_list);

        Forecast forecast = forecasts.get(position);
        dateView.setText(forecast.getDate());
        forecastResourseView.setImageResource(forecast.getForecastResourse());
        descriptionView.setText(forecast.getDetails());
        temperatureView.setText(String.format("%.0f",forecast.getMaxTemp()) +" / "+String.format("%.0f",forecast.getMinTemp())+" ℃");
        windSpeedView.setText(String.format("%.1f",forecast.getWind().getWindSpeed()));
        windResourseView.setImageResource(forecast.getWind().getWindResourse());
        windDirectView.setText(forecast.getWind().getWindDirection());
        return view;
    }
}
