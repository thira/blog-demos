package com.example.ksoap2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ksoap2.R;
import com.example.ksoap2.wsclient.weather.bo.CityForecastBO.ForecastInfo;

/**
 * Custom {@link ArrayAdapter} to city forecast information in a {@link ListView}
 * 
 * @author Thira
 */
public class CityForecastInfoAdapter extends ArrayAdapter<ForecastInfo> {

    private LayoutInflater inflater;

    public CityForecastInfoAdapter(Context context, List<ForecastInfo> forecasts) {
        super(context, R.layout.list_item_city_forecast, forecasts);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_city_forecast, null);
        }

        ForecastInfo forecastInfo = getItem(position);

        TextView headerView = (TextView) convertView.findViewById(R.id.lblHeader);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.lblDescription);
        TextView temperatureInfoView = (TextView) convertView.findViewById(R.id.lblTemperatureInfo);
        TextView precipitationInfoView = (TextView) convertView.findViewById(R.id.lblPrecipitationInfo);

        headerView.setText(String.format(" %s (ID: %d)", forecastInfo.getDate(), forecastInfo.getWeatherId()));
        descriptionView.setText(forecastInfo.getDescription());
        temperatureInfoView.setText(String.format("Temperature: %s (morning) - %s (daytime)", forecastInfo.getTemperatureMorningLow(),
                forecastInfo.getTemperatureDaytimeHigh()));
        precipitationInfoView.setText(String.format("Preciptation %%: %s (daytime) - %s (night)",
                forecastInfo.getPrecipitationProbabilityDaytime(), forecastInfo.getPrecipitationProbabilityNightTime()));

        return convertView;
    }

}
