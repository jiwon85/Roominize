package com.HackTx.roominize;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.Serializable;


public class AccountsDatabase implements Serializable{

    private DbxAccountManager mAccountManager;
    private DbxAccount mAccount;
    private DbxTable accountsTbl;
    private DbxDatastore store;
    private Activity returnToActivity;
    private Activity savedActivity;

    public AccountsDatabase(Activity classInstance){
        initializeDatabase(classInstance);
    }

    public void initializeDatabase(Activity classInstance){
        //returnToActivity = classInstance;

        Class ourClassC;
        try{
            ourClassC = Class.forName("com.HackTx.roominize.AccountsDatabaseBaby");
            Intent ourIntentC = new Intent(classInstance, ourClassC);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public boolean usernameExists(String username) throws DbxException{
        DbxFields queryParams = new DbxFields().set("username", username);
        DbxTable.QueryResult results = accountsTbl.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        if(firstResult!=null){
            return true;
        }
        return false;
    }

    //returns null if login is wrong
    //returns apt key if it is right match
    public String login(String username, String pw) throws DbxException{
        DbxFields queryParams = new DbxFields().set("username", username).set("pw", pw);
        DbxTable.QueryResult results = accountsTbl.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        if(firstResult == null){
            return null;
        }
        return firstResult.getString("aptkey");
    }

    public void createAccount(String username, String pw, String aptkey, String firstname) throws DbxException{
        accountsTbl.insert().set("username", username).set("pw", pw).set("aptkey", aptkey).set("name", firstname);
        store.sync();
    }

    public boolean aptkeyExists(String aptkey) throws DbxException{
        DbxFields queryParams = new DbxFields().set("aptkey", aptkey);
        DbxTable.QueryResult results = accountsTbl.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        if(firstResult == null){
            return false;
        }
        return true;
    }

    //make sure to only call it after you call aptkeyExists
    //you can call iterator on it
    public DbxTable.QueryResult getResultsForKey(String aptkey) throws DbxException{
        DbxFields queryParams = new DbxFields().set("aptkey", aptkey);
        return accountsTbl.query(queryParams);
    }

    //returns true if the account deleted
    public boolean deleteAccount(String username, String pw) throws DbxException{
        DbxFields queryParams = new DbxFields().set("username", username).set("pw", pw);
        DbxTable.QueryResult results = accountsTbl.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        if(firstResult == null){
            return false;
        }
        firstResult.deleteRecord();
        store.sync();
        return true;
    }

    public void endActivity(){
        //savedActivity.finish();
        store.close();
    }









    public class AccountsDatabaseBaby extends Activity {

	    static final int REQUEST_LINK_TO_DBX = 0;



	@Override
	    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_accounts_database);
            super.onCreate(savedInstanceState);
	        mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, SECRET_KEY);
	    
	    
//	    mLinkButton = (Button) findViewById(R.id.loginB);
//	    mLinkButton.setOnClickListener(new OnClickListener() {
//	        @Override
//	        public void onClick(View v) {
	        mAccountManager.startLink((Activity)AccountsDatabaseBaby.this, REQUEST_LINK_TO_DBX);
//	        }
//	    });
	    
//	    mLinkButton2 = (Button) findViewById(R.id.registerB);
//	    mLinkButton2.setOnClickListener(new OnClickListener() {
//	        @Override
//	        public void onClick(View v) {
//	            mAccountManager.startLink((Activity)AccountsDatabase.this, REQUEST_LINK_TO_DBX);
//	        }
//	    });

	    
	    

	        if (mAccountManager.hasLinkedAccount()){ //{
	            mAccount = mAccountManager.getLinkedAccount();
                try {
                    store = DbxDatastore.openDefault(mAccount);
                } catch (DbxException e) {
                    e.printStackTrace();
                }

            }
	        //mLinkButton.setVisibility(View.GONE);
	        // ... Now display your own UI using the linked account information.
//	    } else {
//	        mLinkButton.setVisibility(View.VISIBLE);

//	    }
            accountsTbl = store.getTable("accounts");
//


	    }

	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		    getMenuInflater().inflate(R.menu.accounts_database, menu);
		    return true;
	    }
	
	@Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_LINK_TO_DBX) {
	            if (resultCode == Activity.RESULT_OK) {
	                mAccount = mAccountManager.getLinkedAccount();
	            //mLinkButton.setVisibility(View.GONE);
	            // ... Now display your own UI using the linked account information.
	            } else {
	            // ... Link failed or was cancelled by the user.
	            }
	        } else {
	            super.onActivityResult(requestCode, resultCode, data);
	        }

	    }




    }


}
