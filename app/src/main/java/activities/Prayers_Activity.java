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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import classes.ArabicReshape;
import classes.AthanService;
import classes.HijriTime;
import classes.MiladiTime;
import classes.PrayersTimes;
import classes.PreferenceHandler;
import classes.UserConfig;
import widget.LargeWidgetProvider;
import widget.SmallWidgetProvider;
import widget.LargeWidgetProviderService;
import widget.SmallWidgetProviderService;

import com.sallyprayertimes.R;


public class Prayers_Activity extends Activity {
	
	private TextView fajr;
	private TextView shorouk;
	private TextView duhr;
	private TextView asr;
	private TextView maghrib;
	private TextView ishaa;
	
	private TextView fajr_label;
	private TextView shorouk_label;
	private TextView duhr_label;
	private TextView asr_label;
	private TextView maghrib_label;
	private TextView ishaa_label;
	
	private TextView location_country;
	private TextView location_city;
	private TextView miladi_label;
	private TextView hijri_label;
	
	private String latitude;
	private String longitude;

    private String country;
    private String city;
    private String language;
    private String fajr_athan;
    private String shorouk_athan;
    private String duhr_athan;
    private String asr_athan;
    private String maghrib_athan;
    private String ishaa_athan;
    
    private Button fajr_athan_button;
    private Button shorouk_athan_button;
    private Button duhr_athan_button;
    private Button asr_athan_button;
    private Button maghrib_athan_button;
    private Button ishaa_athan_button;
    
    private View fajr_athan_button_layout;
    private View shorouk_athan_button_layout;
    private View duhr_athan_button_layout;
    private View asr_athan_button_layout;
    private View maghrib_athan_button_layout;
    private View ishaa_athan_button_layout;
    
    private Typeface tf;
	private AlertDialog alertDialog;
	
	private TextView missing_time;
	private TextView missing_to;
	private TextView missing_salat;
	
	private Timer nextPrayer_timer;
	
	private Button previousDayPrayerTtimes;
	private Button actualDayPrayerTtimes;
	private Button nextDayPrayerTtimes;
	private int nextPreviousDay = 0;
	
	private View previousDayPrayerTimesLinearlayout;
	private View actualDayPrayerTimesLinearlayout;
	private View nextDayPrayerTimesLinearlayout;
	
