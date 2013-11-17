package com.HackTx.roominize;
import java.util.Random;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class CreateAccount extends Activity {
	EditText name,username, key, password, passwordConfirm;
	Button submit;


    private DbxAccountManager mAccountManager;
    private DbxAccount mAccount;
    private DbxTable accountsTbl;
    private DbxDatastore store;
    static final int REQUEST_LINK_TO_DBX = 0;

    private DbxDatastore.SyncStatusListener mDatastoreListener = new DbxDatastore.SyncStatusListener() {
        @Override
        public void onDatastoreStatusChange(DbxDatastore d) {
            if (d.getSyncStatus().hasIncoming) {
                try {
                    store.sync();
                } catch (DbxException e) {
                   printError((e));
                }
            }
            //updateList();
        }
    };


	public AccountsDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        database = (AccountsDatabase) getIntent().getSerializableExtra("AccountsDatabase");


        // TODO Auto-generated method stub
        setContentView(R.layout.activity_create_account);
        database = (AccountsDatabase) getIntent().getSerializableExtra("AccountsDatabase");
		name = (EditText) findViewById(R.id.eName);
		username = (EditText) findViewById(R.id.eUsername);
		key = (EditText) findViewById(R.id.eKey);
		password = (EditText) findViewById(R.id.ePassword);
		passwordConfirm = (EditText) findViewById(R.id.ePasswordConfirm);
        Log.i("roominize", "button before \n\n");
		submit = (Button) findViewById(R.id.bSubmit);

		submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("roominize", "button after \n\n");


                // TODO Auto-generated method stub
                try {
                    if (checkAllFields()) {
                        Log.i("roominize", "we just set up an account fucker");
                        setUpAccount();
                        Log.i("roominize", "we just set up an account fucker");
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
        boolean flag = false;
		if(key.getText().toString().equals("")){
            realKey = generateKey();
            try{
                flag = database.aptkeyExists((realKey));
            }catch (DbxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while(flag){

                realKey = generateKey();
                try{
                    flag = database.aptkeyExists((realKey));
                }catch (DbxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else{
            realKey = key.getText().toString();
        }


		
		//FINISH HIM.
		try {
            database.createAccount(username.getText().toString(), password.getText().toString(), realKey, name.getText().toString());
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        toLogin();



	}

    public void toLogin(){
        Class ourClassC;
        try{

            ourClassC = Class.forName("com.HackTx.roominize.MainActivity");
            Intent ourIntentC = new Intent(CreateAccount.this, ourClassC);
            startActivity(ourIntentC);
        }catch(ClassNotFoundException e){
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
		if(!name.getText().toString().equals("") && !username.getText().toString().equals("") && !password.getText().toString().equals("") && !passwordConfirm.getText().toString().equals("")) isFilled = true;
		//Does the username already exist?

		isNewUsername = !(database.usernameExists(username.getText().toString()));
        String print;
        if(isNewUsername){
            print = "it's true";
        }
        else{
            print = "it's false";
        }
        Log.i("roominize", print);
		//Did the user enter a key? If so, does it already exist?
		if(key.getText().toString().equals("") || (database.aptkeyExists(key.getText().toString()))) isExistingKey=true;
		//Do the passwords match?
		if(password.getText().toString().equals(passwordConfirm.getText().toString())) isMatchingPass=true;
		
//		if(!isFilled)
//			Toast.makeText(CreateAccount.this,
//                    "All fields are required!", Toast.LENGTH_SHORT).show();
//		if(!isNewUsername)
//			Toast.makeText(CreateAccount.this,
//                    "I'm sorry, username is taken. Try again.", Toast.LENGTH_SHORT).show();
//		if(!isMatchingPass)
//			Toast.makeText(CreateAccount.this,
//                    "Passwords don't match...", Toast.LENGTH_SHORT).show();
//		if(!isExistingKey)
//			Toast.makeText(CreateAccount.this,
//                    "I'm terribly sorry, that key does not exist.", Toast.LENGTH_SHORT).show();
        Log.i("roominize", "we're about to return to method");
		return(isFilled && isNewUsername && isExistingKey && isMatchingPass);
	}

    private void printError(DbxException d){
        d.printStackTrace();
        Toast.makeText(this, d.getMessage(), Toast.LENGTH_SHORT).show();
    }


}








