package com.max.natifeweather.model;

import android.content.Context;
import android.content.res.Resources;

import com.max.natifeweather.R;

public class Wind
{
    private Double windSpeed;
    private int windResourse;
    private String windDirection;

    public Wind(Double windSpeed, int windResourse, String windDirection)
    {
        this.windSpeed = windSpeed;
        this.windResourse = windResourse;
        this.windDirection = windDirection;
    }

    public Wind(Double windSpeed, Double deg, Context context)
    {
        this.windSpeed = windSpeed;
        if (windSpeed == 0)
        {
            windDirection = context.getString(R.string.wind_calm);
        } else
        {


            if (deg == 0)
            {
                windResourse = R.drawable.ic_icon_wind_n;
                windDirection = context.getString(R.string.wind_dir_n);
            } else if (0 < deg && deg < 90)
            {
                windResourse = R.drawable.ic_icon_wind_ne;
                windDirection = context.getString(R.string.wind_dir_ne);
            } else if (deg == 90)
            {
                windResourse = R.drawable.ic_icon_wind_e;
                windDirection = context.getString(R.string.wind_dir_e);
            } else if (90 < deg && deg < 180)
            {
                windResourse = R.drawable.ic_icon_wind_se;
                windDirection = context.getString(R.string.wind_dir_se);
            }
            if (deg == 180)
            {
                windResourse = R.drawable.ic_icon_wind_s;
                windDirection = context.getString(R.string.wind_dir_s);
            } else if (180 < deg && deg < 270)
            {
                windResourse = R.drawable.ic_icon_wind_sw;
                windDirection = context.getString(R.string.wind_dir_sw);
            } else if (deg == 270)
            {
                windResourse = R.drawable.ic_icon_wind_w;
                windDirection = context.getString(R.string.wind_dir_w);
            } else if (270 < deg && deg < 360)
            {
                windResourse = R.drawable.ic_icon_wind_nw;
                windDirection = context.getString(R.string.wind_dir_nw);
            }

        }
    }

    public Double getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public int getWindResourse()
    {
        return windResourse;
    }

    public void setWindResourse(int windResourse)
    {
        this.windResourse = windResourse;
    }

    public String getWindDirection()
    {
        return windDirection;
    }

    public void setWindDirection(String windDirection)
    {
        this.windDirection = windDirection;
    }
}
