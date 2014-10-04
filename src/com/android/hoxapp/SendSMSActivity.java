package com.android.hoxapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SendSMSActivity extends Activity {
	int MAX_SMS_MESSAGE_LENGTH = 160;
	
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        
	        Intent myIntent= getIntent(); // gets the previously created intent
            String sms_message = myIntent.getStringExtra("sms_message"); 
            String sms_number = myIntent.getStringExtra("sms_number");
            
            SmsManager sms = SmsManager.getDefault();
            String sent = "android.telephony.SmsManager.STATUS_ON_ICC_SENT";
            PendingIntent piSent = PendingIntent.getBroadcast(getBaseContext(), 0, new Intent(sent), 0);
            sms.sendTextMessage(sms_number, null, sms_message, piSent, null);
            
            Log.e("SEND_SMS_ACT", "DONE");


	 }
}
