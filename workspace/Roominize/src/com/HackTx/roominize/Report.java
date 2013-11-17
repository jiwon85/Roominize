package com.HackTx.roominize;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Report extends Activity implements OnClickListener {

	EditText item;
	EditText amount;
	EditText day;
	EditText month;
	EditText year;
	Button submitB;
	Button backB;

	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reportlayout);

		item = (EditText) findViewById(R.id.itemEdit);
		amount = (EditText) findViewById(R.id.amountEdit);
		day = (EditText) findViewById(R.id.dayEdit);
		month = (EditText) findViewById(R.id.monthEdit);
		year = (EditText) findViewById(R.id.yearEdit);
		submitB = (Button) findViewById(R.id.submitB);
		backB = (Button) findViewById(R.id.backB);

		// If you click the Submit
		submitB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				double amountMoney = Double.parseDouble(amount.getText().toString());
				int day = Integer.parseInt(amount.getText().toString());
				int month = Integer.parseInt(amount.getText().toString());
				int year = Integer.parseInt(amount.getText().toString());
				String itemString = item.getText().toString();
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				//MainActivity.mainAccountData.addPurchase(calendar, MainActivity.userNameString, amountMoney,itemString);
			}
		});

		// If you click the BackButton
		backB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Class ourClassC;
				try {
					ourClassC = Class.forName("com.example.roomateapp.finance");
					Intent ourIntentC = new Intent(Report.this, ourClassC); 
					startActivity(ourIntentC);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


}

