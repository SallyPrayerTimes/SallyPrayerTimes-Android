/*******************************Copyright Block*********************************
 *                                                                             *
 *                Sally Prayer Times Calculator (Final 24.1.20)                *
 *           Copyright (C) 2015 http://www.sallyprayertimes.com/               *
 *                         bibali1980@gmail.com                                *
 *                                                                             *
 *     This program is free software: you can redistribute it and/or modify    *
 *     it under the terms of the GNU General Public License as published by    *
 *      the Free Software Foundation, either version 3 of the License, or      *
 *                      (at your option) any later version.                    *
 *                                                                             *
 *       This program is distributed in the hope that it will be useful,       *
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 *                 GNU General Public License for more details.                *
 *                                                                             *
 *      You should have received a copy of the GNU General Public License      *
 *      along with this program.  If not, see http://www.gnu.org/licenses      *
 *******************************************************************************/
package activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sallyprayertimes.R;

import java.util.Calendar;

import classes.AthanService;
import classes.RefreshDayServiceManager;

public class SplashScreen_Activity extends Activity{

	private static final int SPLASH_TIME = 150;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_activity);

		if(!isMyServiceRunning(AthanService.class))
		{
			this.startService(new Intent(getApplicationContext(), AthanService.class));

			AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			Intent AthanServiceBroasdcastReceiverIntent = new Intent(getApplicationContext(), RefreshDayServiceManager.class);
			PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 70, AthanServiceBroasdcastReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			am.cancel(pi);

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 01);
			calendar.set(Calendar.SECOND, 00);

			am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
		}

        goToHomeProgram();
	}
	
	public void goToHomeProgram(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SplashScreen_Activity.this.finish();
				Intent intent = new Intent(SplashScreen_Activity.this,Home_Programe_Activity.class);
		        SplashScreen_Activity.this.startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		}, SPLASH_TIME);	
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}
