package com.android.hoxapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class AndroidAlarmTracking extends Activity {
	private MediaPlayer mMediaPlayer;

	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        alarm();
	        
	 }
	private void alarm(){
		Log.e("ACTION", "ALARM FUNCTION");
		
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	     if(alert == null){
	         // alert is null, using backup
	         alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	         if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
	             // alert backup is null, using 2nd backup
	             alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
	         }
	     }
	     
	     try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(this, alert);
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				
			     new CountDownTimer(60000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						
					}
					public void onFinish() {
						mMediaPlayer.stop();
					}
			      }.start();
			}
    	} catch(Exception e) {
    	
    	}  

	}
	
}
