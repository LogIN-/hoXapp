package com.android.hoxapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.android.hoxapp.AndroidGPSTrackingActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * The class is called when SMS is received.
 */
public class SMSReceiver extends BroadcastReceiver { 

	int abort = 1;
	String USER_ADMIN_NUMBER = "";
	String GPS_URL = "NO_URL";
	String password_sms = "0";
	String action = "";
	String appIdent = "";
	String php_location = "NOPHP";
	String detect_phone_number = "";
	
   public void onReceive(Context context, Intent intent) {
    	// Get Number user Dailed if correct enter in application
        Bundle bundle = intent.getExtras();
        String phoneNubmer = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        // GET SIM INDENT to see if SIM is Changed
        TelephonyManager telephoneMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        detect_phone_number = telephoneMgr. getSubscriberId();
        

        
        // Get Shared Prefs Defines
		SharedPreferences settings = context.getSharedPreferences(MainActivity.class.getName(), 0);	

		String FIRST_RUN = settings.getString("FIRST_RUN", null);
        String APP_PASS = settings.getString("APP_PASS", null);
        String APP_IDENT = settings.getString("APP_IDENT", null);
        String GPS_SMS = settings.getString("GPS_SMS", null);
        String GPS_NET = settings.getString("GPS_NET", null);
        String ADMIN_NUMBER = settings.getString("ADMIN_NUMBER", null);
        
        
        if(FIRST_RUN == null){        	
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("FIRST_RUN", "1");
			editor.putString("USER_ADMIN_NUMBER", detect_phone_number);
			editor.putString("PHP_LOCATION", php_location);
	    	editor.commit();
	    	try {
				CopyRawToFile(context);
			} catch (IOException e) {
				Log.e("COPY_PHP_ERROR", "IOException");
			}
        }else{
        	USER_ADMIN_NUMBER = settings.getString("USER_ADMIN_NUMBER", null);
        }
	        
        if(USER_ADMIN_NUMBER.equals(detect_phone_number) || FIRST_RUN == null){
        	Log.e("SIM_OK", USER_ADMIN_NUMBER + " - " + detect_phone_number);
        }else{
        	Log.e("SIM_NOT_OK",USER_ADMIN_NUMBER + " - " + detect_phone_number);
        	abort = 2;
        }
        if (phoneNubmer == "" || phoneNubmer == null){  
		    																							
		    Object messages[] = (Object[]) bundle.get("pdus");
		    SmsMessage smsMessage[] = new SmsMessage[messages.length];
		    for (int n = 0; n < messages.length; n++) {
		        smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		    }
		    
		    String sms_message = smsMessage[0].getMessageBody();
		    String sms_number = smsMessage[0].getOriginatingAddress();
		    String sms_number_sender = smsMessage[0].getDisplayOriginatingAddress();
		    Long Timestamp = smsMessage[0].getTimestampMillis();
		    String timestamp = String.valueOf(Timestamp);
		    
		    // Set Admin_Number If action "CALL" to intercept
		    SharedPreferences.Editor editor = settings.edit();
        	editor.putString("ADMIN_NUMBER", sms_number);
        	editor.commit();
        	
        	/* ========================== */
		    /* Split SMS and GET Commands */
		    /* ========================== */		    
		    String[] words = sms_message.split(" ");
		    
		    try {
		    	appIdent = words[0].trim();
		    	Log.e("SMS_MESSAGE_APPIDENT",words[0]);
		    } catch (ArrayIndexOutOfBoundsException e) {
		    	Log.e("SMS_MESSAGE_SPLIT","ERROR_APP_IDENT");		    	
		    }	    
		    
		    if(APP_IDENT.equals(appIdent) && appIdent.equals("hoXapp")){
			    
			    try {
			    	password_sms = words[1].trim();
			    	Log.e("SMS_MESSAGE_PASS",words[1]);
			    } catch (ArrayIndexOutOfBoundsException e) {
			    	Log.e("SMS_MESSAGE_SPLIT","ERROR_SMS_PASS");
			    	password_sms = "";			    	
			    }
			    try {
			    	action = words[2].trim();
			    	Log.e("SMS_MESSAGE_ACTION:", words[2]);
			    } catch (ArrayIndexOutOfBoundsException e) {
			    	Log.e("SMS_MESSAGE_SPLIT","ERROR_ACTION");
			    	action = "";		    	
			    }	    	
			    
			    /* ======================= */
			    /* Additional SMS Commands */
			    /* ======================= */
			    if(action.equals("GPS")){
			    	String gps_url = words[3];	
		    		if(gps_url.trim().length() >= 5){
			    		GPS_URL = gps_url;
			    	}
			    }
		    }	    
		    
		    /* ========================================= */
		    /* Check if hoXapp Ident is set and correct! */
		    /* ========================================= */
		    if(!APP_IDENT.equals("hoXapp") && APP_IDENT != null && !APP_IDENT.equals("") && APP_IDENT.equals(appIdent)){
	        	if(password_sms.equals(APP_PASS)){
	        		abort = 0;
	        		Log.e("SMS_ACTION","PASS_OK");
	        	}else{
	        		abort = 1;
	        		Log.e("SMS_ACTION","PASS_NOT_OK" + password_sms + "-" + APP_PASS);
	        	}
		    }else if(appIdent.equals("hoXapp") && APP_IDENT.equals("hoXapp")){
	        	if(password_sms.equals(APP_PASS)){
	        		abort = 0;
	        		Log.e("SMS_ACTION","PASS_OK");
	        	}else{
	        		abort = 1;
	        		Log.e("SMS_ACTION","PASS_NOT_OK" + password_sms + "-" + APP_PASS);
	        	}
		    }else{
		    	abort = 1;
		    	Log.e("SMS_ACTION","WRONG_WORD" + appIdent);
		    }
		    
		    if(abort == 0){        
		        try {
		        	abortBroadcast();
			    } catch (Exception e) {
			    	Log.e("SMS_ACTION", "Canot_Abort");
			    }
		    
		        if(action.equals("GPS")){
		        	Log.e("SMS_ACTION", "ENABLE_GPS");
			        Intent i = new Intent(context,AndroidGPSTrackingActivity.class);
			        i.putExtra("sms_message",sms_message);
			        i.putExtra("sms_number",sms_number);
			        i.putExtra("sms_number_sender",sms_number_sender);
			        i.putExtra("timestamp",timestamp);	
			        i.putExtra("GPS_SMS",GPS_SMS);	
			        i.putExtra("GPS_NET",GPS_NET);	
			        if(GPS_URL.equals(words[3])){
			        	i.putExtra("GPS_URL",GPS_URL);
			        }
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 
		        }else if(action.equals("ALARM")){
		        	Log.e("SMS_ACTION", "START_ALARM");
		        	Intent i = new Intent(context,AndroidAlarmTracking.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 	        	
		        }else if(action.equals("WIPE")){
		        	Log.e("SMS_ACTION", "WIPE_SDCARD");
		        	Intent i = new Intent(context,AndroidwipeMemoryCard.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 	        	
		        }else if(action.equals("WIPEALL")){
		        	Log.e("SMS_ACTION", "WIPE_ALL");
		        	Intent i = new Intent(context,AndroidwipeAll.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 	        	
		        }else if(action.equals("LOCK")){
		        	Log.e("SMS_ACTION", "LOCK_SCREEN");
		        	Intent i = new Intent(context,AndroidLockScreen.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 	        	
		        }else if(action.equals("CALL")){
		        	Log.e("SMS_ACTION", "CALL_PHONE");
		        	Intent i = new Intent(context,AndroidCallPhone.class);
		        	i.putExtra("sms_number",sms_number);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        context.startActivity(i); 	        	
		        }else{
		        	Log.e("SMS_ACTION", "ERROR -" + action);
		        }
		    }
        }else if(abort == 2){
        	Log.e("SMS_ACTION", "PHONE_SIM_CHANGED");
	        Intent i = new Intent(context,AndroidGPSTrackingActivity.class);
	        i.putExtra("sms_message", "SIM CARD CHANGED!");
	        i.putExtra("sms_number", USER_ADMIN_NUMBER);
	        i.putExtra("sms_number_sender", detect_phone_number);
	        i.putExtra("GPS_SMS","ON");	
	        i.putExtra("GPS_NET","ON");	
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
	        context.startActivity(i); 
	        /*
	        // After User notifiction set current admin number
	        SharedPreferences.Editor editor = settings.edit();
	        editor.putString("USER_ADMIN_NUMBER", detect_phone_number);
        	editor.commit();
        	*/
        }else{
        	// Check if hoXapp is started first time
        	if (APP_PASS == "" || APP_PASS == null){ 
	        	if (phoneNubmer.equals("6322")){
	        		setResultData(null);
		        	Intent i = new Intent(context,MainActivity.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        context.startActivity(i); 
	        	}        		
        	}else{
        	// If not  first time compare to user password
	        	if (phoneNubmer.equals(APP_PASS)){
	        		setResultData(null);
		        	Intent i = new Intent(context,MainActivity.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        context.startActivity(i); 
			// If Action Call Phone intercept call 
	        	}else if(phoneNubmer.equals(ADMIN_NUMBER)){
	        		Log.e("CALL_ACTION", "CALL INTERCEPTED");	        		
	        	}
        	}
        }
    }
   

	private void CopyRawToFile(Context context) throws IOException {

		String filesdir = context.getFilesDir().getAbsolutePath()+"/";
		
		SharedPreferences settings = context.getSharedPreferences(MainActivity.class.getName(), 0);	
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("PHP_LOCATION", filesdir + "/php");
		// editor.putString("VNC_LOCATION", filesdir + "/vncserver");
		// editor.putString("VNC_CLIENTS_LOCATION", filesdir + "/webclients.zip");
		editor.commit();

		// copyBinary(R.raw.webclients, filesdir + "/webclients.zip", context);
		// copyBinary(R.raw.vncserver, filesdir + "/vncserver", context);
		copyBinary(R.raw.php, filesdir + "/php", context);

		try {
			// ResLoader.unpackResources(R.raw.webclients,context,filesdir);
			// ResLoader.unpackResources(R.raw.vncserver,context,filesdir);
			ResLoader.unpackResources(R.raw.php,context,filesdir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyBinary(int id,String path, Context context){
		Log.e("COPY_BINARY", "copy -> " + path);
		try {
		InputStream ins = context.getResources().openRawResource(id);
		int size = ins.available();
		
		// Read the entire resource into a local byte buffer.
		byte[] buffer = new byte[size];
		ins.read(buffer);
		ins.close();
		
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(buffer);
		fos.close();
		}
		catch (Exception e)
		{
			Log.e("COPY_BINARY", "public void createBinary() error! : " + e.getMessage());
		}
	}


}
