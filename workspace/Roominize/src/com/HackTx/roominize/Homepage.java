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

public class Homepage extends Activity implements OnClickListener {

	ImageButton radioBCalendar;
	ImageButton radioBFinances;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepagelayout);

		radioBCalendar = (ImageButton) findViewById(R.id.calendar);
		radioBFinances = (ImageButton) findViewById(R.id.finances);

		radioBCalendar.setOnClickListener(this); 
		radioBFinances.setOnClickListener(this);
		
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
		case R.id.calendar: 
			try{ 
				Class ourClassC = Class.forName("com.example.roominize.calendar"); 
				Intent ourIntentC = new Intent(Homepage.this, ourClassC); 
				startActivity(ourIntentC); 
			}catch(ClassNotFoundException e){ 
				e.printStackTrace(); 
			}     
			break; 

		case R.id.finances: 
			try { 
				Class ourClassF = Class.forName("com.example.roominize.finances"); 
				Intent ourIntentF = new Intent(Homepage.this, ourClassF); 
				startActivity(ourIntentF); 
			} catch (ClassNotFoundException e) { 
				// TODO Auto-generated catch block 
				//e.printStackTrace(); 
			}
			break; 
		} 

	}
}

