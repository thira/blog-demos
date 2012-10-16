package com.example.ksoap2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ksoap2.wsclient.weather.bo.WeatherDescriptionBO;

/**
 * Custom {@link ArrayAdapter} to display weather information in a {@link ListView}
 * 
 * @author Thira
 */
public class WeatherDescriptionAdapter extends ArrayAdapter<WeatherDescriptionBO> {

    private LayoutInflater inflater;

    public WeatherDescriptionAdapter(Context context, List<WeatherDescriptionBO> objects) {
        super(context, android.R.layout.two_line_list_item, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.two_line_list_item, null);
        }

        WeatherDescriptionBO weatherInfo = getItem(position);

        TextView lineOneView = (TextView) convertView.findViewById(android.R.id.text1);
        TextView lineTwoView = (TextView) convertView.findViewById(android.R.id.text2);

        lineOneView.setText(String.format("[ %d ] - %s", weatherInfo.getId(), weatherInfo.getDescription()));
        lineTwoView.setText(String.format("Picture URL: %s", weatherInfo.getPictureURL()));

        return convertView;
    }

}
