package com.thira.examples.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.thira.examples.actionbar.widget.ActionBar;

public class MainActivity extends Activity {

	private ActionBar mActionBar;
	private int mActionIconCount;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prevent the default title-bar from beig displayed
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		mActionIconCount = 0;

		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		mActionBar.setTitle(R.string.app_name);
		mActionBar.setHomeLogo(R.drawable.ic_title_home_default);

		ToggleButton btnToggle = (ToggleButton) findViewById(R.id.btnShowProgressBar);
		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					mActionBar.showProgressBar();
				} else {
					mActionBar.hideProgressBar();
				}
			}
		});
		
		final OnClickListener actionIconClickListener = new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Clicked on an ActionIcon", Toast.LENGTH_SHORT).show();
			}
		};

		Button btnAddActions = (Button) findViewById(R.id.btnAddActions);
		btnAddActions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int mod = mActionIconCount % 3;
				final int iconId;
				switch (mod) {
				case 0:
					iconId = R.drawable.ic_settings1;
					break;
				case 1:
					iconId = R.drawable.ic_settings2;
					break;
				default:
					iconId = R.drawable.ic_settings3;
					break;
				}
				mActionBar.addActionIcon(iconId, actionIconClickListener);
				mActionIconCount++;
			}
		});

		Button btnRemoveActions = (Button) findViewById(R.id.btnRemoveActions);
		btnRemoveActions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mActionBar.removeActionIconAt(mActionIconCount - 1)) {
					mActionIconCount--;
				}
			}
		});
		
		Button btnStartChild = (Button)findViewById(R.id.btnStartChildActivity);
		btnStartChild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, CustomTitleActivity.class));
			}
		});
	}
}