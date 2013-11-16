package com.HackTx.roominize;

import com.dropbox.sync.android.DbxAccountManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	
	public static AccountsDatabase mainAccountData = new AccountsDatabase();
	public static String userNameString;
	EditText userName;
	EditText password;
	Button loginB;
	Button registerB;

	String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userName = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.password);

		loginB = (Button) findViewById(R.id.loginB);
		registerB = (Button) findViewById(R.id.registerB);
		
		userNameString = userName.getText().toString();

		// If you click the login
		loginB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				key = login(userName.getText().toString(), password.getText().toString());
				// If credentials are incorrect
				if(key == null) {
					showError();
				}
				
				// Go to home screen 
				else {
					Class ourClassC;
					try {
						ourClassC = Class.forName("com.example.roomateapp.homePage");
						Intent ourIntentC = new Intent(MainActivity.this, ourClassC); 
						startActivity(ourIntentC);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		// If you click the register
		registerB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Go to register page 
				Class ourClassC;
				try {
					ourClassC = Class.forName("com.example.roomateapp.CreateAccount");
					Intent ourIntentC = new Intent(MainActivity.this, ourClassC); 
					startActivity(ourIntentC);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});


	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void showError() {
		userName.setError("Password and username didn't match");
	}

	private String login(String u, String p) {
		if(u.equals("irv05") && p.equals("pimp") ) {
			return "hi";
		}
		else {
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
