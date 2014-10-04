package com.android.hoxapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
 
public class AndroidGPSTrackingActivity extends Activity {
  
    // GPSTracker class
    GPSTracker gps;
    int online_check;
    static InputStream is = null;
    static String json = "";
    String IMEI = "";
    int mcc = 0;
    int mnc = 0;
    int lac = 0;
    String url;
 
    
 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 

                gps = new GPSTracker(AndroidGPSTrackingActivity.this);
                
                Intent myIntent= getIntent(); // gets the previously created intent
                String sms_message = myIntent.getStringExtra("sms_message"); // will return "FirstKeyValue"
                String sms_number = myIntent.getStringExtra("sms_number"); // will return "SecondKeyValue"
                String sms_number_sender = myIntent.getStringExtra("sms_number_sender"); // will return "SecondKeyValue"
                String timestamp = myIntent.getStringExtra("timestamp"); // will return "SecondKeyValue"
                
                String GPS_SMS = myIntent.getStringExtra("GPS_SMS");
                String GPS_NET = myIntent.getStringExtra("GPS_NET");
                String GPS_URL = myIntent.getStringExtra("GPS_URL");
                
                /* String Lng = Locale.getDefault().getDisplayLanguage(); */
                final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                
                final TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String networkOperator = tel.getNetworkOperator();            
                
                CellLocation cl = tel.getCellLocation();
                if (cl instanceof GsmCellLocation) {
                    GsmCellLocation gcl = (GsmCellLocation) cl;
                    lac = gcl.getLac();
                }
                IMEI = tel.getDeviceId();
                
                if (activeNetwork != null && activeNetwork.isConnected()) {
                	online_check = 1;
                } else {
                	online_check = 0;
                } 

                // check if GPS enabled
                if(gps.canGetLocation()){
 
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String lat = Double.toString(latitude);
                	String lng = Double.toString(longitude);
                	
                    if(online_check == 1 && GPS_NET.equals("ON")){     
                    	
                    	if(GPS_URL != null){
                    		url = GPS_URL;
                    	}else{
                    		url = "http://hoxapp.com/location/get_location.php";
                    	}
        				
        				HttpClient httpClient = new DefaultHttpClient();
        				HttpPost httpPost = new HttpPost(url);
        				try {
        					List<NameValuePair> params = new ArrayList<NameValuePair>();
        					params.add(new BasicNameValuePair("sms_message", sms_message));
        					params.add(new BasicNameValuePair("sms_number", sms_number));
        					params.add(new BasicNameValuePair("sms_number_sender", sms_number_sender));
        					params.add(new BasicNameValuePair("latitude", lat));
        					params.add(new BasicNameValuePair("longitude", lng));
        					params.add(new BasicNameValuePair("timestamp", timestamp));
        					httpPost.setEntity(new UrlEncodedFormEntity(params));
	
        					 HttpResponse httpResponse = httpClient.execute(httpPost);
        					 HttpEntity httpEntity = httpResponse.getEntity();
    		                 is = httpEntity.getContent();
        		        } catch (UnsupportedEncodingException e) {
        		            e.printStackTrace();
        		        } catch (ClientProtocolException e) {
        		            e.printStackTrace();
        		        } catch (IOException e) {
        		            e.printStackTrace();
        		        }
        				
        		        try {
        		            BufferedReader reader = new BufferedReader(new InputStreamReader(
        		                    is, "iso-8859-1"), 8);
        		            StringBuilder sb = new StringBuilder();
        		            String line = null;
        		            while ((line = reader.readLine()) != null) {
        		                sb.append(line + "\n");
        		            }
        		            is.close();
        		            json = sb.toString();
        		            Log.e("RESAULT:", json);
        		        } catch (Exception e) {
        		            Log.e("Buffer Error", "Error converting result " + e.toString());
        		        }
        		        
                    }else if(GPS_NET.equals("OFF") || GPS_SMS.equals("ON")){
    		        	Log.e("SMS_ACTION", "ENABLE_GPS");
    		        	sms_message = "LAT:" + lat + "LNG:" + lng + "MOB:" + sms_number;
    			        Intent i = new Intent(this,SendSMSActivity.class);
    			        i.putExtra("sms_message",sms_message);
    			        i.putExtra("sms_number",sms_number);
    			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
    			        getBaseContext().startActivity(i); 
    			        Log.e("GPS_TRACKING", "SMS_STARTED :" + sms_message);
                    }else{
                    	Log.e("GPS_TRACKING", "NOT_ONLINE_AND_SMS_OFF");
                    }

                }else{
                	Log.e("GPS_TRACKING", "NO_VALID_GPS_ENABLED");             
                  
                    if (networkOperator != null) {                    	
                        mcc = Integer.parseInt(networkOperator.substring(0, 3));
                        mnc = Integer.parseInt(networkOperator.substring(3));                  
                    }
                    sms_message = "MCC: " + mcc + " MNC: " + mnc + " LAC: " + lac + " IMEI: " + IMEI;                    
			        Intent i = new Intent(this,SendSMSActivity.class);
			        i.putExtra("sms_message",sms_message);
			        i.putExtra("sms_number",sms_number);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
			        getBaseContext().startActivity(i); 
			        // http://opencellid.org/api
                	Log.e("GPS_TRACKING", "SENDING_DATA");
                }
    }

}