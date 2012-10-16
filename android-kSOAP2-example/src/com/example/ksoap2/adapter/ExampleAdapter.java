package com.example.ksoap2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ksoap2.R;

/**
 * Custom {@link ArrayAdapter} to display examples in a {@link ListView}
 * 
 * @author Thira
 */
public class ExampleAdapter extends ArrayAdapter<ExampleAdapter.ExampleAdapterView> {
    private LayoutInflater inflater;
    
    public ExampleAdapter(Context context, List<ExampleAdapter.ExampleAdapterView> examples) {
        super(context, R.layout.list_item_soap_example, examples);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_soap_example, null);
        }

        final ExampleAdapterView exampleView = getItem(position);
        
        ((TextView) convertView.findViewById(R.id.lblHeader)).setText(exampleView.headerTextId);
        ((TextView) convertView.findViewById(R.id.lblDescription)).setText(exampleView.descriptionTextId);
        ((Button) convertView.findViewById(R.id.btnTryExample)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                exampleView.onClickListener.onClick(v);
            }
        });
        
        return convertView;
    }

    public static class ExampleAdapterView {
        private final int headerTextId;
        private final int descriptionTextId;
        private final OnClickListener onClickListener;
        
        public ExampleAdapterView(int headerTextId, int descriptionTextId, OnClickListener onClickListener) {
            this.headerTextId = headerTextId;
            this.descriptionTextId = descriptionTextId;
            this.onClickListener = onClickListener;
        }
    }
}
