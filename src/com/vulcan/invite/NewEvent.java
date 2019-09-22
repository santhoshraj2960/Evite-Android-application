package com.vulcan.invite;

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.vulcan.invite.SuperActivity.ActorsAsyncTask;

import android.support.v7.app.ActionBarActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class NewEvent extends ActionBarActivity implements View.OnClickListener {
	
	DateFormat formate=DateFormat.getDateInstance();
	Calendar calendar=Calendar.getInstance();
	TextView label;
	TextView label2;
	Button btn; 
	Button btn2; 
	Button btn4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);
		
		System.out.println("Inside On create");
		
		long unixTime = System.currentTimeMillis() / 1000L;
		System.out.println("Current UNIX TIME = " + unixTime);
		
		long dv = Long.valueOf(unixTime)*1000;// its need to be in milisecond
		Date df = new java.util.Date(dv);
		String curDate = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
		
		System.out.println("CURRENT TIME = " + curDate);

//		
//		String str_date="13-09-2015";
//		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		Date date;
//		try {
//			date = (Date)formatter.parse(str_date);
//			System.out.println("Today is " +date.getTime());
//			
//			
//			long dv2 = Long.valueOf(date.getTime())*1000;// its need to be in milisecond
//			Date df2 = new java.util.Date(dv2);
//			String curDate2 = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df2);
//			
//			System.out.println("CURRENT TIME inside try for 13-09-2011 is = " + curDate2);
//			
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
		
		
		label=(TextView) findViewById(R.id.textView1);
		label2=(TextView) findViewById(R.id.textView2);
		btn=(Button) findViewById(R.id.button1);
		btn2=(Button) findViewById(R.id.button2);
		btn4=(Button) findViewById(R.id.button4);
		label.setOnClickListener((OnClickListener) this);
		
		
		SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        
        String Address = sharedpreferences.getString("AddressLine", "");
        String latitude = sharedpreferences.getString("LAT_LON", "");
        
        String name = sharedpreferences.getString("name", "");
       
		 
		 
		 if(sharedpreferences.getBoolean("hasLat", false))
		 {
			 btn2.setText(Address);
			// btn2.setText(latitude);
			 
		 
		 }
		 else
		 {
			 btn2.setText("Where");
			 
		 }
        
		 
		 
		 
		 if(sharedpreferences.getBoolean("hasPhone", false))
		 {
			 btn4.setText(name);
			// 
			 
		 
		 }
		 else
		 {
			 btn4.setText("Contacts");
			 
		 }
        
		
        
		if(sharedpreferences.getBoolean("hasLat", false))
		{
//			 String Latitude = sharedpreferences.getString("latitude", "");
//			 System.out.println("Latitude "+Latitude);
//	         
//			btn2.setText(Latitude);
		}
   		
		
	}
	
	
	
	public void getLocation(View view) 
	{
		startActivity(new Intent(NewEvent.this, LocationActivity.class));
		
		Toast.makeText(NewEvent.this, "Select the place", Toast.LENGTH_LONG).show();
		
	}
	public void contacts(View view) 																			
	{
		startActivity(new Intent(NewEvent.this, ContactsActivity2.class));
		
		Toast.makeText(NewEvent.this, "Select the Contacts", Toast.LENGTH_LONG).show();
		
	}
	/*public void showtoa(View v){
		 Toast.makeText(NewEvent.this, "Event has been created", Toast.LENGTH_LONG).show();
		
	}*/
	
	
	
	// Change this to show toa later
	public void showtoa(View v) {
		 Toast.makeText(NewEvent.this, "Event has been created", Toast.LENGTH_LONG).show();
		 new EventsAsyncTask(this.getContentResolver(),getApplicationContext()).execute();

		 
		 
		 
		
	}
	public void Back(View v) {
			Intent i=new Intent(this,SuperActivity.class);
			startActivity(i);

		}
		
	public void updatedate()
	{
		label.setText(formate.format(calendar.getTime())) ;
		Date time=calendar.getTime();
		String strTime = time.toString();
		
		
		
		System.out.println("Time = " + strTime);
		String mo=strTime.substring(4, 7);
		System.out.println("Month = " + mo);
		
		String d=strTime.substring(8, 10);
		System.out.println("Date = " + d);

		String t=strTime.substring(11, 16);
		System.out.println("Time = " + t);
		
		String y=strTime.substring(30, 34);
		System.out.println("year = " + y);
		
		
		
//		String str_date="13-09-2011";
//		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		Date date;
//		try {
//			date = (Date)formatter.parse(str_date);
//			System.out.println("Today is " +date.getTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//		
		
		SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("eventTime", strTime);
        editor.commit();
	}
	public void setDate()
	{
		new DatePickerDialog(NewEvent.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	
	DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(calendar.YEAR, year);
			calendar.set(calendar.MONTH, monthOfYear);
			calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
			updatedate();
			
			
		}
		
	};
	

	@Override
	public void onClick(View v) {
		
		timeOnClick();
		setDate();
		
		
	}
	public void updateTime()
	{
		//label2.setText(formate.format(calendar.getTime()));
		String timeFormat="hh:mm a";
		SimpleDateFormat stf=new SimpleDateFormat(timeFormat);
		label2.setText(stf.format(calendar.getTime()));
		
		Date time=calendar.getTime();
		String strTime = time.toString();
		
		int timestamp = (int) (calendar.getTimeInMillis() / 1000);
		
		
		System.out.println("Time stamp = " + timestamp);
		
		
		SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("timestamp", timestamp); 
        editor.commit();
		
		
		long dv = Long.valueOf(timestamp)*1000;// its need to be in milisecond
		Date df = new java.util.Date(dv);
		String date = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
		
		System.out.println("Correct date after conversion is" + date);
		
		
	}
	
	
	public void timeOnClick(){
		new TimePickerDialog(NewEvent.this, time,
		calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false).show();
	}
	
	
	TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(calendar.MINUTE, minute);
			updateTime();
		}
	};

	
	
	public class EventsAsyncTask extends AsyncTask<String, Void, Boolean> {
		
		 ContentResolver content;
	     Context mContext;
		 public EventsAsyncTask(ContentResolver content,Context context)
	        {
	              this.content = content;
	              this.mContext = context;
	          	Log.i("step0","step0");
	        }

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				
				
	            SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
				String deviceKey = sharedpreferences.getString("device-key", "");
				String deviceId = sharedpreferences.getString("device-id", "");
				String contact = sharedpreferences.getString("contact", "");
				
				System.out.println("contact = " + contact);
				
				String attendeePhoneNumbers[] = { contact, "+918754100392" };
				JSONArray mJSONArray = new JSONArray(Arrays.asList(attendeePhoneNumbers));
				String deviceUUId = Settings.Secure.getString(content, Settings.Secure.ANDROID_ID);
				int endTime = sharedpreferences.getInt("timestamp", 0);
				
				
				JSONObject jsonObject = new JSONObject();
				
				LocationManager locationManager;
		        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		        String Address = sharedpreferences.getString("AddressLine", "");
		        Address = Address + ", Chennai";
		        
		        String lat = sharedpreferences.getString("lati", "");
		        String lon= sharedpreferences.getString("longi", "");
		        
		        double lati = Double.parseDouble(lat);
		        double longi = Double.parseDouble(lon);
		        
		        System.out.println("lati = " + lati);
		        System.out.println("longi = " + longi);
		        
		        jsonObject.put("description", "coolPlace");
		        jsonObject.put("google_location_id", "1007809");
		        jsonObject.put("latitude", lati);
                jsonObject.put("longitude", longi);
                jsonObject.put("name", Address);
                
                Gson gson = new Gson();
		        String loc = gson.toJson(location);

		        
		        System.out.println("loc = " + loc);
		        
				String message = "BirthdayParty";
				String eventType = "birthday";
				int startTime = 1427975810 ;
				String userPhoneNumber = sharedpreferences.getString("user-phone-number", "");
//	            
//				double lat = location.getLatitude();
//		        double lng = location.getLongitude();
//		        
//		        System.out.println("After double lng = location.getLongitude();lat="  + lat);
	            
	            
	            
	            
	            
	            JSONObject jsonObj = new JSONObject();
	            
	            jsonObj.put("device-key", deviceKey);
	            jsonObj.put("device-id", deviceId);
	            jsonObj.put("attendee-phone-numbers", mJSONArray); 
                jsonObj.put("device-uuid", deviceUUId);
                jsonObj.put("end_time", endTime);
                jsonObj.put("location", jsonObject);
                jsonObj.put("message", message);
                jsonObj.put("event_type", eventType);
                jsonObj.put("start_time", startTime);
                jsonObj.put("user-phone-number", userPhoneNumber);
               
               
               

               //String url = "http://beehive.bee.works/api/signup_verify/";
               //String url = "http://106.51.127.58:8002/apis/v1/events/create";
               // Create the POST object and add the parameters jsonObj.toString()
               
                String url = "http://106.51.127.58:8002/apis/v1/events/create/";
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
              // Log.i("response", responseText);
               System.out.println(responseText);
               
               
               int status = response.getStatusLine().getStatusCode();
               System.out.println("status");
               System.out.println(status);
				
				/*if(status==200)
				{
					int position=0;
					//HttpEntity entity = response.getEntity();
					//String data = EntityUtils.toString(entity);
				//	System.out.print("result "+data);
					//JSONObject jObj = new JSONObject(responseText);
					jArray = new JSONArray(responseText);
					System.out.println("status1");
				
				//	System.out.println(jArray);
					System.out.println("len"+jArray.length());
					System.out.println(jArray.getJSONObject(0));
					for(int i=0;i<jArray.length();i++)
					{
						Actors actor = new Actors();
						
						JSONObject jRealObject = jArray.getJSONObject(i);
						String date=jRealObject.getString("start_time");
						String message=jRealObject.getString("message");
						String num=jRealObject.getString("user-phone-number");
						String eventid=jRealObject.getString("eventid");
						System.out.println(message);
						System.out.println(num);
						System.out.println("eventid"+eventid);
				
						
						//eveid[position]=eventid;
						
						System.out.println("eventid[" + position + "] = " + eventid);
						
						String y=date.substring(0, 4);
						
						char[] month=new char[2];
						date.getChars(5, 7, month, 0);
						System.out.println(month);
						String mo=date.substring(5, 7);
						int mon= Integer.parseInt(mo);
						String m=new DateFormatSymbols().getMonths()[mon-1];
						System.out.println(m);
						
						String d=date.substring(8, 10);
						
						String t=date.substring(11, 16);
						System.out.println(t);
						
						actor.setMonth(m);
						actor.setYear(y);
						actor.setDate(d);
						actor.setEvent(message);
						actor.setTime(t);
						actor.setOrganiser(num);
						actor.setEventid(eventid);
						
						actorsList.add(actor);
						
					}
					
					return true;
				}*/
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return false;
		}

		
		@Override
		protected void onPostExecute(Boolean result) {
			
			if(result==false)
			{
				
			}
			else
			{
				System.out.println("abc");
			}
//				ActorsAdapter adapter = new ActorsAdapter(getApplicationContext(), R.layout.row, actorsList); 
//				list.setAdapter(adapter);
//				list.setClickable(true);
//				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					/*@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {					
						// TODO Auto-generated method stub
						Locale current = getResources().getConfiguration().locale;
						System.out.println(current);
						String article_url =listData[position].postThumbUrl;
						Intent start = new Intent(MainActivity.this,Browser.class);
						start.putExtra("article_url", article_url);
						startActivity(start);
					}*/

//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int place, long arg3) {
//						
//						
//						System.out.println(actorsList.get(place).getEventid());
//						String eventi=actorsList.get(place).getEventid();
//						
//					    SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
//	                    SharedPreferences.Editor editor = sharedpreferences.edit();
//	                    editor.putString("event-id", eventi);
//	                    editor.commit();
//	                	finish();
//						startActivity(new Intent(SuperActivity.this, EventDetail.class));
//					}
				
			

			super.onPostExecute(result);
		}

	}

}
	
	


