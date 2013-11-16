package com.HackTx.roominize;
import java.util.Random;

import com.dropbox.sync.android.DbxException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class CreateAccount extends Activity {
	EditText name,username, key, password, passwordConfirm;
	Button submit;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		name = (EditText) findViewById(R.id.eName);
		username = (EditText) findViewById(R.id.eUsername);
		key = (EditText) findViewById(R.id.eKey);
		password = (EditText) findViewById(R.id.ePassword);
		passwordConfirm = (EditText) findViewById(R.id.ePasswordConfirm);
		submit = (Button) findViewById(R.id.bSubmit);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if(checkAllFields())
					{
						setUpAccount();
					}
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});	
		
	}
	public void setUpAccount()
	{
		String realKey;
		if(key.getText().toString() ==null) realKey = generateKey();
		else realKey = key.getText().toString();
		
		//FINISH HIM.
		try {
			MainActivity.mainAccountData.createAccount(username.getText().toString(), password.getText().toString(), realKey, name.getText().toString());
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String generateKey()
	{
		Random picker = new Random();
		
		int digit= picker.nextInt(899)+100;
		char A=(char)(picker.nextInt(26)+65);
		char B=(char)(picker.nextInt(26)+65);
		
		return (""+digit+A+B);
	}
	public boolean checkAllFields() throws DbxException
	{
		boolean isFilled=false, isNewUsername=false, isExistingKey=false, isMatchingPass=false;
		//Did the user forget to fill in a blank? (he's retarded lolz)
		if(name.getText().toString()!=null && username.getText().toString() != null && password.getText().toString() != null && passwordConfirm.getText().toString() != null) isFilled = true;
		//Does the username already exist?
		isNewUsername = !(MainActivity.mainAccountData.usernameExists(username.getText().toString()));
		//Did the user enter a key? If so, does it already exist?
		if(key== null || (MainActivity.mainAccountData.aptkeyExists(key.getText().toString()))) isExistingKey=true;
		//Do the passwords match?
		if(password.getText().toString() == passwordConfirm.getText().toString()) isMatchingPass=true;
		
		if(!isFilled)
			Toast.makeText(CreateAccount.this,
                    "All fields are required!", Toast.LENGTH_SHORT).show();
		else if(!isNewUsername)
			Toast.makeText(CreateAccount.this,
                    "I'm sorry, username is taken. Try again.", Toast.LENGTH_SHORT).show(); 
		else if(!isMatchingPass)
			Toast.makeText(CreateAccount.this,
                    "Passwords don't match...", Toast.LENGTH_SHORT).show();
		else if(!isExistingKey)
			Toast.makeText(CreateAccount.this,
                    "I'm terribly sorry, that key does not exist.", Toast.LENGTH_SHORT).show();
		
		return(isFilled && isNewUsername && isExistingKey && isMatchingPass);
	}
}