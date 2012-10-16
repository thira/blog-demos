package com.example.ksoap2;

import java.util.List;

import com.example.ksoap2.adapter.WeatherDescriptionAdapter;
import com.example.ksoap2.wsclient.AsyncTaskManager;
import com.example.ksoap2.wsclient.OnAsyncTaskCompleteListener;
import com.example.ksoap2.wsclient.weather.GetWeatherInformationTask;
import com.example.ksoap2.wsclient.weather.bo.WeatherDescriptionBO;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleKSoapExample1Activity extends Activity {

    private static final String TAG = SimpleKSoapExample1Activity.class.getName();

    static final String INTENT_EXTRA_DO_START_FLAG = "SimpleKSoapExample1Activity_INTENT_EXTRA_DO_START_FLAG";

    private ListView lvWeatherInfo;
    private TextView lblResultCount;

    private AsyncTaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_ksoap_example_1);

        taskManager = new AsyncTaskManager(this);

        lblResultCount = (TextView)findViewById(R.id.lblResultCount);
        lvWeatherInfo = (ListView) findViewById(R.id.lvWeatherInfo);
        ((Button) findViewById(R.id.btnTestExample1)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                getWeatherInformation();
            }
        });

        // invoke the web service if the activity was invoked with a flag to invoke the web service
        if (getIntent() != null && getIntent().getExtras().containsKey(INTENT_EXTRA_DO_START_FLAG)) {
            getWeatherInformation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (taskManager == null) {
            taskManager = new AsyncTaskManager(this);
        }
    }

    private void getWeatherInformation() {
        GetWeatherInformationTask task = new GetWeatherInformationTask();
        taskManager.executeTask(task, GetWeatherInformationTask.createRequest(), getString(R.string.example_1_ws_in_progress),
                new OnAsyncTaskCompleteListener<List<WeatherDescriptionBO>>() {

                    @Override
                    public void onTaskCompleteSuccess(List<WeatherDescriptionBO> result) {
                        displayWeatherDetailsResults(result);
                    }

                    @Override
                    public void onTaskFailed(Exception cause) {
                        Log.e(TAG, cause.getMessage(), cause);
                        showToastMessage(R.string.example_1_failed_to_invoke_ws);
                    }
                });
    }

    private void displayWeatherDetailsResults(List<WeatherDescriptionBO> results) {
        lblResultCount.setText(String.format("Found %d results", results.size()));
        lvWeatherInfo.setAdapter(new WeatherDescriptionAdapter(this, results));
        
        showToastMessage(R.string.example_1_ws_success);
    }

    private void showToastMessage(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }
}
