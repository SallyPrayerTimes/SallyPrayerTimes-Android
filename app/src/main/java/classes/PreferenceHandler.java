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

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {

    private Context context;

    private static PreferenceHandler preferenceHandler;//object handle user configuration

    private String MY_PREFS_NAME = "MY_PREFS_NAME";

    public static PreferenceHandler getSingleton() {//get single object PreferenceHandler
        if (preferenceHandler == null) {
            preferenceHandler = new PreferenceHandler();
        }
        return preferenceHandler;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public UserConfig getUserConfig()
    {
        UserConfig userConfig = UserConfig.getSingleton();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        userConfig.setCountry(prefs.getString("country", "Saudi Arabia"));
        userConfig.setCity(prefs.getString("city", "Makkah"));
        userConfig.setLongitude(prefs.getString("longitude", "39.8409"));
        userConfig.setLatitude(prefs.getString("latitude", "21.4309"));
        userConfig.setTimezone(prefs.getString("timezone", "3.0"));
        userConfig.setHijri(prefs.getString("hijri", "0"));
        userConfig.setTypetime(prefs.getString("typetime", "standard"));
        userConfig.setMazhab(prefs.getString("mazhab", "shafi3i"));
        userConfig.setCalculationMethod(prefs.getString("calculationMethod", "MuslimWorldLeague"));
        userConfig.setFajr_athan(prefs.getString("fajr_athan", "athan"));
        userConfig.setShorouk_athan(prefs.getString("shorouk_athan", "athan"));
        userConfig.setDuhr_athan(prefs.getString("duhr_athan", "athan"));
        userConfig.setAsr_athan(prefs.getString("asr_athan", "athan"));
        userConfig.setMaghrib_athan(prefs.getString("maghrib_athan", "athan"));
        userConfig.setIshaa_athan(prefs.getString("ishaa_athan", "athan"));
        userConfig.setFajr_time(prefs.getString("fajr_time", "0"));
        userConfig.setShorouk_time(prefs.getString("shorouk_time", "0"));
        userConfig.setDuhr_time(prefs.getString("duhr_time", "0"));
        userConfig.setAsr_time(prefs.getString("asr_time", "0"));
        userConfig.setMaghrib_time(prefs.getString("maghrib_time", "0"));
        userConfig.setIshaa_time(prefs.getString("ishaa_time", "0"));
        userConfig.setAthan(prefs.getString("athan", "ali_ben_ahmed_mala"));
        userConfig.setTime12or24(prefs.getString("time12or24", "24"));
        userConfig.setLanguage(prefs.getString("language", "en"));
        userConfig.setBattery_optimization(prefs.getString("battery_optimization", "no"));
        userConfig.setWidget_background_color(prefs.getString("widget_background_color", "blue"));

        return  userConfig;
    }

    public void addUserConfig(UserConfig userConfig)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();

        editor.putString("country", userConfig.getCountry());
        editor.putString("city", userConfig.getCity());
        editor.putString("longitude", userConfig.getLongitude());
        editor.putString("latitude", userConfig.getLatitude());
        editor.putString("timezone", userConfig.getTimezone());
        editor.putString("hijri", userConfig.getHijri());
        editor.putString("typetime", userConfig.getTypetime());
        editor.putString("mazhab", userConfig.getMazhab());
        editor.putString("calculationMethod", userConfig.getCalculationMethod());
        editor.putString("fajr_athan", userConfig.getFajr_athan());
        editor.putString("shorouk_athan", userConfig.getShorouk_athan());
        editor.putString("duhr_athan", userConfig.getDuhr_athan());
        editor.putString("asr_athan", userConfig.getAsr_athan());
        editor.putString("maghrib_athan", userConfig.getMaghrib_athan());
        editor.putString("ishaa_athan", userConfig.getIshaa_athan());
        editor.putString("fajr_time", userConfig.getFajr_time());
        editor.putString("shorouk_time", userConfig.getShorouk_time());
        editor.putString("duhr_time", userConfig.getDuhr_time());
        editor.putString("asr_time", userConfig.getAsr_time());
        editor.putString("maghrib_time", userConfig.getMaghrib_time());
        editor.putString("ishaa_time", userConfig.getIshaa_time());
        editor.putString("athan", userConfig.getAthan());
        editor.putString("time12or24", userConfig.getTime12or24());
        editor.putString("language", userConfig.getLanguage());
        editor.putString("battery_optimization", userConfig.getBattery_optimization());
        editor.putString("widget_background_color", userConfig.getWidget_background_color());

        editor.commit();
    }


}
