package com.max.natifeweather.model;

import com.max.natifeweather.R;

import java.util.Date;

public class Forecast
{
    private String city;
    private String date;
    private int forecastResourse;
    private String details;
    private Double temp;
    private Double feelsLikeTemp;
    private Double maxTemp;
    private Double minTemp;
    private Wind wind;


    public Forecast(String date, int forecastResourse, String details,
                    Double maxTemp, Double minTemp, Wind wind)
    {
        this.date = date;
        this.forecastResourse = forecastResourse;
        this.details = details;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.wind = wind;

    }

    public Forecast(String city, String date, int forecastResourse, String details, Double temp,
                    Double feelsLikeTemp,
                    Double maxTemp, Double minTemp, Wind wind)
    {
        this.date = date;
        this.forecastResourse = forecastResourse;
        this.details = details;
        this.temp = temp;
        this.feelsLikeTemp = feelsLikeTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.wind = wind;
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Double getTemp()
    {
        return temp;
    }

    public void setTemp(Double temp)
    {
        this.temp = temp;
    }

    public Double getFeelsLikeTemp()
    {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(Double feelsLikeTemp)
    {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getForecastResourse()
    {
        return forecastResourse;
    }

    public void setForecastResourse(int forecastResourse)
    {
        this.forecastResourse = forecastResourse;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public Double getMaxTemp()
    {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp)
    {
        this.maxTemp = maxTemp;
    }

    public Double getMinTemp()
    {
        return minTemp;
    }

    public void setMinTemp(Double minTemp)
    {
        this.minTemp = minTemp;
    }

    public Wind getWind()
    {
        return wind;
    }

    public void setWind(Wind wind)
    {
        this.wind = wind;
    }

    public static int setForecastResourse(int actualId, long sunrise, long sunset, boolean considerTimeOfDay)
    {
        int id = actualId / 100;
        int forecastResourse = 0;
        long currentTime = new Date().getTime();
        if ((currentTime >= sunrise && currentTime < sunset) || !considerTimeOfDay)
        {
            if (actualId == 800)
            {
                forecastResourse = R.drawable.ic_white_day_bright;
            } else
            {
                switch (id)
                {
                    case 2:
                        forecastResourse = R.drawable.ic_white_day_thunder;
                        break;
                    case 3:
                        //drizzle
                        forecastResourse = R.drawable.ic_white_day_rain;
                        break;
                    case 7:
                    case 8:
                        //cloudy
                        //foggy
                        forecastResourse = R.drawable.ic_white_day_cloudy;
                        break;
                    case 6:
                    case 5:
                        //rainy
                        //snowy
                        forecastResourse = R.drawable.ic_white_day_shower;
                        break;
                }
            }
        } else
        {
            if (actualId == 800)
            {
                forecastResourse = R.drawable.ic_white_night_bright;
            } else
            {
                switch (id)
                {
                    case 2:
                        forecastResourse = R.drawable.ic_white_night_thunder;
                        break;
                    case 3:
                        //drizzle
                        forecastResourse = R.drawable.ic_white_night_rain;
                        break;
                    case 7:
                    case 8:
                        //cloudy
                        //foggy
                        forecastResourse = R.drawable.ic_white_night_cloudy;
                        break;
                    case 6:
                    case 5:
                        //rainy
                        //snowy
                        forecastResourse = R.drawable.ic_white_night_shower;
                        break;
                }
            }
        }
        return  forecastResourse;
    }
}
