package com.android.hoxapp;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;


public class hoxappconfig extends Activity{

	EditText set_password;
	EditText config_app_ident;	
	EditText config_admin_number;
	
	Button reset_password;
    Button reset_app_ident;   
    Button reset_admin_number;  
    
    ToggleButton ButtonSMSOFF;
    ToggleButton ButtonINTOFF;
    
    String SMSValue;
    String IntValue;
    String password;
    String app_identifier;
    String admin_number;
    
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);
        
        
        
        set_password = (EditText)findViewById(R.id.set_password);
        config_app_ident = (EditText)findViewById(R.id.config_app_ident);
        config_admin_number = (EditText)findViewById(R.id.config_admin_number);
        
        reset_password = (Button)findViewById(R.id.reset_password);
        reset_app_ident = (Button)findViewById(R.id.reset_app_ident);
        reset_admin_number = (Button)findViewById(R.id.reset_admin_number);
        
        ButtonSMSOFF = (ToggleButton)findViewById(R.id.ButtonSMSOFF);
        ButtonINTOFF = (ToggleButton)findViewById(R.id.ButtonINTOFF);
        
        
        updateButtonStates();
        
        reset_password.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	password = (String) set_password.getText().toString();        	
	        	 if(password.trim().length() >= 4){
	             	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
	            	SharedPreferences.Editor editor = settings.edit();
	            	editor.putString("APP_PASS", password);
	            	editor.commit();
	            	Log.e("CONFIG_APP_PASS", password);
	            	
	            	String msg_passChanged = getBaseContext().getResources().getString(R.string.msg_passChanged);
	                showToast(getBaseContext(), msg_passChanged + " " + password);
	                set_password.setText("");
	        	 }else{
	             	String msg_pass_weak = getBaseContext().getResources().getString(R.string.msg_pass_weak);
	                showToast(getBaseContext(), msg_pass_weak);
	                set_password.setText("");
	        	 }
	             
            } 
        });
        reset_admin_number.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		admin_number = (String) config_admin_number.getText().toString();
		    	if(admin_number.trim().length() >= 4){		    		
			    	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
			    	SharedPreferences.Editor editor = settings.edit();
			    	editor.putString("USER_ADMIN_NUMBER", admin_number);
			    	editor.commit();
			    	Log.e("USER_ADMIN_NUMBER", admin_number);        	
			    	String msg_admin_numberChanged = getBaseContext().getResources().getString(R.string.msg_admin_numberChanged);
			        showToast(getBaseContext(), msg_admin_numberChanged + " " + admin_number);
			        config_admin_number.setText("");

		    	}else{
			    	String msg_admin_numberWeak = getBaseContext().getResources().getString(R.string.msg_admin_numberWeak);
			        showToast(getBaseContext(), msg_admin_numberWeak);
			        config_admin_number.setText("");
		    	}
	        } 
        });
        reset_app_ident.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
		    	app_identifier = (String) config_app_ident.getText().toString();
		    	if(app_identifier.trim().length() >= 4){		    		
			    	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
			    	SharedPreferences.Editor editor = settings.edit();
			    	editor.putString("APP_IDENT", app_identifier);
			    	editor.commit();
			    	Log.e("CONFIG_APP_IDENT", app_identifier);        	
			    	String msg_appIdentChanged = getBaseContext().getResources().getString(R.string.msg_appIdentChanged);
			        showToast(getBaseContext(), msg_appIdentChanged + " " + app_identifier);
			        config_app_ident.setText("");

		    	}else{
			    	String msg_appIdent_weak = getBaseContext().getResources().getString(R.string.msg_appIdent_weak);
			        showToast(getBaseContext(), msg_appIdent_weak);
			        config_app_ident.setText("");
		    	}
	        } 
        });
        ButtonSMSOFF.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	SMSValue = (String) ButtonSMSOFF.getText().toString();
        	
        	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("GPS_SMS", SMSValue);
        	editor.commit();
        	Log.e("CONFIG_GPS_SMS", SMSValue);
        	updateButtonStates();

            } 
        });
        ButtonINTOFF.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	IntValue = (String) ButtonINTOFF.getText().toString();
        	
        	SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("GPS_NET", IntValue);
        	editor.commit();        	
        	Log.e("CONFIG_GPS_NET", IntValue);
        	updateButtonStates();

            } 
        });
    }
    
    public void updateButtonStates() {
		SharedPreferences settings = getSharedPreferences(MainActivity.class.getName(), 0);
		SMSValue = settings.getString("GPS_SMS", null);
		if (SMSValue.equals("ON")){ 
			ButtonSMSOFF.setChecked(true);
		}else{
			ButtonSMSOFF.setChecked(false);
		}
		IntValue = settings.getString("GPS_NET", null);
		if (IntValue.equals("ON")){ 
			ButtonINTOFF.setChecked(true);
		}else{
			ButtonINTOFF.setChecked(false);
		}
    }
    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
