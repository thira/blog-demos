package com.example.ksoap2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ksoap2.adapter.ExampleAdapter;
import com.example.ksoap2.adapter.ExampleAdapter.ExampleAdapterView;

/**
 * {@link Activity} for displaying all the example web services associated with the WeatherService
 * 
 * @author Thira
 */
public class SimpleKSoapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_ksoap);

        // Example 1
        ExampleAdapterView example1 = new ExampleAdapterView(R.string.example_1_header, R.string.example_1_description,
                new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        startExampleOne();
                    }
                });
        // Example 2
        ExampleAdapterView example2 = new ExampleAdapterView(R.string.example_2_header, R.string.example_2_description,
                new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        startExampleTwo();
                    }
                });

        List<ExampleAdapterView> examples = new ArrayList<ExampleAdapter.ExampleAdapterView>();
        examples.add(example1);
        examples.add(example2);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ExampleAdapter(this, examples));
    }

    private void startExampleOne() {
        Intent intent = new Intent(this, SimpleKSoapExample1Activity.class);
        intent.putExtra(SimpleKSoapExample1Activity.INTENT_EXTRA_DO_START_FLAG, "true");

        startActivity(intent);
    }

    private void startExampleTwo() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View promptsView = inflater.inflate(R.layout.dialog_enter_zip_code, null);
        final EditText txtZipCode = (EditText) promptsView.findViewById(R.id.txtZipCode);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                startExampleTwo(txtZipCode.getText().toString());
                dialog.cancel();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        
        alertDialogBuilder.create().show();
    }

    private void startExampleTwo(String zipCode) {
        Intent intent = new Intent(this, SimpleKSoapExample2Activity.class);
        intent.putExtra(SimpleKSoapExample2Activity.INTENT_EXTRA_ZIP_CODE, zipCode);

        startActivity(intent);
    }
}
