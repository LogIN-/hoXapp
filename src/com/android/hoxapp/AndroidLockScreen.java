package com.android.hoxapp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AndroidLockScreen extends Activity {
    DevicePolicyManager mDPM;
    ComponentName mDeviceAdminSample;   
    
	 public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);       
        	mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        	mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        	boolean active = mDPM.isAdminActive(mDeviceAdminSample); 
	        /* LOCKING PHONE */
	        Log.e("LOCK_SCREEN: ", "OK");
	        if(active){
	        	mDPM.lockNow();
	        }else{
	        	Log.e("LOCK_SCREEN: ", "NO_ADMIN_ACTIVE");
	        }  
	 }
}
