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

import java.io.IOException;
import java.util.Calendar;

import android.content.Context;

import com.sallyprayertimes.R;

public class MiladiTime {

    private int year;//actual year
    private int month;//actual month
    private int dayOfWeek;//actual day of week
    private int dayOfMonth;//actual day of month
    private Context context;
    
    public MiladiTime(Calendar calendar , Context context) throws IOException{
    	this.context = context;
        this.year = calendar.get(Calendar.YEAR);//actual year
        this.month = calendar.get(Calendar.MONTH);//actual month
        this.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//actual day of week
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);//actual day of month
    }

    public String getMonth(int i) {//get translated month name
        String month1;
        month1 = "";
        int j = i;
        switch (j) {
        case 0:
            month1 = context.getResources().getString(R.string.January);
            break;
        case 1:
            month1 = context.getResources().getString(R.string.February);
            break;
        case 2:
            month1 = context.getResources().getString(R.string.March);
            break;
        case 3:
            month1 = context.getResources().getString(R.string.April);
            break;
        case 4:
            month1 = context.getResources().getString(R.string.May);
            break;
        case 5:
            month1 = context.getResources().getString(R.string.June);
            break;
        case 6:
            month1 = context.getResources().getString(R.string.July);
            break;
        case 7:
            month1 = context.getResources().getString(R.string.August);
            break;
        case 8:
            month1 = context.getResources().getString(R.string.September);
            break;
        case 9:
            month1 = context.getResources().getString(R.string.October);
            break;
        case 10:
            month1 = context.getResources().getString(R.string.November);
            break;
        case 11:
            month1 = context.getResources().getString(R.string.December);
            break;
        }
        return month1;
    }

    public String getDay(int i) throws IOException {//get translated day name
        String day = "";
        int j = i;
        switch (j) {
        case 1:
            day = context.getResources().getString(R.string.SUNDAY);
            break;
        case 2:
            day = context.getResources().getString(R.string.MONDAY);
            break;
        case 3:
            day = context.getResources().getString(R.string.TUESDAY);
            break;
        case 4:
            day = context.getResources().getString(R.string.WEDNESDAY);
            break;
        case 5:
            day = context.getResources().getString(R.string.TUESDAY);
            break;
        case 6:
            day = context.getResources().getString(R.string.FRIDAY);
            break;
        case 7:
            day = context.getResources().getString(R.string.SATURDAY);
            break;
        }
        return day;
    }

    public String getMiladiTime() {//get final translated miladi date
    	if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
    		return year+" "+ArabicReshape.reshape(getMonth(month))+" "+dayOfMonth;
    	}else{
    		return dayOfMonth+" "+getMonth(month)+" "+year;
    	}
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

}
