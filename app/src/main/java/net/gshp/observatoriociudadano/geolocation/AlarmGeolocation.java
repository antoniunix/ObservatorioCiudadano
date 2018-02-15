package net.gshp.observatoriociudadano.geolocation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;

/**
 * Created by gnu on 28/02/17.
 */

public class AlarmGeolocation {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private final static AlarmGeolocation INSTANCE = new AlarmGeolocation();

    private AlarmGeolocation() {
        setAlarm();
    }

    public static AlarmGeolocation getInstance() {
        return INSTANCE;
    }


    private void setAlarm() {
        alarmMgr = (AlarmManager) ContextApp.context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ContextApp.context, Wakelock.class);
        alarmIntent = PendingIntent.getBroadcast(ContextApp.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System
                .currentTimeMillis(), ContextApp.context.getResources().getInteger(R.integer.geolocation_alarm_start), alarmIntent);
    }

}
