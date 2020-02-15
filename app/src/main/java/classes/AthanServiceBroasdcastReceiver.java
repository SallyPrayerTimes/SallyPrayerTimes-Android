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
import java.util.List;
import java.util.Random;

import activities.Home_Programe_Activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import com.sally.R;

import classes.PrayersTimes;
import widget.MyWidgetProviderService;
import widget.MyWidgetProviderService2;

public class AthanServiceBroasdcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("StartAthanAction"))
        {
            startNextPrayer(true , context);
        }
	}

	public static void startNextPrayer(Boolean startAthan , Context context)
    {
        if(startAthan)
        {
            AthanService.actualPrayerCode = AthanService.nextPrayerCode;
            AthanService.ifActualSalatTime = true;

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            AthanService.actualSalatTimeMinutes = (hour * 60) + minutes;

            startAthanActivity(AthanService.nextPrayerCode , context);

            AthanService.getNextPrayer(true);

            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent AthanServiceBroasdcastReceiverIntent = new Intent(context, AthanServiceBroasdcastReceiver.class);
            AthanServiceBroasdcastReceiverIntent.setAction("StartAthanAction");
            PendingIntent pi = PendingIntent.getBroadcast(context, 25, AthanServiceBroasdcastReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            am.cancel(pi);

            if(AthanService.isFajrForNextDay == true)
            {
                calendar.add(Calendar.DATE , 1);
            }

            int salatHour = AthanService.nextPrayerTimeInMinutes / 60;
            int salatMinutes = AthanService.nextPrayerTimeInMinutes % 60;

            calendar.set(Calendar.HOUR_OF_DAY , salatHour);
            calendar.set(Calendar.MINUTE , salatMinutes);
            calendar.set(Calendar.SECOND , 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
            } else {
                am.set(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
            }
        }
        else
            {
                Calendar calendar = Calendar.getInstance();

                PreferenceHandler.getSingleton().setContext(context);
                AthanService.prayerTimes = new PrayersTimes(calendar , PreferenceHandler.getSingleton().getUserConfig());

                AthanService.prayerTimesInMinutes = new int[6];
                AthanService.prayerTimesInMinutes = AthanService.prayerTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes

                AthanService.calendar = calendar;
                AthanService.getNextPrayer(false);

                AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent AthanServiceBroasdcastReceiverIntent = new Intent(context, AthanServiceBroasdcastReceiver.class);
                AthanServiceBroasdcastReceiverIntent.setAction("StartAthanAction");
                PendingIntent pi = PendingIntent.getBroadcast(context, 25, AthanServiceBroasdcastReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                am.cancel(pi);

                if(AthanService.isFajrForNextDay == true)
                {
                    calendar.add(Calendar.DATE , 1);
                }

                int salatHour = AthanService.nextPrayerTimeInMinutes / 60;
                int salatMinutes = AthanService.nextPrayerTimeInMinutes % 60;

                calendar.set(Calendar.HOUR_OF_DAY , salatHour);
                calendar.set(Calendar.MINUTE , salatMinutes);
                calendar.set(Calendar.SECOND , 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am.setExact(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
                } else {
                    am.set(AlarmManager.RTC, calendar.getTimeInMillis()  , pi);
                }
            }
        context.startService(new Intent(context, MyWidgetProviderService.class));
        context.startService(new Intent(context, MyWidgetProviderService2.class));
    }

    public static void startAthanActivity(int nextPrayerCode , Context context){
		  try{
		   String athanType = getAthanAlertType(nextPrayerCode);
		   if(athanType.equalsIgnoreCase("athan")){
               startNotification(nextPrayerCode , context , athanType);
		   }
		   else{
			   if(athanType.equalsIgnoreCase("vibration")){
                   startNotification(nextPrayerCode , context , athanType);
			   }
			   else
			   	{
					if(athanType.equalsIgnoreCase("notification")){
                        startNotification(nextPrayerCode , context , athanType);
                    }
				}
		   }
		  }catch(Exception ex){}
	   }

	   static void startNotification(int nextPrayerCode , Context context , String notificationType)
       {
           String notification_channel_id = "sally_channel_id";
           //String notification_channel_id = "sally_channel_id_" + new Random().nextInt(1000000);
           NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
           /*
           notificationManager.cancelAll();
           if (notificationManager != null) {
               List<NotificationChannel> channelList = notificationManager.getNotificationChannels();
               for (int i = 0; channelList != null && i < channelList.size(); i++) {
                   notificationManager.deleteNotificationChannel(channelList.get(i).getId());
               }
           }
           */
           //when click on widget let' go to home activity
           Intent intent = new Intent(context, Home_Programe_Activity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

           Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
           Notification.Builder builder = (new Notification.Builder(context).
                   setPriority(Notification.PRIORITY_HIGH)
                   .setSmallIcon(R.drawable.ic_launcher)
                   .setLargeIcon(largeIcon)
                   .setContentIntent(pendingIntent)
                   //.setAutoCancel(true)
                   .setOngoing(false));

           String actualAthanTime ="";
           switch (nextPrayerCode){
               case 1020:actualAthanTime = context.getResources().getString(R.string.fajr);break;
               case 1021:actualAthanTime = context.getResources().getString(R.string.shorouk);break;
               case 1022:actualAthanTime = context.getResources().getString(R.string.duhr);break;
               case 1023:actualAthanTime = context.getResources().getString(R.string.asr);break;
               case 1024:actualAthanTime = context.getResources().getString(R.string.maghrib);break;
               case 1025:actualAthanTime = context.getResources().getString(R.string.ishaa);break;
               default:break;
           }

           if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
               builder.setContentTitle(ArabicReshape.reshape(context.getResources().getString(R.string.notificationTitle))+" "+ArabicReshape.reshape(actualAthanTime));
               builder.setContentText(ArabicReshape.reshape(context.getResources().getString(R.string.notificationMessage))+" "+ArabicReshape.reshape(actualAthanTime));
           }
           else{
               builder.setContentTitle(context.getResources().getString(R.string.notificationTitle)+" "+actualAthanTime);
               builder.setContentText(context.getResources().getString(R.string.notificationMessage)+" "+actualAthanTime);
           }

           if(notificationType.equalsIgnoreCase("notification"))
           {
               builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
               notification_channel_id = "sally_channel_id_notification";
           }

           if(notificationType.equalsIgnoreCase("vibration"))
           {
               builder.setVibrate(new long[] {1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000});
               notification_channel_id = "sally_channel_id_vibration";
           }

           Uri alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.ali_ben_ahmed_mala);
           if(notificationType.equalsIgnoreCase("athan"))
           {
               String athanName = UserConfig.getSingleton().getAthan();
               if(athanName.equalsIgnoreCase("ali_ben_ahmed_mala")){
                   alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.ali_ben_ahmed_mala);
                   notification_channel_id = "sally_channel_id_athan_ali_ben_ahmed_mala";
               }else{
                   if(athanName.equalsIgnoreCase("abd_el_basset_abd_essamad")){
                       alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.abd_el_basset_abd_essamad);
                       notification_channel_id = "sally_channel_id_athan_abd_el_basset_abd_essamad";
                   }else{
                       if(athanName.equalsIgnoreCase("farou9_abd_errehmane_hadraoui")){
                           alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.farou9_abd_errehmane_hadraoui);
                           notification_channel_id = "sally_channel_id_athan_farou9_abd_errehmane_hadraoui";
                       }else{
                           if(athanName.equalsIgnoreCase("mohammad_ali_el_banna")){
                               alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.mohammad_ali_el_banna);
                               notification_channel_id = "sally_channel_id_athan_mohammad_ali_el_banna";
                           }else{
                               if(athanName.equalsIgnoreCase("mohammad_khalil_raml")){
                                   alarmSound = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.mohammad_khalil_raml);
                                   notification_channel_id = "sally_channel_id_athan_mohammad_khalil_raml";
                               }
                           }
                       }
                   }
               }

               if(alarmSound == null)
               {
                   notification_channel_id = "sally_channel_id_athan";
                   alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                   if(alarmSound == null){
                       alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                       if(alarmSound == null){
                           alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                       }
                   }
               }
               builder.setSound(alarmSound);
           }

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
           {
               NotificationChannel channel = new NotificationChannel(
                       notification_channel_id,
                       "Sally Prayer Times",
                       NotificationManager.IMPORTANCE_HIGH);

               if(notificationType.equalsIgnoreCase("athan"))
               {
                   AudioAttributes audioAttributes = new AudioAttributes.Builder()
                           .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                           .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                           .build();
                   channel.setSound(alarmSound, audioAttributes);
               }

               if(notificationType.equalsIgnoreCase("vibration"))
               {
                   channel.setVibrationPattern(new long[] {1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000 ,1000});
               }

               notificationManager.createNotificationChannel(channel);
               builder.setChannelId(notification_channel_id);
           }

           Notification note = builder.build();

           if(notificationType.equalsIgnoreCase("athan"))
           {
               note.sound = alarmSound;
               note.defaults |= Notification.DEFAULT_VIBRATE;
               note.flags |= Notification.FLAG_INSISTENT;
           }

           PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
           PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
           wakeLock.acquire();

           notificationManager.notify(1 , note);
       }

    public static String getAthanAlertType(int value){
        String athanType = "athan";
        switch (value) {
            case 1020:athanType = UserConfig.getSingleton().getFajr_athan();break;
            case 1021:athanType = UserConfig.getSingleton().getShorouk_athan();break;
            case 1022:athanType = UserConfig.getSingleton().getDuhr_athan();break;
            case 1023:athanType = UserConfig.getSingleton().getAsr_athan();break;
            case 1024:athanType = UserConfig.getSingleton().getMaghrib_athan();break;
            case 1025:athanType = UserConfig.getSingleton().getIshaa_athan();break;
            default:break;
        }
        return athanType;
    }
	  
}
