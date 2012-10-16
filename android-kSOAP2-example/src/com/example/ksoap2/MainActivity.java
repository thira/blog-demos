package com.example.ksoap2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ((Button)findViewById(R.id.btnSimpleKSoap)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                startActivity(SimpleKSoapActivity.class);
            }
        });
    }
    
    private void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
