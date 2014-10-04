package com.android.hoxapp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AndroidwipeAll extends Activity {
	
    DevicePolicyManager mDPM;
    ComponentName mDeviceAdminSample;
    
    
	 public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);   
       
        	mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        	mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        	boolean active = mDPM.isAdminActive(mDeviceAdminSample); 
        	
        	/* WIPING SD CARD */
        	Log.e("WIPE_ALL: ", "SD_CARD");
        	Intent i = new Intent(getBaseContext(),AndroidwipeMemoryCard.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
	        getBaseContext().startActivity(i);
	        /* WIPING PHONE */
	        Log.e("WIPE_ALL: ", "PHONE");
	        if(active){
	        	mDPM.wipeData(0);
	        }else{
	        	Log.e("WIPE_ALL_ACTIVE: ", "NO_ADMIN_ACTIVE");
	        }  
	 }
}
