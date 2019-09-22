package com.vulcan.invite;

import android.app.ProgressDialog;



import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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



public class Dummy extends ActionBarActivity {
    ProgressDialog progress;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
    }


   
    public void sendVerificationCode(View e)
    {
        progress = ProgressDialog.show(this, "Evite",
                "Verifying Your Code...", true);
        new VerificationTask(this.getContentResolver()).execute();
    }

    class VerificationTask extends AsyncTask<String, Void, String> {

        ContentResolver content;

        public VerificationTask(ContentResolver content) {
            this.content = content;
        }
        
        String deviceName= Build.MODEL.toString();
        String deviceModel= Build.MANUFACTURER.toString();


        @Override
        public String doInBackground(String... arg) {
            String verifyCode = ((TextView) findViewById(R.id.verifyCode)).getText().toString();
            String deviceId = Settings.Secure.getString(this.content, Settings.Secure.ANDROID_ID);
            SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            String userPhoneNumber = sharedpreferences.getString("user-phone-number", "");
            
           
            String pushNotifyId=sharedpreferences.getString("push-notify-code", "");

            if (userPhoneNumber.equals("")) {
                return "noCode";
            }
            
            
            
           
            Log.i("Phone Number", userPhoneNumber + "");
            Log.i("device id", deviceId);
            try {

                JSONObject jsonObj = new JSONObject();
               // jsonObj.put("device-model",deviceModel );
                jsonObj.put("device-name","samsung");
                jsonObj.put("device-type","android");
                jsonObj.put("verify-number", verifyCode);
                jsonObj.put("push-notify-code", pushNotifyId);
                jsonObj.put("user-phone-number", userPhoneNumber);
                jsonObj.put("device-uuid", deviceId);

                Log.i("json Obj","hi");
                String url = "http://106.51.127.58:8002/apis/v1/verify_device/";
                // Create the POST object and add the parameters jsonObj.toString()
                StringEntity se;
                se = new StringEntity(jsonObj.toString());
                Log.i("json Obj",jsonObj.toString());

                // Set HTTP parameters

                HttpPost httpPost = new HttpPost(url);

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                Log.i("response", jsonObj.toString());

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
                Log.i("response", responseText);
                JSONObject responseJson = new JSONObject(responseText);
                Log.i("response", responseText);
                int status = response.getStatusLine().getStatusCode();
                if(status==200)
                {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("hasPhoneNumber", true);
                        editor.putString("device-key", responseJson.get("device-key").toString());
                        editor.putString("device-id", responseJson.get("device-id").toString());
                        editor.commit();
                      //Remove the comment in the following line and add comment to the line following it when you have a fingerPrint Device
                		startActivity(new Intent(Dummy.this, FingerPrint.class));
                		//startActivity(new Intent(Dummy.this, SuperActivity.class));
                        
                        finish();
                        progress.dismiss();
                        return "";
                    } else {
                        return "invalid";
                    }
                
            } catch (Exception er) {
                Log.e("HTTP", "Error in http connection " + er.toString());
            }
            return "error";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            if(s.equals("invalid"))
                Toast.makeText(getApplicationContext(), "Invalid Verification Code...", Toast.LENGTH_LONG).show();
            else if(s.equals("error"))
                Toast.makeText(getApplicationContext(), "Network Error Please try again Later...", Toast.LENGTH_LONG).show();
            else if(s.equals("noCode"))
                Toast.makeText(getApplicationContext(), "Please Enter the Verification Code First...", Toast.LENGTH_LONG).show();
        }
    }
}
