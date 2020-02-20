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

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.sally.R;

import widget.MyWidgetProvider;
import widget.MyWidgetProvider2;
import widget.MyWidgetProviderService;
import widget.MyWidgetProviderService2;


public class AthanService extends Service{

	public static int nextPrayerTimeInMinutes;//next prayer time in minutes
    public static int nextPrayerCode;
    public static int missing_hours_to_nextPrayer;
    public static int missing_minutes_to_nextPrayer;
    public static int missing_seconds_to_nextPrayer;

    public static Boolean isFajrForNextDay = false;
	
    public static int[] prayerTimesInMinutes;//all prayer times in minutes
	public static PrayersTimes prayerTimes;
	public static Calendar calendar;

	private Handler handler = new Handler();
	private Runnable runnable ;

	public static int actualSalatTimeMinutes;
    public static Boolean ifActualSalatTime;
    public static int actualPrayerCode;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		runnable = new Runnable() {
			public void run() {
				refreshTime();
				handler.postDelayed(this, 1000L);
			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		PreferenceHandler.getSingleton().setContext(getApplicationContext());
		AthanService.prayerTimes = new PrayersTimes(Calendar.getInstance() , PreferenceHandler.getSingleton().getUserConfig());

		AthanService.prayerTimesInMinutes = new int[6];
		AthanService.prayerTimesInMinutes = AthanService.prayerTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes

		AthanService.calendar = Calendar.getInstance();
		AthanService.getNextPrayer(false);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        actualSalatTimeMinutes = (hour * 60) + minutes;
        ifActualSalatTime = false;
		actualPrayerCode = AthanService.nextPrayerCode;

		AthanServiceBroasdcastReceiver.startNextPrayer(false , getApplicationContext());

		runnable.run();

		this.startService(new Intent(this, MyWidgetProviderService.class));
		this.startService(new Intent(this, MyWidgetProviderService2.class));

		return Service.START_STICKY;
	}

	public void refreshTime()
	{
		try {
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);

			int totalMinutes = (hour * 60) + minutes;

			if(ifActualSalatTime == false)
			{
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
			else
			    {
			        if(totalMinutes == (actualSalatTimeMinutes + 15))//fix actual prayer name for 15 minutes
			        {
						PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
						PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag1");
						wakeLock.acquire(10000);

						actualPrayerCode = AthanService.nextPrayerCode;
                        ifActualSalatTime = false;

						this.startService(new Intent(this, MyWidgetProviderService.class));
						this.startService(new Intent(this, MyWidgetProviderService2.class));
                    }
                }

		} catch (Exception ex) {
		}
	}

	@Override
	public void onDestroy() {
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
