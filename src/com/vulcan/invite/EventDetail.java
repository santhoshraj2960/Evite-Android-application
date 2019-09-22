package com.vulcan.invite;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;





import android.support.v7.app.ActionBarActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetail extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		
		TextView name;
		TextView loc;
		TextView time;
		
		SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String eventName = sharedpreferences.getString("eventName", "");
        String location = sharedpreferences.getString("eventPlace", "");
        String eventTime = sharedpreferences.getString("eventTime", "");


        name = (TextView) findViewById(R.id.eventtextView);
        name.setText(eventName);
        
        loc = (TextView) findViewById(R.id.editText2);
        loc.setText(location);
        
        time = (TextView) findViewById(R.id.editText3);
        time.setText(eventTime);
        
        
        
		
		
        //Snew EventTask(this.getContentResolver(),getApplicationContext()).execute();
        
	}

	public void showLocation(View view) 
	{
		startActivity(new Intent(EventDetail.this, LocationActivity2.class));
		
		
		
	}
	
	
	  class EventTask extends AsyncTask<String, Void, String> {

	        ContentResolver content;
	        Context mContext;
	        public EventTask(ContentResolver content,Context context)
	        {
	              this.content = content;
	              this.mContext = context;
	          	
	        }

	        @Override
	        public String doInBackground(String... arg)
	        {
	        	

				String deviceUUId = Settings.Secure.getString(content, Settings.Secure.ANDROID_ID);
		        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
		        String userPhoneNumber = sharedpreferences.getString("user-phone-number", "");
		        String eventId = sharedpreferences.getString("event-id", "");
		        String deviceId = sharedpreferences.getString("device-id", "");
	            String deviceKey = sharedpreferences.getString("device-key", "");
		        
		        System.out.println("userPhoneNumber:-"+ userPhoneNumber);
		        System.out.println("deviceUUId:-"+ deviceUUId);
		        System.out.println("eventId:-" + eventId);
		        
				
	            try {
	               // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	                //StrictMode.setThreadPolicy(policy);
	            	
	            	String slash="/";
	            	String url = "http://106.51.127.58/apis/v1/events/";
	            	url=url+eventId+slash;
	            	System.out.println("URL:-" + url);

	                JSONObject jsonObj = new JSONObject();
	                jsonObj.put("device-uuid", deviceUUId);
	                jsonObj.put("user-phone-number", userPhoneNumber);
	                jsonObj.put("device-id", deviceId);
	                jsonObj.put("device-key", deviceKey);

	                StringEntity se;
	                se = new StringEntity(jsonObj.toString());

	                // Set HTTP parameters

	                HttpPost httpPost = new HttpPost(url);

	                httpPost.setEntity(se);
	                
	                Log.i("request",jsonObj.toString());

	                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
	                // ObjectMapper mapper = new ObjectMapper();
	                // json = mapper.writeValueAsString(person);

	                HttpParams httpParams = new BasicHttpParams();
	                HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	                HttpConnectionParams.setSoTimeout(httpParams, 10000);

	                HttpClient client = new DefaultHttpClient(httpParams);
	                HttpResponse response = client.execute(httpPost);
	                Log.d("HTTP", "HTTP: OK");
	                HttpEntity ent = response.getEntity();
	                
	                String responseText = EntityUtils.toString(ent);
	                JSONObject responseJson = new JSONObject(responseText);
	                Log.i("response", responseText);
	                
	                int status = response.getStatusLine().getStatusCode();
	                System.out.println("status");
	                System.out.println(status);
	                
	                if(status==200)
	                {

	               /*     SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
	                    SharedPreferences.Editor editor = sharedpreferences.edit();
	                    editor.putString("user-phone-number", userNum);
	                    editor.commit();
	                	finish();
	                	startActivity(new Intent(LoginActivity.this, Dummy.class));
	                	*/
	                	
	            		return "";
	                	
	                	
	                }
	              

	            } catch (Exception er) {
	                Log.e("HTTP", "Error in http connection " + er.toString());
	            }
	           return "error";
	        }

	        @Override
	        protected void onPostExecute(String s) {
	            super.onPostExecute(s);
	            if(s.equals("invalid"))
	               Toast.makeText(this.mContext, "Invalid Phone Number...", Toast.LENGTH_LONG).show();
	            else if(s.equals("error"))
	                Toast.makeText(this.mContext, "Network Error Please try again Later...", Toast.LENGTH_LONG).show();
	            else if(s.equals("noPhone"))
	                Toast.makeText(getApplicationContext(), "Please Enter the Phone number First...", Toast.LENGTH_LONG).show();
	        }
	    }
	

}
