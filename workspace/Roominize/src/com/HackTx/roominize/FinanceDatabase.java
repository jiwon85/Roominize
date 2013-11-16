package com.HackTx.roominize;

import com.dropbox.sync.android.DbxAccount;

import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.Button;

public class FinanceDatabase extends Activity {
	static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you

	private Button mLinkButton;
	private DbxAccountManager mAccountManager;
	private DbxAccount mAccount;
	private DbxTable financesTbl;
	private DbxDatastore store;
	private String aptkey;
	private DbxTable.QueryResult roommates; 
	private DbxTable whatioweTbl;
	private DbxTable paymenthistoryTbl;
	private ArrayList<String> roommateIndex;
	private AccountsDatabase accounts;
	

	
	//this class needs to call the initializeTables method right after initialization or it won't work!!!!
	//!!!!!!!!!!!!!!!
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_database);
		super.onCreate(savedInstanceState);
	    mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), "j3kj9rrkkr0b6oc", "5kwtzx6t6vt63ov");
	    
	    
	    
	    mAccountManager.startLink((Activity)FinanceDatabase.this, REQUEST_LINK_TO_DBX);
	       

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
		getMenuInflater().inflate(R.menu.finance_database, menu);
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            mAccount = mAccountManager.getLinkedAccount();
	            mLinkButton.setVisibility(View.GONE);
	            // ... Now display your own UI using the linked account information.
	        } else {
	            // ... Link failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	public void initializeTable(String key, AccountsDatabase acc) throws DbxException{
		accounts = acc;
		this.aptkey = key;
		financesTbl = store.getTable("finances"+aptkey);
		whatioweTbl = store.getTable("whatiowe"+aptkey);
		paymenthistoryTbl = store.getTable("paymentHistory"+aptkey);
		roommates = accounts.getResultsForKey(aptkey);
		DbxRecord firstResult = roommates.iterator().next();
		while(firstResult!=null){
			roommateIndex.add(firstResult.getString("username"));
		}
		
		
	
		
	}
	
	//everything from date is an int... 

	public void addPurchase(Calendar calendar, String username, double price, String item) throws DbxException{	
		financesTbl.insert().set("month", calendar.get(Calendar.MONTH)).set("day", calendar.get(Calendar.DATE)).set("year", calendar.get(Calendar.YEAR)).set("item", item).set("username", username);
		addDebt(username, price);
		store.sync();
		
	}
	

	public boolean deletePurchase(Calendar calendar, String username, double price, String item) throws DbxException{
		DbxFields queryParams = new DbxFields().set("month", calendar.get(Calendar.MONTH)).set("day", calendar.get(Calendar.DATE)).set("year", calendar.get(Calendar.YEAR)).set("item", item).set("username", username);
		DbxTable.QueryResult results = financesTbl.query(queryParams);
		DbxRecord firstResult = results.iterator().next();
		if(firstResult == null){
			return false;
		}
		addDebt(username, price*-1);
		firstResult.deleteRecord();
		store.sync();
		return true;
	}
	
	public DbxTable.QueryResult returnPurchasesPerUser(String username) throws DbxException{
		DbxFields queryParams = new DbxFields().set("username", username);
		DbxTable.QueryResult results = financesTbl.query(queryParams);
		return results;
	}
	
	public DbxTable.QueryResult returnAll() throws DbxException{
		DbxFields queryParams = new DbxFields();
		DbxTable.QueryResult results = financesTbl.query(queryParams);
		return results;
	}
	
	
	
	private void addDebt(String username, double price) throws DbxException{
		for(String current: roommateIndex){
			if(!username.equals(current)){
				DbxFields queryParams = new DbxFields().set("username", current).set("otheruser", username);
				DbxTable.QueryResult results = whatioweTbl.query(queryParams);
				DbxRecord firstResult = results.iterator().next();
				if(firstResult == null){
					whatioweTbl.insert().set("username", current).set("otheruser", username).set("amount", (price/roommateIndex.size()));
				}
				else{
					double currentAmount = firstResult.getDouble("amount");
					firstResult.set("amount", currentAmount+(price/roommateIndex.size()));
				}
				store.sync();
			}
		}
	}
	
	//returns new balance
	public double payback(String username, String whoToPay, double amount, Calendar calendar) throws DbxException{
		paymenthistoryTbl.insert().set("username", username).set("month", calendar.get(Calendar.MONTH)).set("day", calendar.get(Calendar.DATE)).set("year", calendar.get(Calendar.YEAR)).set("amount", amount);
		DbxFields queryParams = new DbxFields().set("username", username).set("otheruser", whoToPay);
		DbxTable.QueryResult results = whatioweTbl.query(queryParams);
		DbxRecord firstResult = results.iterator().next();
		double amountLeft;
		if(firstResult == null){
			amountLeft = (amount*-1);
			whatioweTbl.insert().set("username", username).set("otheruser", whoToPay).set("amount", amountLeft);
		}
		else{
			amountLeft = firstResult.getDouble("amount") - amount;
			firstResult.set("amount", amountLeft);
		}
		store.sync();
		return amountLeft;
	}
	
	public double howMuchOwedToThisRoommate(String username) throws DbxException{
		DbxFields queryParams = new DbxFields().set("otheruser", username);
		DbxTable.QueryResult results = whatioweTbl.query(queryParams);
		double total = 0;
		for(DbxRecord firstResult: results){
			total+=firstResult.getDouble("amount");
		}
		return total;
	}
	
	public double howMuchThisRoommateOwes(String username) throws DbxException{
		DbxFields queryParams = new DbxFields().set("username", username);
		DbxTable.QueryResult results = whatioweTbl.query(queryParams);
		double total = 0;
		for(DbxRecord firstResult: results){
			total+=firstResult.getDouble("amount");
		}
		return total;
	}
	
	public DbxTable.QueryResult allPaymentHistory() throws DbxException{
		DbxFields queryParams = new DbxFields();
		DbxTable.QueryResult results = paymenthistoryTbl.query(queryParams);
		return results;
	}
	
	public DbxTable.QueryResult paymentHistoryForUser(String username) throws DbxException{
		DbxFields queryParams = new DbxFields().set("username", username);
		DbxTable.QueryResult results = paymenthistoryTbl.query(queryParams);
		return results;
	}
	
	
	


}
