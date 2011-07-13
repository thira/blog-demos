package com.thira.examples.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.thira.examples.actionbar.widget.ActionBar;

public class CustomTitleActivity extends Activity {

	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.custom_title_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        mActionBar = (ActionBar) findViewById(R.id.actionBar);
		mActionBar.setTitle("Custom Title");
		
		// close this activity when the user presses the HOME icon 
		mActionBar.setHomeLogo(R.drawable.ic_title_home_default, new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomTitleActivity.this.finish();
			}
		});
		
		mActionBar.addActionIcon(R.drawable.ic_settings2, null);		
	}
}
