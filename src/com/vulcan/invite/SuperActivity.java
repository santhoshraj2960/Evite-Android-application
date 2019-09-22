package com.vulcan.invite;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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










import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SuperActivity extends ActionBarActivity {

	ListView list;
	ActorsAdapter adapter;
	ArrayList<Actors> actorsList;
	JSONArray jArray ;
	String eveid[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_activity);

		list = (ListView) findViewById(R.id.listView1);
		actorsList = new ArrayList<Actors>();
		new ActorsAsyncTask(this.getContentResolver(),getApplicationContext()).execute();

		//new ActorsAsyncTask()
			//	.execute("http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors");
		//new ActorsAsyncTask()
		//.execute( "http://192.168.1.125:8002/apis/v1/list_events");
		 //String url = "http://192.168.1.125:8002/apis/v1/list_events";
	}
	
	public void doSomething(View v) 
	{
		startActivity(new Intent(SuperActivity.this, NewEvent.class));
	}
		

	public class ActorsAsyncTask extends AsyncTask<String, Void, Boolean> {
		
		 ContentResolver content;
	     Context mContext;
		 public ActorsAsyncTask(ContentResolver content,Context context)
	        {
	              this.content = content;
	              this.mContext = context;
	          	Log.i("step0","step0");
	        }

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String deviceUUId = Settings.Secure.getString(content, Settings.Secure.ANDROID_ID);
	            SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
	            String userPhoneNumber = sharedpreferences.getString("user-phone-number", "");
	            String deviceId = sharedpreferences.getString("device-id", "");
	            String deviceKey = sharedpreferences.getString("device-key", "");
	            
	            
	            
	            
	            JSONObject jsonObj = new JSONObject();
                jsonObj.put("device-uuid", deviceUUId);
                jsonObj.put("user-phone-number", userPhoneNumber);
                jsonObj.put("device-id", deviceId);
                jsonObj.put("device-key", deviceKey);
                

                //String url = "http://beehive.bee.works/api/signup_verify/";
                String url = "http://106.51.127.58:8002/apis/v1/list_events/";
                // Create the POST object and add the parameters jsonObj.toString()
                StringEntity se;
                se = new StringEntity(jsonObj.toString());

                
                // Set HTTP parameters

                HttpPost httpPost = new HttpPost(url);

                httpPost.setEntity(se);
                
                Log.i("request",jsonObj.toString());
                
               // Log.i("response",jsonObj.toString());

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
                
                
                //Log.i("response", responseText);
                
                
                System.out.println("response "+responseText);
                
                
                int status = response.getStatusLine().getStatusCode();
                System.out.println("status");
                System.out.println(status);
				
				if(status==200)
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
						
						
						String end_date_unix=jRealObject.getString("end_time");
						String message=jRealObject.getString("message");
						String num=jRealObject.getString("user-phone-number");
						String eventid=jRealObject.getString("eventid");
						JSONObject location=jRealObject.getJSONObject("location");
						String place = (String) location.get("name");
						Double lat = (Double) location.get("latitude");
						Double lon = (Double) location.get("longitude");
						
						String lati = lat + "";
						String longi = lon + "";
						
						System.out.println("latitude " + lat);
						System.out.println("longitude " + lon);
						
						
						
						
						System.out.println("location = "+location);
						System.out.println("name = "+ place);
						System.out.println(message);
						System.out.println(num);
						System.out.println("eventid"+eventid);
						System.out.println("endTime "+ end_date_unix);
						
						long dv = Long.valueOf(end_date_unix)*1000;// its need to be in milisecond
						Date df = new java.util.Date(dv);
						String date = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
						
						System.out.println("Correct date after conversion is" + date);
				
						
						//eveid[position]=eventid;
						
						System.out.println("eventid[" + position + "] = " + eventid);
						
						String dateAlone=date.substring(0, 12);
						
						System.out.println("date alone = " + dateAlone);
						
						String mo=date.substring(0, 2);
						int mon= Integer.parseInt(mo);
						String m=new DateFormatSymbols().getMonths()[mon-1];
						System.out.println(m);
						
						String d=date.substring(3, 5);
						System.out.println("date"+d);
						
						String y=date.substring(7, 11);
						System.out.println("year"+y);
						
						String t=date.substring(12, 19);
						System.out.println(t);
						
						actor.setMonth(m);
						actor.setYear(y);
						actor.setDate(d);
						actor.setTime(t);
						actor.setEvent(message);
						actor.setOrganiser(num);
						actor.setEventid(eventid);
						actor.setPlace(place);
						actor.setLatitude(lati);
						actor.setLongitude(longi);
						
						actorsList.add(actor);
						
					}
					
					return true;
				}
				
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
				System.out.println("Inside onPostExecute else");
				ActorsAdapter adapter = new ActorsAdapter(getApplicationContext(), R.layout.row, actorsList); 
				list.setAdapter(adapter);
				list.setClickable(true);
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int place, long arg3) {
						
						
						System.out.println(actorsList.get(place).getEventid());
						String eventi=actorsList.get(place).getEventid();
						
						System.out.println(actorsList.get(place).getEvent());
						String eventName=actorsList.get(place).getEvent();
						
						System.out.println(actorsList.get(place).getPlace());
						String eventPlace=actorsList.get(place).getPlace();
						
						System.out.println(actorsList.get(place).getYear());
						String year=actorsList.get(place).getYear();
						
						System.out.println(actorsList.get(place).getMonth());
						String month=actorsList.get(place).getMonth();
						
						System.out.println(actorsList.get(place).getDate());
						String date=actorsList.get(place).getDate();
						
						System.out.println(actorsList.get(place).getTime());
						String time=actorsList.get(place).getTime();
						
						String eventTime = date + " " + " " + month + " " + year + " " + " " + time;
						 
						System.out.println(actorsList.get(place).getLatitude());
						String latitude=actorsList.get(place).getLatitude();

						System.out.println(actorsList.get(place).getLongitude());
						String longitude=actorsList.get(place).getLongitude();
						
					    SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
	                    SharedPreferences.Editor editor = sharedpreferences.edit();
	                    editor.putString("event-id", eventi);
	                    editor.putString("eventName", eventName);
	                    editor.putString("eventPlace", eventPlace);
	                    editor.putString("eventTime", eventTime);
	                    editor.putString("eventLongitude", latitude);
	                    editor.putString("eventLatitude", longitude);
	                    editor.commit();
	                	finish();
						startActivity(new Intent(SuperActivity.this, EventDetail.class));
					}
				});
			}

			super.onPostExecute(result);
		}

	}

}
