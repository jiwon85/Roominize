package com.HackTx.roominize;

import java.util.Calendar;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CalendarDatabase extends Activity {

	static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you
	private Button mLinkButton;
	private DbxAccountManager mAccountManager;
	private DbxAccount mAccount;
	private DbxTable calendarTbl;
	private DbxDatastore store;
	private String aptkey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_database);
		super.onCreate(savedInstanceState);
	    mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, SECRET_KEY);
	    
	    
	    
	    mAccountManager.startLink((Activity)CalendarDatabase.this, REQUEST_LINK_TO_DBX);
	        

	    if (mAccountManager.hasLinkedAccount()) {
	        mAccount = mAccountManager.getLinkedAccount();
	        mLinkButton.setVisibility(View.GONE);
	        // ... Now display your own UI using the linked account information.
	    } else {
	        mLinkButton.setVisibility(View.VISIBLE);
	    }
	    
	    try {
			store = DbxDatastore.openDefault(mAccount);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_database, menu);
		return true;
	}
	
	public void initializeTable(String key) throws DbxException{
		aptkey = key;
		calendarTbl = store.getTable("calendar"+aptkey);		
	}
	
	public DbxTable.QueryResult getEvents() throws DbxException{
		DbxFields queryParams = new DbxFields();
		DbxTable.QueryResult results = calendarTbl.query(queryParams);
		return results;
	}
	
	public void addEvent(Calendar calendar, String name, String description) throws DbxException{
		calendarTbl.insert().set("month", calendar.get(Calendar.MONTH)).set("day", calendar.get(Calendar.DATE)).set("year", calendar.get(Calendar.YEAR)).set("name", name).set("descprition", description);
		store.sync();
	}
	
	//returns true if deleted
	public boolean deleteEvent(Calendar calendar, String name, String description) throws DbxException{
		DbxFields queryParams = new DbxFields().set("month", calendar.get(Calendar.MONTH)).set("day", calendar.get(Calendar.DATE)).set("year", calendar.get(Calendar.YEAR)).set("name", name).set("descprition", description);
		DbxTable.QueryResult results = calendarTbl.query(queryParams);
		DbxRecord firstResult = results.iterator().next();
		if(firstResult != null){
			firstResult.deleteRecord();
			store.sync();
			return true;
		}
		return false;
	}

}
