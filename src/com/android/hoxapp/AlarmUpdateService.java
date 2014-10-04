package com.android.hoxapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmUpdateService extends BroadcastReceiver
{   
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {   
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.SetAlarm(context);
        }
    }
}