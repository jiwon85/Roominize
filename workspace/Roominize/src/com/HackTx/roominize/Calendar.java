package com.HackTx.roominize;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Calendar extends Activity implements OnClickListener {

	ImageButton imageBHome;
	ImageButton imageBFinances;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarlayout);

		imageBHome = (ImageButton) findViewById(R.id.home);
		imageBFinances = (ImageButton) findViewById(R.id.finances);

		imageBHome.setOnClickListener(this); 
		imageBFinances.setOnClickListener(this); 
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){ 
		case R.id.home: 
			try{ 
				Class ourClassH = Class.forName("com.example.roomateapp.homePage"); 
				Intent ourIntentH = new Intent(Calendar.this, ourClassH); 
				startActivity(ourIntentH); 
			}catch(ClassNotFoundException e){ 
				e.printStackTrace(); 
			}     
			break; 

		case R.id.finances: 
			try { 
				Class ourClassF = Class.forName("com.example.roomateapp.finances"); 
				Intent ourIntentF = new Intent(Calendar.this, ourClassF); 
				startActivity(ourIntentF); 
			} catch (ClassNotFoundException e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
			}
			break; 
		} 

	}
}

