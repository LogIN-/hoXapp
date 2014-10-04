package com.android.hoxapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AndroidCallPhone extends Activity {
	int MAX_SMS_MESSAGE_LENGTH = 160;
	
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        
	       Intent myIntent= getIntent(); // gets the previously created intent
            String sms_number = myIntent.getStringExtra("sms_number");

            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+sms_number));
                startActivity(callIntent);
            } catch (ActivityNotFoundException e) {
                Log.e("CALL_PHONE", "CALL_FAILD", e);
            }     
  
            Log.e("CALL_PHONE", "DONE");
	 }
}
