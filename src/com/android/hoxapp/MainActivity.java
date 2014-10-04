package com.android.hoxapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
    	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
        /*
        Alarm alarm = new Alarm();
    	alarm.SetAlarm(getBaseContext());    
    	*/
    	
        // Check if User has entered password //
		SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
        String password = settings.getString("APP_PASS", null);
        
        TelephonyManager telephoneMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String detect_phone_number = telephoneMgr. getSubscriberId();
        
        
        
        if (password == "" || password == null){      
        	
        	// If Admin_Pass nt set application is runed first time
	        AlertDialog.Builder alert = new AlertDialog.Builder(this);	
	        alert.setTitle(R.string.main_pass_title);
	        alert.setMessage(R.string.main_pass_desc);
	
	        // Set an EditText view to get user input 
	        final EditText input = new EditText(this);	    	
	        input.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_VARIATION_PASSWORD); 
	        alert.setView(input);
	
	        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        	String password = input.getText().toString().trim();
	        	
	        	if(password.trim().length() >= 4){	        	
		        	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
		        	SharedPreferences.Editor editor = settings.edit();
		        	editor.putString("APP_PASS", password);
		        	editor.putString("APP_IDENT", "hoXapp");
		        	editor.putString("GPS_SMS", "ON");
		        	editor.putString("GPS_NET", "ON");
		        	editor.putString("USER_ADMIN_NUMBER", detect_phone_number);
		        	
		        	editor.putString("DEVICE_ADMIN_CAN_DEACTIVATE", "ON");
		        	editor.putString("DEVICE_ADMIN_CAN_UNINSTALL", "ON");
		        	editor.putString("DEVICE_ADMIN_CAN_CLEARDATA", "ON");
		        	
		        	editor.commit();
	        	}else{
	             	String msg_pass_weak = getBaseContext().getResources().getString(R.string.msg_pass_weak);
	                showToast(getBaseContext(), msg_pass_weak);
		        	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
		        	SharedPreferences.Editor editor = settings.edit();
		        	editor.putString("APP_PASS", "6322");
		        	editor.putString("APP_IDENT", "hoXapp");
		        	editor.putString("GPS_SMS", "ON");
		        	editor.putString("GPS_NET", "ON");
		        	editor.putString("DEVICE_ADMIN_CAN_DEACTIVATE", "ON");
		        	editor.putString("DEVICE_ADMIN_CAN_UNINSTALL", "ON");
		        	editor.putString("DEVICE_ADMIN_CAN_CLEARDATA", "ON");
		        	editor.putString("USER_ADMIN_NUMBER", detect_phone_number);
		        	editor.commit();
	        	}

	          }
	        });
	
	        alert.show();
        }
        
        /* CHECK IF HOXAPP IS OFFLINE OR ONLINE */
        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);   	
        Button deviceadmin = (Button) findViewById(R.id.deviceadmin);        
        Button startserver = (Button) findViewById(R.id.startserver);
        Button about = (Button) findViewById(R.id.help);
        Button configuration = (Button) findViewById(R.id.config);
        
        configuration.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), hoxappconfig.class);
		        v.getContext().startActivity(i);  
            } 
        });
        shutdown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Log.e("SIM_DETECTED_NUMBER", detect_phone_number);
            }
        });
        deviceadmin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), DeviceAdmin.class);
		        v.getContext().startActivity(i); 
            }
        });
        
         startserver.setOnClickListener(new OnClickListener() {         
            public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), AndroidHTTPD.class);
		        v.getContext().startActivity(i); 

            }
        });
        
        about.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                //set up dialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle(R.string.help_description_title);
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!
 
                //set up text
                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
                text.setText(R.string.help_description);
 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.Button01);
                button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                		dialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it    
                dialog.show();
            }
        });
        
    }

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
