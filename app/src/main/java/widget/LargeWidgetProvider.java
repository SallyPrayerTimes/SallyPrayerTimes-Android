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
package widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class LargeWidgetProvider extends AppWidgetProvider {

	public static Boolean isEnabled = false;

    @Override
	public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		context.startService(new Intent(context, LargeWidgetProviderService.class));

		isEnabled = true;

		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, LargeWidgetProviderBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 19 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

		//After 1 minute
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pi); // update widget every minute
    }

	@Override
	public void onEnabled(Context context) {
		context.startService(new Intent(context, LargeWidgetProviderService.class));
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds){

    	isEnabled = false;

		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, LargeWidgetProviderBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 19 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(pi);
	}
       
}
