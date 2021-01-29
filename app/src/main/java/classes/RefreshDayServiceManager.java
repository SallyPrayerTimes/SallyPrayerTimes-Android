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

import widget.LargeWidgetProvider;
import widget.LargeWidgetProviderService;
import widget.SmallWidgetProvider;
import widget.SmallWidgetProviderService;

public class RefreshDayServiceManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 25, new Intent(context, AthanServiceBroasdcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pi);
        PendingIntent piRefreshWidget = PendingIntent.getBroadcast(context, 37 , new Intent(context, RefreshWidgetAfter15Minutes.class), PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(piRefreshWidget);

        if(LargeWidgetProvider.isEnabled){
            context.startService(new Intent(context, LargeWidgetProviderService.class));
        }

        if(SmallWidgetProvider.isEnabled){
            context.startService(new Intent(context, SmallWidgetProviderService.class));
        }

        context.startService(new Intent(context,AthanService.class));
    }
}
