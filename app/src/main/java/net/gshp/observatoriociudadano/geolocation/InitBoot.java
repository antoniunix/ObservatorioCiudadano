package net.gshp.observatoriociudadano.geolocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InitBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmGeolocation.getInstance();
	}

}
