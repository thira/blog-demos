package com.example.ksoap2;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ksoap2.adapter.CityForecastInfoAdapter;
import com.example.ksoap2.wsclient.AsyncTaskManager;
import com.example.ksoap2.wsclient.OnAsyncTaskCompleteListener;
import com.example.ksoap2.wsclient.weather.GetCityForcastByZipCodeTask;
import com.example.ksoap2.wsclient.weather.bo.CityForecastBO;

public class SimpleKSoapExample2Activity extends Activity {

    private static final String TAG = SimpleKSoapExample2Activity.class.getName();

    static final String INTENT_EXTRA_ZIP_CODE = "SimpleKSoapExample2Activity.INTENT_EXTRA_ZIP_CODE";

    private ListView lvCityForcastSummary;
    private TextView lblCityInfo;
    private TextView lblForecastDescription;
    private Dialog dialog;

    private AsyncTaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_ksoap_example_2);

        taskManager = new AsyncTaskManager(this);

        final EditText txtZipCode = (EditText) findViewById(R.id.txtZipCode);
        lblCityInfo = (TextView) findViewById(R.id.lblCityInfo);
        lblForecastDescription = (TextView) findViewById(R.id.lblForecastDescription);
        lvCityForcastSummary = (ListView) findViewById(R.id.lvCityForcastSummary);
        ((Button) findViewById(R.id.btnTestExample2)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                String zipCode = txtZipCode.getText().toString();
                getCityForcast(zipCode);
            }
        });

        // invoke the web service if the activity was invoked with a flag to invoke the web service
        if (getIntent() != null && getIntent().getExtras().containsKey(INTENT_EXTRA_ZIP_CODE)) {
            getCityForcast(getIntent().getExtras().getString(INTENT_EXTRA_ZIP_CODE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (taskManager == null) {
            taskManager = new AsyncTaskManager(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    private void getCityForcast(String zipCode) {
        if (StringUtils.isEmpty(zipCode)) {
            Builder builder = new Builder(this);
            builder.setTitle("Missing ZIP code");
            builder.setMessage("Please specify the ZIP code and try again.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("OK", null);

            displayNewDialog(builder);
        } else {
            GetCityForcastByZipCodeTask task = new GetCityForcastByZipCodeTask();
            taskManager.executeTask(task, GetCityForcastByZipCodeTask.createRequest(zipCode), getString(R.string.example_2_ws_in_progress),
                    new OnAsyncTaskCompleteListener<CityForecastBO>() {

                        @Override
                        public void onTaskCompleteSuccess(CityForecastBO result) {
                            updateForecastDetails(result);
                        }

                        @Override
                        public void onTaskFailed(Exception cause) {
                            Log.e(TAG, cause.getMessage(), cause);
                            showToastMessage(R.string.example_2_failed_to_invoke_ws);
                        }
                    });
        }
    }

    private void updateForecastDetails(CityForecastBO result) {
        if (result == null) {
            lblCityInfo.setText("No city forecast information could be found. A possible web service error (please refer to LogCat logs)");
            lblForecastDescription.setText("");
            lvCityForcastSummary.removeAllViews();
        } else if (!result.isSuccess()) {
            lblCityInfo.setText("Could not obtain forecast info for the specified ZIP code");
            lblForecastDescription.setText(result.getResponseText());
            lvCityForcastSummary.removeAllViews();
        } else {
            lblCityInfo.setText(String.format("Forecast information for city: %s (state: %s, weather station: %s)", result.getCity(),
                    result.getState(), result.getWeatherStationCity()));
            lblForecastDescription.setText(result.getResponseText());
            lvCityForcastSummary.setAdapter(new CityForecastInfoAdapter(this, result.getForecasts()));
        }
    }

    private void showToastMessage(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }

    private void displayNewDialog(Builder builder) {
        dismissDialog();
        dialog = builder.create();
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
