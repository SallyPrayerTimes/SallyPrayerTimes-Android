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
package classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class StartServiceAtStartupDevice extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
		   {
			   context.startService(new Intent(context, AthanService.class));

			   AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			   Intent AthanServiceBroasdcastReceiverIntent = new Intent(context, RefreshDayServiceManager.class);
			   PendingIntent pi = PendingIntent.getBroadcast(context, 70, AthanServiceBroasdcastReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			   am.cancel(pi);

			   Calendar calendar = Calendar.getInstance();
			   calendar.set(Calendar.HOUR_OF_DAY, 00);
			   calendar.set(Calendar.MINUTE, 01);
			   calendar.set(Calendar.SECOND, 00);

			   am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
		   }
	}
}