    public NumberFormat formatter = new DecimalFormat("00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prayers_activity);

		this.longitude = UserConfig.getSingleton().getLongitude();
		this.latitude = UserConfig.getSingleton().getLatitude();
	    this.country = UserConfig.getSingleton().getCountry();
		this.city = UserConfig.getSingleton().getCity();
		this.language = UserConfig.getSingleton().getLanguage();
			    
		this.fajr_athan = UserConfig.getSingleton().getFajr_athan();
		this.shorouk_athan = UserConfig.getSingleton().getShorouk_athan();
		this.duhr_athan = UserConfig.getSingleton().getDuhr_athan();
		this.asr_athan = UserConfig.getSingleton().getAsr_athan();
		this.maghrib_athan = UserConfig.getSingleton().getMaghrib_athan();
		this.ishaa_athan = UserConfig.getSingleton().getIshaa_athan();
			
		this.fajr = (TextView)findViewById(R.id.fajrTime);
		this.shorouk = (TextView)findViewById(R.id.shoroukTime);
		this.duhr = (TextView)findViewById(R.id.duhrTime);
		this.asr = (TextView)findViewById(R.id.asrTime);
		this.maghrib = (TextView)findViewById(R.id.maghribTime);
		this.ishaa = (TextView)findViewById(R.id.ishaaTime);
		this.fajr_label = (TextView)findViewById(R.id.fajr_label);
		this.shorouk_label = (TextView)findViewById(R.id.shorouk_label);
		this.duhr_label = (TextView)findViewById(R.id.duhr_label);
		this.asr_label = (TextView)findViewById(R.id.asr_label);
		this.maghrib_label = (TextView)findViewById(R.id.maghrib_label);
		this.ishaa_label = (TextView)findViewById(R.id.ishaa_label);
		this.location_country = (TextView)findViewById(R.id.location_country);
		this.location_city = (TextView)findViewById(R.id.location_city);
		this.miladi_label = (TextView)findViewById(R.id.miladi_time);
		this.hijri_label = (TextView)findViewById(R.id.hijri_time);
		
		this.fajr_athan_button = (Button)findViewById(R.id.fajr_athan_button);
		this.shorouk_athan_button = (Button)findViewById(R.id.shorouk_athan_button);
		this.duhr_athan_button = (Button)findViewById(R.id.duhr_athan_button);
		this.asr_athan_button = (Button)findViewById(R.id.asr_athan_button);
		this.maghrib_athan_button = (Button)findViewById(R.id.maghrib_athan_button);
		this.ishaa_athan_button = (Button)findViewById(R.id.ishaa_athan_button);
		
		this.fajr_athan_button_layout = (View)findViewById(R.id.row_fajr);
		this.shorouk_athan_button_layout = (View)findViewById(R.id.row_shorouk);
		this.duhr_athan_button_layout = (View)findViewById(R.id.row_duhr);
		this.asr_athan_button_layout = (View)findViewById(R.id.row_asr);
		this.maghrib_athan_button_layout = (View)findViewById(R.id.row_maghreb);
		this.ishaa_athan_button_layout = (View)findViewById(R.id.row_ishaa);

		setAthanButton(this.fajr_athan_button , this.fajr_athan);
		setAthanButton(this.shorouk_athan_button , this.shorouk_athan);
		setAthanButton(this.duhr_athan_button , this.duhr_athan);
		setAthanButton(this.asr_athan_button , this.asr_athan);
		setAthanButton(this.maghrib_athan_button , this.maghrib_athan);
		setAthanButton(this.ishaa_athan_button , this.ishaa_athan);

		this.missing_time = (TextView)findViewById(R.id.missing_time);
		this.missing_to = (TextView)findViewById(R.id.missing_to);
		this.missing_salat = (TextView)findViewById(R.id.missing_salat);
		
		this.previousDayPrayerTimesLinearlayout = (View)findViewById(R.id.previous_day_prayer_times_linearlayout);
		this.previousDayPrayerTimesLinearlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previousDayPrayerTtimesButtonHandler();
			}
		});
		this.actualDayPrayerTimesLinearlayout = (View)findViewById(R.id.actual_day_prayer_times_linearlayout);
		this.actualDayPrayerTimesLinearlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actualDayPrayerTtimesButtonHandler();
			}
		});
		this.nextDayPrayerTimesLinearlayout = (View)findViewById(R.id.next_day_prayer_times_linearlayout);
		this.nextDayPrayerTimesLinearlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextDayPrayerTtimesButtonHandler();
			}
		});
		
		this.previousDayPrayerTtimes = (Button)findViewById(R.id.previous_day_prayer_times);
		this.previousDayPrayerTtimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previousDayPrayerTtimesButtonHandler();
			}
		});
		this.actualDayPrayerTtimes = (Button)findViewById(R.id.actual_day_prayers_times);
		this.actualDayPrayerTtimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actualDayPrayerTtimesButtonHandler();
			}
		});
		this.nextDayPrayerTtimes = (Button)findViewById(R.id.next_day_prayers_times);
		this.nextDayPrayerTtimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextDayPrayerTtimesButtonHandler();
			}
		});
		
		setDefaultLanguage(UserConfig.getSingleton().getLanguage());
		
		if(language.equalsIgnoreCase("ar")){
			
		this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
		this.fajr_label.setTypeface(tf);
		this.shorouk_label.setTypeface(tf);
		this.duhr_label.setTypeface(tf);
		this.asr_label.setTypeface(tf);
		this.maghrib_label.setTypeface(tf);
		this.ishaa_label.setTypeface(tf);
	    this.fajr.setTypeface(tf);
		this.shorouk.setTypeface(tf);
		this.duhr.setTypeface(tf);
		this.asr.setTypeface(tf);
		this.maghrib.setTypeface(tf);
		this.ishaa.setTypeface(tf);
		this.location_country.setTypeface(tf);
		this.location_city.setTypeface(tf);
		this.miladi_label.setTypeface(tf);
		this.hijri_label.setTypeface(tf);
		this.missing_to.setTypeface(tf);
		this.missing_salat.setTypeface(tf);
		
		this.fajr_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.fajr))+" ");
		this.shorouk_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.shorouk))+" ");
		this.duhr_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.duhr))+" ");
		this.asr_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.asr))+" ");
		this.maghrib_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.maghrib))+" ");
		this.ishaa_label.setText(" "+ArabicReshape.reshape(getResources().getString(R.string.ishaa))+" ");
		
		if(this.country.equalsIgnoreCase("none") || this.city.equalsIgnoreCase("none")){
		this.location_country.setText(longitude);
		this.location_city.setText(latitude);
		}
		else{
			if(isProbablyArabic(city))
			{
				this.location_country.setText(ArabicReshape.reshape(city));
				this.location_city.setText(ArabicReshape.reshape(country));
			}else
			{
				this.location_country.setText(ArabicReshape.reshape(city));
				this.location_city.setText(ArabicReshape.reshape(country));
			}
		}
		}
		else{
			this.fajr_label.setText(" "+getResources().getString(R.string.fajr));
			this.shorouk_label.setText(" "+getResources().getString(R.string.shorouk));
			this.duhr_label.setText(" "+getResources().getString(R.string.duhr)+" ");
			this.asr_label.setText(" "+getResources().getString(R.string.asr));
			this.maghrib_label.setText(" "+getResources().getString(R.string.maghrib));
			this.ishaa_label.setText(" "+getResources().getString(R.string.ishaa));

			if(this.country.equalsIgnoreCase("none") || this.city.equalsIgnoreCase("none")){
				this.location_country.setText(longitude);
				this.location_city.setText(latitude);
				}
				else{
					if(isProbablyArabic(city))
					{
						this.location_country.setText(ArabicReshape.reshape(city));
						this.location_city.setText(ArabicReshape.reshape(country));
					}else
					{
						this.location_country.setText(ArabicReshape.reshape(city));
						this.location_city.setText(ArabicReshape.reshape(country));
					}
				}
		}

		this.fajr.setText(AthanService.prayerTimes.getFajrFinalTime()+" ");
		this.shorouk.setText(AthanService.prayerTimes.getShorou9FinalTime()+" ");
		this.duhr.setText(AthanService.prayerTimes.getDuhrFinalTime()+" ");
		this.asr.setText(AthanService.prayerTimes.getAsrFinalTime()+" ");
		this.maghrib.setText(AthanService.prayerTimes.getMaghribFinalTime()+" ");
		this.ishaa.setText(AthanService.prayerTimes.getIshaaFinalTime()+" ");
		
		try {
			this.miladi_label.setText(new MiladiTime(Calendar.getInstance(),getApplicationContext()).getMiladiTime());
			this.hijri_label.setText(new HijriTime(Calendar.getInstance(),getApplicationContext()).getHijriTime());
		} catch (IOException e) {
			this.miladi_label.setText("");
			this.hijri_label.setText("");
		}
		
		this.fajr_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.fajr),"fajr",fajr_athan_button);
			}
		});
		
		this.fajr_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.fajr),"fajr",fajr_athan_button);
			}
		});
		
		this.shorouk_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.shorouk),"shorouk",shorouk_athan_button);
			}
		});
		this.shorouk_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.shorouk),"shorouk",shorouk_athan_button);
			}
		});
		this.duhr_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.duhr),"duhr",duhr_athan_button);
			}
		});
		this.duhr_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.duhr),"duhr",duhr_athan_button);
			}
		});
		this.asr_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.asr),"asr",asr_athan_button);
			}
		});
		this.asr_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.asr),"asr",asr_athan_button);
			}
		});
		this.maghrib_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.maghrib),"maghrib",maghrib_athan_button);
			}
		});
		this.maghrib_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.maghrib),"maghrib",maghrib_athan_button);
			}
		});
		this.ishaa_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.ishaa),"ishaa",ishaa_athan_button);
			}
		});
		this.ishaa_athan_button_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanButtonHandler(getResources().getString(R.string.ishaa),"ishaa",ishaa_athan_button);
			}
		});

		setNextPrayerTime();
		setActualPrayerBackground(AthanService.nextPrayerCode);
	}

		   public void athanButtonHandler(final String title ,final String property ,final Button athanButton){
			   AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
				final String[] athanTypsArray = getResources().getStringArray(R.array.athan_Alert_Array);
				if(language.equalsIgnoreCase("ar")){
					builder.setTitle(ArabicReshape.reshape(title));
					for(int i = 0 ; i<athanTypsArray.length ; i++){
						athanTypsArray[i] = ArabicReshape.reshape(athanTypsArray[i]);
					}
			    	}
			    	else{
			    		builder.setTitle(title);
			    	}
				builder.setSingleChoiceItems(athanTypsArray, 0, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	String value = (String) athanTypsArray[item];
				    	try{

								if(!value.trim().equals("")){
									if(language.equalsIgnoreCase("ar")){
										if(value.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.athan)))){
											value ="athan";
											athanButton.setBackground(getResources().getDrawable(R.drawable.athan_on));
										}
										else{
											if(value.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.vibration)))){
												value ="vibration";
												athanButton.setBackground(getResources().getDrawable(R.drawable.athan_vibration));
											}
											else{
												if(value.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.notification)))){
													value ="notification";
													athanButton.setBackground(getResources().getDrawable(R.drawable.athan_notifications));
												}
												else{
													value ="none";
													athanButton.setBackground(getResources().getDrawable(R.drawable.athan_off));
												}
											}
										}
									}
									else{
										if(value.equalsIgnoreCase(getResources().getString(R.string.athan))){
											value ="athan";
											athanButton.setBackground(getResources().getDrawable(R.drawable.athan_on));
										}
										else{
											if(value.equalsIgnoreCase(getResources().getString(R.string.vibration))){
												value ="vibration";
												athanButton.setBackground(getResources().getDrawable(R.drawable.athan_vibration));
											}
											else{
												if(value.equalsIgnoreCase(getResources().getString(R.string.notification))){
													value ="notification";
													athanButton.setBackground(getResources().getDrawable(R.drawable.athan_notifications));
												}
												else{
													value ="none";
													athanButton.setBackground(getResources().getDrawable(R.drawable.athan_off));
												}
											}
										}
									}
									
									
									if(property.equalsIgnoreCase("fajr")){
										 UserConfig.getSingleton().setFajr_athan(value);
									}
									else{
										if(property.equalsIgnoreCase("shorouk")){
											UserConfig.getSingleton().setShorouk_athan(value);
										}
										else{
											if(property.equalsIgnoreCase("duhr")){
												UserConfig.getSingleton().setDuhr_athan(value);
											}
											else{
												if(property.equalsIgnoreCase("asr")){
													UserConfig.getSingleton().setAsr_athan(value);
												}
												else{
													if(property.equalsIgnoreCase("maghrib")){
														UserConfig.getSingleton().setMaghrib_athan(value);
													}
													else{
														UserConfig.getSingleton().setIshaa_athan(value);
													}
												}
											}
										}
									}
									PreferenceHandler.getSingleton().addUserConfig(UserConfig.getSingleton());
							}
						}
						catch(Exception e){}
				    	alertDialog.dismiss();
				    }
				});
			 this.alertDialog = builder.create();
			 this.alertDialog.show();
		   }
		   
		   
		   public void setAthanButton(final Button button , final String value){
					if(value.equalsIgnoreCase("athan")){
						button.setBackground(getResources().getDrawable(R.drawable.athan_on));
					}
					else{
						if(value.equalsIgnoreCase("vibration")){
							button.setBackground(getResources().getDrawable(R.drawable.athan_vibration));
						}
						else{
							if(value.equalsIgnoreCase("notification")){
								button.setBackground(getResources().getDrawable(R.drawable.athan_notifications));
							}
							else{
								button.setBackground(getResources().getDrawable(R.drawable.athan_off));
							}
						}
					}
		   }

		public TextView getFajr() {
			return fajr;
		}

		public void setFajr(TextView fajr) {
			this.fajr = fajr;
		}

		public TextView getShorouk() {
			return shorouk;
		}

		public void setShorouk(TextView shorouk) {
			this.shorouk = shorouk;
		}

		public TextView getDuhr() {
			return duhr;
		}

		public void setDuhr(TextView duhr) {
			this.duhr = duhr;
		}

		public TextView getAsr() {
			return asr;
		}

		public void setAsr(TextView asr) {
			this.asr = asr;
		}

		public TextView getMaghrib() {
			return maghrib;
		}

		public void setMaghrib(TextView maghrib) {
			this.maghrib = maghrib;
		}

		public TextView getIshaa() {
			return ishaa;
		}

		public void setIshaa(TextView ishaa) {
			this.ishaa = ishaa;
		}

		public TextView getFajr_label() {
			return fajr_label;
		}

		public void setFajr_label(TextView fajr_label) {
			this.fajr_label = fajr_label;
		}

		public TextView getShorouk_label() {
			return shorouk_label;
		}

		public void setShorouk_label(TextView shorouk_label) {
			this.shorouk_label = shorouk_label;
		}

		public TextView getDuhr_label() {
			return duhr_label;
		}

		public void setDuhr_label(TextView duhr_label) {
			this.duhr_label = duhr_label;
		}

		public TextView getAsr_label() {
			return asr_label;
		}

		public void setAsr_label(TextView asr_label) {
			this.asr_label = asr_label;
		}

		public TextView getMaghrib_label() {
			return maghrib_label;
		}

		public void setMaghrib_label(TextView maghrib_label) {
			this.maghrib_label = maghrib_label;
		}

		public TextView getIshaa_label() {
			return ishaa_label;
		}

		public void setIshaa_label(TextView ishaa_label) {
			this.ishaa_label = ishaa_label;
		}

		public TextView getLocation_country() {
			return location_country;
		}

		public void setLocation_country(TextView location_country) {
			this.location_country = location_country;
		}

		public TextView getLocation_city() {
			return location_city;
		}

		public void setLocation_city(TextView location_city) {
			this.location_city = location_city;
		}

		public TextView getMiladi_label() {
			return miladi_label;
		}

		public void setMiladi_label(TextView miladi_label) {
			this.miladi_label = miladi_label;
		}

		public TextView getHijri_label() {
			return hijri_label;
		}

		public void setHijri_label(TextView hijri_label) {
			this.hijri_label = hijri_label;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getFajr_athan() {
			return fajr_athan;
		}

		public void setFajr_athan(String fajr_athan) {
			this.fajr_athan = fajr_athan;
		}

		public String getShorouk_athan() {
			return shorouk_athan;
		}

		public void setShorouk_athan(String shorouk_athan) {
			this.shorouk_athan = shorouk_athan;
		}

		public String getDuhr_athan() {
			return duhr_athan;
		}

		public void setDuhr_athan(String duhr_athan) {
			this.duhr_athan = duhr_athan;
		}

		public String getAsr_athan() {
			return asr_athan;
		}

		public void setAsr_athan(String asr_athan) {
			this.asr_athan = asr_athan;
		}

		public String getMaghrib_athan() {
			return maghrib_athan;
		}

		public void setMaghrib_athan(String maghrib_athan) {
			this.maghrib_athan = maghrib_athan;
		}

		public String getIshaa_athan() {
			return ishaa_athan;
		}

		public void setIshaa_athan(String ishaa_athan) {
			this.ishaa_athan = ishaa_athan;
		}

		public Button getFajr_athan_button() {
			return fajr_athan_button;
		}

		public void setFajr_athan_button(Button fajr_athan_button) {
			this.fajr_athan_button = fajr_athan_button;
		}

		public Button getShorouk_athan_button() {
			return shorouk_athan_button;
		}

		public void setShorouk_athan_button(Button shorouk_athan_button) {
			this.shorouk_athan_button = shorouk_athan_button;
		}

		public Button getDuhr_athan_button() {
			return duhr_athan_button;
		}

		public void setDuhr_athan_button(Button duhr_athan_button) {
			this.duhr_athan_button = duhr_athan_button;
		}

		public Button getAsr_athan_button() {
			return asr_athan_button;
		}

		public void setAsr_athan_button(Button asr_athan_button) {
			this.asr_athan_button = asr_athan_button;
		}

		public Button getMaghrib_athan_button() {
			return maghrib_athan_button;
		}

		public void setMaghrib_athan_button(Button maghrib_athan_button) {
			this.maghrib_athan_button = maghrib_athan_button;
		}

		public Button getIshaa_athan_button() {
			return ishaa_athan_button;
		}

		public void setIshaa_athan_button(Button ishaa_athan_button) {
			this.ishaa_athan_button = ishaa_athan_button;
		}

		public Typeface getTf() {
			return tf;
		}

		public void setTf(Typeface tf) {
			this.tf = tf;
		}

		public AlertDialog getAlertDialog() {
			return alertDialog;
		}

		public void setAlertDialog(AlertDialog alertDialog) {
			this.alertDialog = alertDialog;
		}
		
		private String getNextPrayerName(){
			String nextPrayerName = getResources().getString(R.string.not_set);
	    	switch (AthanService.actualPrayerCode) {
	   		case 1020:
	   			nextPrayerName = getResources().getString(R.string.fajr); break;
	   		case 1021:
	   			nextPrayerName = getResources().getString(R.string.shorouk); break;
	   		case 1022:
	   			nextPrayerName = getResources().getString(R.string.duhr); break;
	   		case 1023:
	   			nextPrayerName = getResources().getString(R.string.asr); break;
	   		case 1024:
	   			nextPrayerName = getResources().getString(R.string.maghrib); break;
	   		case 1025:
	   			nextPrayerName = getResources().getString(R.string.ishaa); break;
	   		default: getResources().getString(R.string.not_set); break;
	   		}
	    	return nextPrayerName;
		}
		
		private void setActualPrayerBackground(int actualPrayerCode){
			findViewById(R.id.row_fajr).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));
	  		findViewById(R.id.row_shorouk).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));
	  		findViewById(R.id.row_duhr).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));
	  		findViewById(R.id.row_asr).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));
	  		findViewById(R.id.row_maghreb).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));
	  		findViewById(R.id.row_ishaa).setBackground(getResources().getDrawable(R.drawable.tab_menu_active));

	    	switch (actualPrayerCode) {
	   		case 1020:
	   		findViewById(R.id.row_fajr).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		case 1021:
	   		findViewById(R.id.row_shorouk).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		case 1022:
	   		findViewById(R.id.row_duhr).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		case 1023:
	   		findViewById(R.id.row_asr).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		case 1024:
	   		findViewById(R.id.row_maghreb).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		case 1025:
	   		findViewById(R.id.row_ishaa).setBackground(getResources().getDrawable(R.drawable.btn_blue_matte)); break;
	   		default:break;
	   		}
		}

		@Override
	    protected void onStart() {
	        super.onStart();

	        this.nextPrayer_timer = new Timer();

	        final Runnable updateNextPrayerTask = new Runnable() {
	            public void run() {
	                setNextPrayerTime();
	            }
	        };
	        this.nextPrayer_timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                runOnUiThread(updateNextPrayerTask);
	            }
	        }, 0, 1000L);
	    }

		private void setNextPrayerTime(){

		AthanService.refreshTime();

		if(AthanService.ifActualSalatTime == true)
			{
				if(this.language.equals("ar")){
					this.missing_time.setText(ArabicReshape.reshape(getNextPrayerName()));
					this.missing_to.setText(ArabicReshape.reshape(getNextPrayerName()));
					this.missing_salat.setText(ArabicReshape.reshape(getResources().getString(R.string.its_the_hour_of)));
				}else{
					this.missing_time.setText(getNextPrayerName());
					this.missing_to.setText(getResources().getString(R.string.its_the_hour_of));
					this.missing_salat.setText(getNextPrayerName());
				}
				setActualPrayerBackground(AthanService.actualPrayerCode);
			}
			else
			{
				setActualPrayerBackground(AthanService.actualPrayerCode);

				if(this.language.equals("ar")){
					this.missing_to.setText(ArabicReshape.reshape(getNextPrayerName()));
					this.missing_salat.setText(ArabicReshape.reshape(getResources().getString(R.string.missing_to)));
					this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
					this.missing_time.setTypeface(tf);
				}else{
					this.missing_to.setText(getResources().getString(R.string.missing_to));
					this.missing_salat.setText(getNextPrayerName());
				}
				this.missing_time.setText(formatter.format(AthanService.missing_hours_to_nextPrayer)+":"+formatter.format(AthanService.missing_minutes_to_nextPrayer)+":"+formatter.format(AthanService.missing_seconds_to_nextPrayer));
			}
		}

        @Override
		protected void onResume()
        {
            super.onResume();
			if(LargeWidgetProvider.isEnabled){
				this.startService(new Intent(this, LargeWidgetProviderService.class));
			}

			if(SmallWidgetProvider.isEnabled){
				this.startService(new Intent(this, SmallWidgetProviderService.class));
			}
        }

		@Override
	    protected void onStop() {
	        super.onStop();
	        try{
	        this.nextPrayer_timer.cancel();
	        this.nextPrayer_timer.purge();
	        this.nextPrayer_timer = null;
	        }catch(Exception e){}
	    }
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			try{
			this.nextPrayer_timer.cancel();
	        this.nextPrayer_timer.purge();
	        this.nextPrayer_timer = null;
		}catch(Exception e){}
		}
		
		public static boolean isProbablyArabic(String s) {
		    for (int i = 0; i < s.length();) {
		        int c = s.codePointAt(i);
		        if (c < 128)
		            return false;
		        i += Character.charCount(c);            
		    }
		    return true;
		  }
		
		private void setDefaultLanguage(String language){
			String languageToLoad = language;
		    Locale locale = new Locale(languageToLoad);
		    Locale.setDefault(locale);
		    Configuration config = new Configuration();
		    config.locale = locale;
		    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		}
		
		public void previousDayPrayerTtimesButtonHandler()
		{
			nextPreviousDay--;
    		Calendar cal= Calendar.getInstance();
    		cal.add(Calendar.DATE, nextPreviousDay);
    		try {
        		PrayersTimes prayerTimes2 = new PrayersTimes(cal , UserConfig.getSingleton());
        		this.fajr.setText(prayerTimes2.getFajrFinalTime());
    			this.shorouk.setText(prayerTimes2.getShorou9FinalTime());
    			this.duhr.setText(prayerTimes2.getDuhrFinalTime());
    			this.asr.setText(prayerTimes2.getAsrFinalTime());
    			this.maghrib.setText(prayerTimes2.getMaghribFinalTime());
    			this.ishaa.setText(prayerTimes2.getIshaaFinalTime());
        		this.miladi_label.setText(new MiladiTime(cal,getApplicationContext()).getMiladiTime());
    			this.hijri_label.setText(new HijriTime(cal,getApplicationContext()).getHijriTime());
			} catch (IOException e1) {}
		}
		
		public void actualDayPrayerTtimesButtonHandler()
		{
			nextPreviousDay = 0;
    		try {
    			this.fajr.setText(AthanService.prayerTimes.getFajrFinalTime());
    			this.shorouk.setText(AthanService.prayerTimes.getShorou9FinalTime());
    			this.duhr.setText(AthanService.prayerTimes.getDuhrFinalTime());
    			this.asr.setText(AthanService.prayerTimes.getAsrFinalTime());
    			this.maghrib.setText(AthanService.prayerTimes.getMaghribFinalTime());
    			this.ishaa.setText(AthanService.prayerTimes.getIshaaFinalTime());
        		this.miladi_label.setText(new MiladiTime(Calendar.getInstance(),getApplicationContext()).getMiladiTime());
    			this.hijri_label.setText(new HijriTime(Calendar.getInstance(),getApplicationContext()).getHijriTime());
			} catch (IOException e1) {}
		}
		
		public void nextDayPrayerTtimesButtonHandler()
		{
    		nextPreviousDay++;
    		Calendar cal= Calendar.getInstance();
    		cal.add(Calendar.DATE, nextPreviousDay);
    		try {
        		PrayersTimes prayerTimes2 = new PrayersTimes(cal,UserConfig.getSingleton());
        		this.fajr.setText(prayerTimes2.getFajrFinalTime());
    			this.shorouk.setText(prayerTimes2.getShorou9FinalTime());
    			this.duhr.setText(prayerTimes2.getDuhrFinalTime());
    			this.asr.setText(prayerTimes2.getAsrFinalTime());
    			this.maghrib.setText(prayerTimes2.getMaghribFinalTime());
    			this.ishaa.setText(prayerTimes2.getIshaaFinalTime());
        		this.miladi_label.setText(new MiladiTime(cal,getApplicationContext()).getMiladiTime());
    			this.hijri_label.setText(new HijriTime(cal,getApplicationContext()).getHijriTime());
			} catch (IOException e1) {}
		}
		
}
