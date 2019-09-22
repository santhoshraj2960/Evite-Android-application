package com.vulcan.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassInvalidStateException;

import java.util.ArrayList;
import java.util.List;

public class FingerPrint extends ActionBarActivity{

    private SpassFingerprint mSpassFingerprint;
    private Spass mSpass;
    private Context mContext;
    private ListView mListView;
    private List<String> mItemArray = new ArrayList<String>();
    private ArrayAdapter<String> mListAdapter;
    private boolean onReadyIdentify = false;
    private boolean onReadyEnroll = false;
    boolean isFeatureEnabled = false;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finger_print);
		initFingerPrint();
    }
   /*public FingerPrint(Context mContext)
    {
    	this.mContext=mContext;
      	System.out.println("Context Cons");
    }
   public FingerPrint()
   {
   	super();
   	System.out.println("Empty Cons");
   }
  */
    public void initFingerPrint()
    {
        System.out.println("Inside init()");
        mSpassFingerprint = new SpassFingerprint(FingerPrint.this);
        //mSpassFingerprint = new SpassFingerprint(this);
        mSpass = new Spass();
        try {
            mSpass.initialize(this);
            isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
            System.out.println("Spass not initialized due to: " + e.getType());
        }
        identifyFingerPrint();
    }

    private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener() {
    	 @Override
         public void onFinished(int eventStatus) {
             onReadyIdentify = false;
             int FingerprintIndex = 0;
             try {
                 FingerprintIndex = mSpassFingerprint.getIdentifiedFingerprintIndex();
             } catch (IllegalStateException ise) {
                 System.out.println(ise.getMessage());

             }
             if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
            	 
            	 startActivity(new Intent(FingerPrint.this, SuperActivity.class));
                 //sendMessage(sessionID);
                 /*
                 new AlertDialog.Builder(FingerPrint.this)
                         .setTitle("Hey!!")
                         .setMessage("You have been identified")
                         .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 // continue with delete
                                 finish();
                             }
                         }).setIcon(android.R.drawable.ic_dialog_alert)
                         .show();
                 */
             } else if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) {
                 System.out.println("onFinished() : Password authentification Success");
             } else {
                 System.out.println("onFinished() : Authentification Fail for identify");
//                 TextView name = (TextView) findViewById(R.id.statusText);
//                 name.setText("Failed");
//                 loginEvent.setStatus(LoginEvent.STATUS_FAILED);
//                 loginEvent.setLoginTime(new Date());
//                 createLoginEvent(loginEvent);
             }
         }

        @Override
        public void onReady() {
            System.out.println("identify state is ready");
            //mSpassFingerprint.setDialogBgTransparency(4);
           // mSpassFingerprint. setDialogTitle("FingerPrint Authenticity", 5);
            int a=5;
            //mSpassFingerprint.setDialogIcon(Integer.toString(R.drawable.finger));
        }

        @Override
        public void onStarted() {
            System.out.println("User touched fingerprint sensor!");
        }
    };

    private SpassFingerprint.RegisterListener mRegisterListener = new SpassFingerprint.RegisterListener() {

        @Override
        public void onFinished() {
            onReadyEnroll = false;
            System.out.println("RegisterListener.onFinished()");

        }
    };

    private static String getEventStatusName(int eventStatus) {
        switch (eventStatus) {
            case SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS:
                return "STATUS_AUTHENTIFICATION_SUCCESS";
            case SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS:
                return "STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS";
            case SpassFingerprint.STATUS_TIMEOUT_FAILED:
                return "STATUS_TIMEOUT";
            case SpassFingerprint.STATUS_SENSOR_FAILED:
                return "STATUS_SENSOR_ERROR";
            case SpassFingerprint.STATUS_USER_CANCELLED:
                return "STATUS_USER_CANCELLED";
            case SpassFingerprint.STATUS_QUALITY_FAILED:
                return "STATUS_QUALITY_FAILED";
            case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
                return "STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE";
            case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
            default:
                return "STATUS_AUTHENTIFICATION_FAILED";
        }

    }

    public void hasRegisteredFinger() {

        int RegisteredFingerPrint;
        SparseArray s;

        try{
            if(!isFeatureEnabled){
                System.out.println("Fingerprint Service is not supported in the device");
            } else {
                boolean hasRegisteredFinger = mSpassFingerprint.hasRegisteredFinger();
                System.out.println("hasRegisteredFinger() = " + hasRegisteredFinger);
                if (mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT_UNIQUE_ID)){
                    s = mSpassFingerprint. getRegisteredFingerprintUniqueID ();
                }

            }
        } catch (UnsupportedOperationException e){
            System.out.println("Fingerprint Service is not supported in the device");
        }
    }



    
    
    
    public void identifyFingerPrint() {

        try {
            if (!isFeatureEnabled) {
                System.out.println("Fingerprint Service is not supported in the device");
            } else {
                if (!mSpassFingerprint.hasRegisteredFinger()) {
                    System.out.println("Please register finger first");
                } else {
                    if (onReadyIdentify == false) {
                        try {
                            onReadyIdentify = true;
                            //mSpassFingerprint.startIdentify(listener);
                            mSpassFingerprint.startIdentifyWithDialog(this, listener, false);
                            System.out.println("Please identify finger to verify you");
                        } catch (SpassInvalidStateException ise) {
                            onReadyIdentify = false;
                            if (ise.getType() == SpassInvalidStateException.STATUS_OPERATION_DENIED) {
                                System.out.println("Exception: " + ise.getMessage());
                            }
                        } catch (IllegalStateException e) {
                            onReadyIdentify = false;
                            System.out.println("Exception: " + e);
                        }
                    } else {
                        System.out.println("Please cancel Identify first");
                    }
                }
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Fingerprint Service is not supported in the device");
        }
    }
    
    

    public void identifyDialogWithoutPW(View v) {

        try{
            if(!isFeatureEnabled){
                System.out.println("Fingerprint Service is not supported in the device");
            } else {
                if(!mSpassFingerprint.hasRegisteredFinger()){
                    System.out.println("Please register finger first");
                } else {
                    if(onReadyIdentify == false){
                        onReadyIdentify = true;
                        mSpassFingerprint.startIdentifyWithDialog(this, listener, true);
                        System.out.println("Please identify finger to verify you");
                    } else {
                        System.out.println("Please cancel Identify first");
                    }
                }
            }
        } catch (UnsupportedOperationException e){
            System.out.println("Fingerprint Service is not supported in the device");
        }
    }

    public void cancelIdentify(View v) {

        try{
            if(!isFeatureEnabled){
                System.out.println("Fingerprint Service is not supported in the device");
            } else {
                if (onReadyIdentify == true) {
                    try {
                        mSpassFingerprint.cancelIdentify();
                        System.out.println("cancelIdentify is called");
                    } catch (IllegalStateException ise) {
                        System.out.println(ise.getMessage());
                    }
                    onReadyIdentify = false;
                } else {
                    System.out.println("Please request Identify first");
                }
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Fingerprint Service is not supported in the device");
        }
    }
}
