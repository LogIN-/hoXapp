package com.android.hoxapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Example of a do-nothing admin class.  When enabled, it lets you control
 * some of its policy and reports when there is interesting activity.
 */
public class DeviceAdmin  extends Activity{
	
	static SharedPreferences getSamplePreferences(Context context) {
	    return context.getSharedPreferences(AdminReceiver.class.getName(), 0);
	}
        static final int RESULT_ENABLE = 1;

        DevicePolicyManager mDPM;
        ActivityManager mAM;
        ComponentName mDeviceAdminSample;

        Button mEnableButton;
        Button mDisableButton;
        Button mSetPasswordButton;
        
        ToggleButton ButtonDeactivateOFF;
        ToggleButton ButtonUnistallOFF;
        ToggleButton ButtoncleardataOFF;
        
        String DeactivateValue;
        /*
        String UnistallValue;
        String ClearDataValue;
        */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);

            setContentView(R.layout.device_admin);
            
            mSetPasswordButton = (Button)findViewById(R.id.set_password);
            mSetPasswordButton.setOnClickListener(mSetPasswordListener);

            // Watch for button clicks.
            mEnableButton = (Button)findViewById(R.id.enable);
            mEnableButton.setOnClickListener(mEnableListener);
            
            mDisableButton = (Button)findViewById(R.id.disable);
            mDisableButton.setOnClickListener(mDisableListener);
            
            ButtonDeactivateOFF = (ToggleButton)findViewById(R.id.ButtonDeactivateOFF);
            /*
            ButtonUnistallOFF = (ToggleButton)findViewById(R.id.ButtonUnistallOFF);
            ButtoncleardataOFF = (ToggleButton)findViewById(R.id.ButtoncleardataOFF);
            */
            
            updateButtonStates();
            
            ButtonDeactivateOFF.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	DeactivateValue = (String) ButtonDeactivateOFF.getText().toString();
                	
                	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString("DEVICE_ADMIN_CAN_DEACTIVATE", DeactivateValue);
                	editor.commit();
                	Log.e("DEVICE_ADMIN_CAN_DEACTIVATE", DeactivateValue);
                	updateButtonStates();

                } 
            });
            /*
            ButtonUnistallOFF.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	UnistallValue = (String) ButtonUnistallOFF.getText().toString();
                	
                	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString("DEVICE_ADMIN_CAN_UNINSTALL", UnistallValue);
                	editor.commit();
                	Log.e("DEVICE_ADMIN_CAN_UNINSTALL", UnistallValue);
                	updateButtonStates();

                } 
            });
            ButtoncleardataOFF.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	ClearDataValue = (String) ButtoncleardataOFF.getText().toString();
                	
                	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString("DEVICE_ADMIN_CAN_CLEARDATA", ClearDataValue);
                	editor.commit();
                	Log.e("DEVICE_ADMIN_CAN_CLEARDATA", ClearDataValue);
                	updateButtonStates();

                } 
            });
            */

        }

        void updateButtonStates() {
            boolean active = mDPM.isAdminActive(mDeviceAdminSample);
            if (active) {
                mEnableButton.setEnabled(false);
                mDisableButton.setEnabled(true);
            } else {
                mEnableButton.setEnabled(true);
                mDisableButton.setEnabled(false);
            }
            SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
            DeactivateValue = settings.getString("DEVICE_ADMIN_CAN_DEACTIVATE", null);
    		if (DeactivateValue.equals("ON")){ 
    			ButtonDeactivateOFF.setChecked(true);
    		}else{
    			ButtonDeactivateOFF.setChecked(false);
    		}
    		/*
    		UnistallValue = settings.getString("DEVICE_ADMIN_CAN_UNINSTALL", null);
    		if (UnistallValue.equals("ON")){ 
    			ButtonUnistallOFF.setChecked(true);
    		}else{
    			ButtonUnistallOFF.setChecked(false);
    		}
    		ClearDataValue = settings.getString("DEVICE_ADMIN_CAN_CLEARDATA", null);
    		if (ClearDataValue.equals("ON")){ 
    			ButtoncleardataOFF.setChecked(true);
    		}else{
    			ButtoncleardataOFF.setChecked(false);
    		}
    		*/
        }
        @Override
        protected void onResume() {
            super.onResume();
            updateButtonStates();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case RESULT_ENABLE:
                    if (resultCode == Activity.RESULT_OK) {
                        Log.i("hoXApp Admin:", "Enabled");
                    } else {
                        Log.i("hoXApp Admin:", "Enable FAILED!");
                    }
                    return;
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

        private OnClickListener mEnableListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "You need to activate Device Administrator to perform hoXapp tasks!");
                startActivityForResult(intent, RESULT_ENABLE);
            }
        };

        private OnClickListener mDisableListener = new OnClickListener() {
            public void onClick(View v) {
                mDPM.removeActiveAdmin(mDeviceAdminSample);
                updateButtonStates();
            }
        };
        
        private OnClickListener mSetPasswordListener = new OnClickListener() {
            public void onClick(View v) {
                // Launch the activity to have the user set a new password.
                Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivity(intent);
            }
        };
        
    }
