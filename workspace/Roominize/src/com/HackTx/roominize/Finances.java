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

public class Finances extends Activity implements OnClickListener {

	ImageButton imageBHome;
	ImageButton imageBCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.financeslayout);

		imageBHome = (ImageButton) findViewById(R.id.home);
		imageBCalendar = (ImageButton) findViewById(R.id.calendar);

		imageBHome.setOnClickListener(this); 
		imageBCalendar.setOnClickListener(this); 
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
				Class ourClassH = Class.forName("com.HackTx.roominize.homePage"); 
				Intent ourIntentH = new Intent(Finances.this, ourClassH); 
				startActivity(ourIntentH); 
			}catch(ClassNotFoundException e){ 
				e.printStackTrace(); 
			}     
			break; 

		case R.id.calendar: 
			try { 
				Class ourClassC = Class.forName("com.HackTx.roominize.homePage"); 
				Intent ourIntentC = new Intent(Finances.this, ourClassC); 
				startActivity(ourIntentC); 
			} catch (ClassNotFoundException e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
			}
			break; 
		} 

	}
}



