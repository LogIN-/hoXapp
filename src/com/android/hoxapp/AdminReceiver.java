package com.android.hoxapp;

import com.android.hoxapp.R;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {

    static SharedPreferences getSamplePreferences(Context context) {
        return context.getSharedPreferences(AdminReceiver.class.getName(), 0);
    }

    static String PREF_PASSWORD_QUALITY = "password_quality";
    static String PREF_PASSWORD_LENGTH = "password_length";
    static String PREF_MAX_FAILED_PW = "max_failed_pw";
    
    
    
    
    
   

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
    	String msg_onEnabled = context.getResources().getString(R.string.msg_onEnabled);
        showToast(context, msg_onEnabled);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
    	
    	SharedPreferences settings = context.getSharedPreferences(MainActivity.class.getName(), 0);
        String DEVICE_ADMIN_CAN_DEACTIVATE = settings.getString("DEVICE_ADMIN_CAN_DEACTIVATE", null);
        if(DEVICE_ADMIN_CAN_DEACTIVATE.equals("ON")){
	    	Intent startMain = new Intent(Intent.ACTION_MAIN);
	        startMain.addCategory(Intent.CATEGORY_HOME);
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(startMain);
	        return "OOPS!";
        }else{
	    	String msg_char_onDisable = context.getResources().getString(R.string.msg_char_onDisable);
	        return msg_char_onDisable;
        }
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
    	String msg_onDisabled = context.getResources().getString(R.string.msg_onDisabled);
        showToast(context, msg_onDisabled);
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
    	String msg_onPasswordChanged = context.getResources().getString(R.string.msg_onPasswordChanged);
        showToast(context, msg_onPasswordChanged);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
    	String msg_onPasswordFailed = context.getResources().getString(R.string.msg_onPasswordFailed);
        showToast(context, msg_onPasswordFailed);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        String msg_onPasswordSucceeded = context.getResources().getString(R.string.msg_onPasswordSucceeded);
        showToast(context, msg_onPasswordSucceeded);
    }
}
