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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import java.util.Calendar;

import widget.LargeWidgetProvider;
import widget.SmallWidgetProvider;
import widget.LargeWidgetProviderService;
import widget.SmallWidgetProviderService;


public class AthanService extends JobIntentService {

	public static int nextPrayerTimeInMinutes;//next prayer time in minutes
	public static int nextPrayerCode;
	public static int missing_hours_to_nextPrayer;
	public static int missing_minutes_to_nextPrayer;
	public static int missing_seconds_to_nextPrayer;

	public static Boolean isFajrForNextDay = false;

	public static int[] prayerTimesInMinutes;//all prayer times in minutes
	public static PrayersTimes prayerTimes;

	public static Boolean ifActualSalatTime = false;
	public static int actualPrayerCode;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		PreferenceHandler.getSingleton().setContext(getApplicationContext());
		AthanService.prayerTimes = new PrayersTimes(Calendar.getInstance() , PreferenceHandler.getSingleton().getUserConfig());

		AthanService.prayerTimesInMinutes = new int[6];//initialize
		AthanService.prayerTimesInMinutes = AthanService.prayerTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes

		AthanService.getNextPrayer(false);

		ifActualSalatTime = false;
		actualPrayerCode = AthanService.nextPrayerCode;

		AthanServiceBroasdcastReceiver.startNextPrayer(false , getApplicationContext());

		refreshTime();

		if(LargeWidgetProvider.isEnabled){
			this.startService(new Intent(this, LargeWidgetProviderService.class));
		}

		if(SmallWidgetProvider.isEnabled){
			this.startService(new Intent(this, SmallWidgetProviderService.class));
		}

		return Service.START_STICKY;
	}

	public static void refreshTime()
	{
		try {
			if(ifActualSalatTime == false)
			{
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minutes = calendar.get(Calendar.MINUTE);
				int second = calendar.get(Calendar.SECOND);

				int totalMinutes = (hour * 60) + minutes;

				if(AthanService.nextPrayerTimeInMinutes > totalMinutes){
					AthanService.missing_hours_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) / 60;
					AthanService.missing_minutes_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) % 60;
					AthanService.missing_seconds_to_nextPrayer = 59 - second;
				}
				else
				{
					AthanService.missing_hours_to_nextPrayer = (((AthanService.nextPrayerTimeInMinutes+1440)-1) - totalMinutes) / 60;
					AthanService.missing_minutes_to_nextPrayer = (((AthanService.nextPrayerTimeInMinutes+1440)-1) - totalMinutes) % 60;
					AthanService.missing_seconds_to_nextPrayer = 59 - second;
				}
			}

		} catch (Exception ex) {
		}
	}

	@Override
	public void onDestroy() {
	}

	@Override
	protected void onHandleWork(@NonNull Intent intent) {

	}

	public static void  getNextPrayer(boolean addOneMinute) {//get time code name and his time in minutes (add one minute just to get the correct athan for the next time)
		Calendar calendar = Calendar.getInstance();
		isFajrForNextDay = false;
		int totalMinutes = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + (calendar.get(Calendar.MINUTE));
		if(addOneMinute)
		{
			totalMinutes++;
		}
		if (totalMinutes == 0 || totalMinutes == 1440 || (totalMinutes >= 0 && totalMinutes <= AthanService.prayerTimesInMinutes[0])) {//if actual time is between 0 and fajr time , means that the next prayer time is fajr
			AthanService.nextPrayerCode = 1020;//fajr time code
			AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[0];
		} else {
			if (totalMinutes > AthanService.prayerTimesInMinutes[0] && totalMinutes <= AthanService.prayerTimesInMinutes[1]) {//if actual time is between fajr time and shorou9 time , means that the next prayer time is shorou9
				AthanService.nextPrayerCode = 1021;//shorou9 time code
				AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[1];
			} else {
				if (totalMinutes > AthanService.prayerTimesInMinutes[1] && totalMinutes <= AthanService.prayerTimesInMinutes[2]) {//if actual time is between shorou9 time and duhr time , means that the next prayer time is duhr
					AthanService.nextPrayerCode = 1022;//duhr time code
					AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[2];
				} else {
					if (totalMinutes > AthanService.prayerTimesInMinutes[2] && totalMinutes <= AthanService.prayerTimesInMinutes[3]) {//if actual time is between duhr and asr time , means that the next prayer time is asr
						AthanService.nextPrayerCode = 1023;//asr time code
						AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[3];
					} else {
						if (totalMinutes > AthanService.prayerTimesInMinutes[3] && totalMinutes <= AthanService.prayerTimesInMinutes[4]) {//if actual time is between asr and maghrib time , means that the next prayer time is maghrib
							AthanService.nextPrayerCode = 1024;//maghrib time code
							AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[4];
						} else {
							if (totalMinutes > AthanService.prayerTimesInMinutes[4] && totalMinutes <= AthanService.prayerTimesInMinutes[5]) {//if actual time is between maghrib and ishaa time , means that the next prayer time is ishaa
								AthanService.nextPrayerCode = 1025;//ishaa time code
								AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[5];
							} else {
								if (totalMinutes > AthanService.prayerTimesInMinutes[5] && totalMinutes < 1440) {//if actual time is between ishaa and 24H  , means that the next prayer time is fajr
									AthanService.nextPrayerCode = 1020;//fajr time code

									calendar.add(Calendar.DATE , 1);
									PrayersTimes prayersTimes = new PrayersTimes(calendar , PreferenceHandler.getSingleton().getUserConfig());// calculate the prayer time for the next day to get fajr time for the next day

									isFajrForNextDay = true;
									int[] nextDayPrayerTimesInMinutes = prayersTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes
									AthanService.nextPrayerTimeInMinutes = nextDayPrayerTimesInMinutes[0];
								}
							}
						}
					}
				}
			}
		}
	}

	public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
		if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
		if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
		if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
		if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
		return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
	}
}
