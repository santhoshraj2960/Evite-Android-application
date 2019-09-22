package com.vulcan.invite;

import com.google.android.gms.internal.cr;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;




public class ContactsActivity2 extends ActionBarActivity {
	
	static int PICK_CONTACT = 5;
	 public static final int REQUEST_CODE_PICK_CONTACT = 1;
	 int  max_pic_contact = 10;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_activity2);
		
		
		
		Button button = (Button)findViewById(R.id.pickcontact);

	    button.setOnClickListener(new OnClickListener() 
	        {
	            @Override
	            public void onClick(View v) 
	            {
	            	
	                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	                
	                
	                startActivityForResult(intent, PICK_CONTACT);
	                
	                //launchMultiplePhonePicker();
	            	
	                
	                

	             }
	         });
	    


	}
	    @Override
	    public void onActivityResult(int reqCode, int resultCode, Intent data) {
	      super.onActivityResult(reqCode, resultCode, data);
	      switch (reqCode) {
	        case (5) :
	          if (resultCode == Activity.RESULT_OK) {
	            Uri contactData = data.getData();
	            Cursor c =  getContentResolver().query(contactData, null, null, null, null);
	            if (c.moveToFirst()) {
	              String name1 = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	              
	              
	              
	             /* if (Integer.parseInt(c.getString(
	                        c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	            	  System.out.println("HasPhoneNumber");
	            	  
	            		ContentResolver cr = getContentResolver();
	                    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
	                            null, null, null, null);
	                    
	                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	                    if (Integer.parseInt(cur.getString(
	                          cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	                       Cursor pCur = cr.query(
	                                 ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                                 null,
	                                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
	                                 new String[]{id}, null);
	                       
	                           String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                           System.out.println("phoneNo :- " + phoneNo);
	                    }
	                           
	            	  
//	                         String phoneNo1 = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//	                         System.out.println("phoneNo :- " + phoneNo);
	                         
	              
	              }*/
	              System.out.println("NAME : " + name1);
	              
	              String phoneNumber = getPhoneNumber(name1,this);
	              System.out.println("NUM : " + phoneNumber);
	              
	              

	              SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
	              SharedPreferences.Editor editor = sharedpreferences.edit();
	              editor.putBoolean("hasPhone", true);
	              
	                
	                
	                
	                
	                editor.putString("name", name1);
	                editor.putString("contact", phoneNumber);
	                editor.commit();
	              
	              Toast.makeText(this, "Name: " + name1 + ", Phone No: " + phoneNumber, Toast.LENGTH_SHORT).show();
	              //System.out.println("phoneNo : " + phoneNo);
	              
	              // TODO Whatever you want to do with the selected contact name.
	            }
	          }
	          break;
	      }
	    }
	
	    
	    public String getPhoneNumber(String name, Context context) {
	    	String ret = null;
	    	String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
	    	String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
	    	Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	    	        projection, selection, null, null);
	    	if (c.moveToFirst()) {
	    	    ret = c.getString(0);
	    	}
	    	c.close();
	    	if(ret==null)
	    	    ret = "Unsaved";
	    	return ret;
	    	}
	    private void launchMultiplePhonePicker() {

	    	int   MAX_PICK_CONTACT = 10;
	        Intent phonebookIntent = new Intent("intent.action.INTERACTION_TOPMENU");
	        phonebookIntent.putExtra("additional", "phone-multi");
	        phonebookIntent.putExtra("maxRecipientCount", MAX_PICK_CONTACT);
	        phonebookIntent.putExtra("FromMMS", true);
	        startActivityForResult(phonebookIntent, REQUEST_CODE_PICK_CONTACT);

	     }
	    public void setContacts(View view)
		{
			startActivity(new Intent(ContactsActivity2.this, NewEvent.class));
			finish();
		}
	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts_activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
