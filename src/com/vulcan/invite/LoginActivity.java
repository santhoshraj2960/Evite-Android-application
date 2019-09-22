package com.vulcan.invite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;



public class LoginActivity extends ActionBarActivity {

	
	static final String SENDER_ID = "339321988912";
    String regId = "";
    ProgressDialog progress;
	public String userPhoneNumber;
	public String userNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  
        GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		 regId = GCMRegistrar.getRegistrationId(LoginActivity.this);

 		if (regId.equals("")) {
 			GCMRegistrar.register(LoginActivity.this, SENDER_ID);
 		}
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        //startActivity(new Intent(this,MainActivity.class));
        System.out.println(sharedpreferences.getBoolean("hasPhoneNumber", true));
        //boolean t=true;
        //if()
        if(sharedpreferences.getBoolean("hasPhoneNumber", false))
        //if(true)
        {
        	finish();
    		//Remove the comment in the following line and add comment to the line following it when you have a fingerPrint Device
    		//startActivity(new Intent(LoginActivity.this, FingerPrint.class));
    		startActivity(new Intent(LoginActivity.this, SuperActivity.class));
        	String message = "No you don't have FingerPrint Sensor...";
        	if(sharedpreferences.getBoolean("hasPhoneNumber", false)) {
        		System.out.println("Inside IF");
        		finish();
        		//Remove the comment in the following line and add comment to the line following it when you have a fingerPrint Device
        		startActivity(new Intent(LoginActivity.this, FingerPrint.class));
        		//startActivity(new Intent(LoginActivity.this, SuperActivity.class));
                message = "Yes have FingerPrint Sensor...";
          }
        	else {
        		new AlertDialog.Builder(this)
        			.setTitle("Sorry")
        			.setMessage("" + message)
        			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        						// continue with delete
        						finish();
        					}
        			}).setIcon(android.R.drawable.ic_dialog_alert)
        			.show();
        		}
        	
            finish();
           // System.out.println("Going inside fingerPrint");
            //startActivity(new Intent(LoginActivity.this, FingerPrint.class));
        }
        Log.i("click2","click2");
       
    }


    public void registerMe(View e)
    {
    	Log.i("click","click");
    	progress = ProgressDialog.show(this, "Evite",
                "Registering the device...", true);
        new LoginTask(this.getContentResolver(),getApplicationContext()).execute();
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        ContentResolver content;
        Context mContext;
        public LoginTask(ContentResolver content,Context context)
        {
              this.content = content;
              this.mContext = context;
          	Log.i("step0","step0");
        }

        @Override
        public String doInBackground(String... arg)
        {
        	Log.i("step1","step1");
            userPhoneNumber = ((TextView) findViewById(R.id.phoneNumber)).getText().toString();
            String deviceId = Settings.Secure.getString(content, Settings.Secure.ANDROID_ID);

            if(userPhoneNumber.equals("")) {
                return "noPhone";
            }
            Log.i("Phone Number",userPhoneNumber+"");
            Log.i("device id",deviceId);
            try {
               // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                //StrictMode.setThreadPolicy(policy);

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("country-code", "IN");
                jsonObj.put("device-uuid", deviceId);
                jsonObj.put("user-phone-number", userPhoneNumber);
                jsonObj.put("verify-type", "text");

                //String url = "http://beehive.bee.works/api/signup_verify/";
                String url = "http://106.51.127.58:8002/apis/v1/submit_phonenumber/";
                // Create the POST object and add the parameters jsonObj.toString()
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
                userNum= responseJson.get("user-phone-number").toString();
                Log.i("user-PhoneNumber", userNum);
                Log.i("response", responseText);
                
                int status = response.getStatusLine().getStatusCode();
                System.out.println("status");
                System.out.println(status);
                
                if(status==200)
                {

                    SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("user-phone-number", userNum);
                    editor.commit();
                	finish();
                	startActivity(new Intent(LoginActivity.this, Dummy.class));
                	progress.dismiss();
                	
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
