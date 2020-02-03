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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import classes.PreferenceHandler;
import widget.MyWidgetProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import classes.ArabicReshape;
import classes.AthanService;
import classes.City;
import classes.DatabaseManager;
import classes.UserConfig;
import widget.MyWidgetProvider2;
import widget.MyWidgetProviderService;
import widget.MyWidgetProviderService2;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;

import com.sally.R;

public class Settings_Activity extends Activity implements LocationListener{

	private double latitude = 0;
	private double longitude = 0;
	private double latitude_fromList = 0;
	private double longitude_fromList = 0;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private Location location;
	private ProgressDialog progressDialog;
	private ProgressDialog progressDialog2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	private String address;
	private String city;
    private String country;
	private Geocoder geocoder;
	private List<Address> addresses = null;
	private Context context;
	private AsyncTask<Void, Void, Location> findLocationAsynkTask;
	private AsyncTask<Void, Void, String[]> getCountriesAsynkTask;
	/*
	 * Location*/
	private TableRow gps_location_row;
	private TableRow country_row;
	private TableRow city_row;
	private TextView gps_location_label;
	private TextView gps_location_value;
	private TextView country_label;
	private TextView country_value;
	private TextView city_label;
	private TextView city_value;
	/*
	 * Time*/
	private TableRow calculatonMethode_row;
	private TableRow timeZone_row;
	private TableRow mazhab_row;
	private TableRow typeTime_row;
	private TableRow hijriTime_row;
	private TableRow time12or24_row;
	private TextView calculationMethod_label;
	private TextView calculationMethod_value;
	private TextView timeZone_label;
	private TextView timeZone_value;
	private TextView mazhab_label;
	private TextView mazhab_value;
	private TextView typeTime_label;
	private TextView typeTime_value;
	private TextView hijriTime_label;
	private TextView hijriTime_value;
	private TextView time12or24_label;
	private TextView time12or24_value;
	
	/*
	 * Athan*/
	private TableRow athan_row;
	private TextView athan_label;
	private TextView athan_value;
	/*
	 * Language*/
	private TableRow language_row;
	private TextView language_label;
	private TextView language_value;
	/*
	 * Time Adjustment*/
	private TableRow fajr_time_row;
    private TableRow shorouk_time_row;
    private TableRow duhr_time_row;
    private TableRow asr_time_row;
    private TableRow maghrib_time_row;
    private TableRow ishaa_time_row;
    private TextView fajr_time_label;
    private TextView shorouk_time_label;
    private TextView duhr_time_label;
    private TextView asr_time_label;
    private TextView maghrib_time_label;
    private TextView ishaa_time_label;
    private TextView fajr_time_value;
    private TextView shorouk_time_value;
    private TextView duhr_time_value;
    private TextView asr_time_value;
    private TextView maghrib_time_value;
    private TextView ishaa_time_value;
    /*
	 * Settings Titles*/
	private TextView location_settings_title;
	private TextView time_settings_title;
	private TextView athan_settings_title;
	private TextView language_settings_title;
	private TextView battery_settings_title;
	private TextView time_adjustment_settings_title;
	
	private AlertDialog alertDialog;
	
	private String[] countries;
	private String[] citiesLocations;
	private ArrayList<City> cities;
	
	private Button upButton;
	private Button downButton;
	private EditText editText;
	private int uprange = 15;
	private int downrange = -15;
	private double timeZoneHijriTimeValue = 0;
	private int uprange_time = 60;
	private int downrange_time = -60;
	
	private String finalCountry="";
	private String finalCity="";
	private String finalTimeZone="";
	private String finalMazhab="";
	private String finalCalculationMethode="";
	private String finalTypeTime="";
	private String finalHijriTime="";
	private String finalLanguage="";
	private String finalAthan="";
	private String finalTime12or24="";
	
	private TextView latitude_value;
	private TextView longitude_value;
	private EditText country_edittext;
	private EditText city_edittext;
	private TextView latitude_label_inflat;
	private TextView longitude_label_inflat;
	private TextView country_label_inflat;
	private TextView city_label_inflat;
	
	private TextView time_zone_calcolated;

	/*
	 * Battery*/
	private TableRow battery_row;
	private TextView battery_label;
	private TextView battery_value;

	/*
	 * Widget Background Color*/
	private TableRow widget_background_color_row;
	private TextView widget_background_color_label;
	private TextView widget_background_color_value;
	private TextView widget_background_color_settings_title;
	private String finalColor = "blue";

    private Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);

		this.context=this;
		this.locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    this.locationListener = new Settings_Activity();
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	    //Location Setting
		this.gps_location_row = (TableRow)findViewById(R.id.row_gps_location);
		this.gps_location_label = (TextView)findViewById(R.id.gps_location_label);
		this.gps_location_value = (TextView)findViewById(R.id.gps_location_value);
		this.country_row = (TableRow)findViewById(R.id.row_country);
		this.country_label = (TextView)findViewById(R.id.country_label);
		this.country_value = (TextView)findViewById(R.id.country_value);
		this.city_row = (TableRow)findViewById(R.id.row_city);
		this.city_label = (TextView)findViewById(R.id.city_label);
		this.city_value = (TextView)findViewById(R.id.city_value);
		//Time Settings
		this.calculatonMethode_row = (TableRow)findViewById(R.id.row_calculationMethod);
		this.calculationMethod_label = (TextView)findViewById(R.id.calculationMethod_label);
		this.calculationMethod_value = (TextView)findViewById(R.id.calculationMethod_value);
		this.timeZone_row = (TableRow)findViewById(R.id.row_time_zone);
		this.timeZone_label = (TextView)findViewById(R.id.time_zone_label);
		this.timeZone_value = (TextView)findViewById(R.id.time_zone_value);
		this.mazhab_row = (TableRow)findViewById(R.id.row_mazhab);
		this.mazhab_label = (TextView)findViewById(R.id.mazhab_label);
		this.mazhab_value = (TextView)findViewById(R.id.mazhab_value);
		this.typeTime_row = (TableRow)findViewById(R.id.row_type_time);
		this.typeTime_label = (TextView)findViewById(R.id.type_time_label);
		this.typeTime_value = (TextView)findViewById(R.id.type_time_value);
		this.hijriTime_row = (TableRow)findViewById(R.id.row_hijri_time);
		this.hijriTime_label = (TextView)findViewById(R.id.hijri_time_label);
		this.hijriTime_value = (TextView)findViewById(R.id.hijri_time_value);
		this.time12or24_row = (TableRow)findViewById(R.id.row_time12or24);
		this.time12or24_label = (TextView)findViewById(R.id.time12or24_label);
		this.time12or24_value = (TextView)findViewById(R.id.time12or24_value);
		//Athan Settings
		this.athan_row = (TableRow)findViewById(R.id.row_athan);
		this.athan_label = (TextView)findViewById(R.id.athan_label);
		this.athan_value = (TextView)findViewById(R.id.athan_value);
		//Language Settings
		this.language_row = (TableRow)findViewById(R.id.row_language);
		this.language_label = (TextView)findViewById(R.id.language_label);
		this.language_value = (TextView)findViewById(R.id.language_value);
		//Time Adjustment
		this.fajr_time_row = (TableRow)findViewById(R.id.fajr_time_row);
		this.fajr_time_label = (TextView)findViewById(R.id.fajr_time_label);
		this.fajr_time_value = (TextView)findViewById(R.id.fajr_time_value);
		this.shorouk_time_row = (TableRow)findViewById(R.id.shorouk_time_row);
		this.shorouk_time_label = (TextView)findViewById(R.id.shorouk_time_label);
		this.shorouk_time_value = (TextView)findViewById(R.id.shorouk_time_value);
		this.duhr_time_row = (TableRow)findViewById(R.id.duhr_time_row);
		this.duhr_time_label = (TextView)findViewById(R.id.duhr_time_label);
		this.duhr_time_value = (TextView)findViewById(R.id.duhr_time_value);
		this.asr_time_row = (TableRow)findViewById(R.id.asr_time_row);
		this.asr_time_label = (TextView)findViewById(R.id.asr_time_label);
		this.asr_time_value = (TextView)findViewById(R.id.asr_time_value);
		this.maghrib_time_row = (TableRow)findViewById(R.id.maghrib_time_row);
		this.maghrib_time_label = (TextView)findViewById(R.id.maghrib_time_label);
		this.maghrib_time_value = (TextView)findViewById(R.id.maghrib_time_value);
		this.ishaa_time_row = (TableRow)findViewById(R.id.ishaa_time_row);
		this.ishaa_time_label = (TextView)findViewById(R.id.ishaa_time_label);
		this.ishaa_time_value = (TextView)findViewById(R.id.ishaa_time_value);
		//Setting Titles
		this.location_settings_title = (TextView)findViewById(R.id.location_settings_title);
		this.time_settings_title = (TextView)findViewById(R.id.time_settings_title);
		this.athan_settings_title = (TextView)findViewById(R.id.athan_settings_title);
		this.language_settings_title = (TextView)findViewById(R.id.language_settings_title);
		this.battery_settings_title = (TextView)findViewById(R.id.battery_settings_title);
		this.time_adjustment_settings_title = (TextView)findViewById(R.id.time_adjustment_settings_title);
		this.widget_background_color_settings_title = (TextView)findViewById(R.id.widget_settings_title);
		//Battery Settings
		this.battery_row = (TableRow)findViewById(R.id.row_battery);
		this.battery_label = (TextView)findViewById(R.id.battery_label);
		this.battery_value = (TextView)findViewById(R.id.battery_value);
		//Widget Background Color
		this.widget_background_color_row = (TableRow)findViewById(R.id.row_widget_background_color);
		this.widget_background_color_label = (TextView)findViewById(R.id.widget_background_color_label);
		this.widget_background_color_value = (TextView)findViewById(R.id.widget_background_color_value);
		
		setDefaultLanguage(UserConfig.getSingleton().getLanguage());
		
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
				
				this.gps_location_label.setTypeface(tf);
				this.gps_location_value.setTypeface(tf);
				this.country_label.setTypeface(tf);
				this.country_value.setTypeface(tf);
				this.city_label.setTypeface(tf);
				this.city_value.setTypeface(tf);
				this.timeZone_label.setTypeface(tf);
				this.timeZone_value.setTypeface(tf);
				this.mazhab_label.setTypeface(tf);
				this.mazhab_value.setTypeface(tf);
				this.calculationMethod_label.setTypeface(tf);
				this.calculationMethod_value.setTypeface(tf);
				this.typeTime_label.setTypeface(tf);
				this.typeTime_value.setTypeface(tf);
				this.hijriTime_label.setTypeface(tf);
				this.hijriTime_value.setTypeface(tf);
				this.language_label.setTypeface(tf);
				this.language_value.setTypeface(tf);
				this.widget_background_color_label.setTypeface(tf);
				this.widget_background_color_value.setTypeface(tf);
				this.battery_label.setTypeface(tf);
				this.battery_value.setTypeface(tf);
				this.athan_label.setTypeface(tf);
				this.athan_value.setTypeface(tf);
				this.time12or24_label.setTypeface(tf);
				this.time12or24_value.setTypeface(tf);
				this.fajr_time_label.setTypeface(tf);
				this.fajr_time_value.setTypeface(tf);
				this.shorouk_time_label.setTypeface(tf);
				this.shorouk_time_value.setTypeface(tf);
				this.duhr_time_label.setTypeface(tf);
				this.duhr_time_value.setTypeface(tf);
				this.asr_time_label.setTypeface(tf);
				this.asr_time_value.setTypeface(tf);
				this.maghrib_time_label.setTypeface(tf);
				this.maghrib_time_value.setTypeface(tf);
				this.ishaa_time_label.setTypeface(tf);
				this.ishaa_time_value.setTypeface(tf);
				this.location_settings_title.setTypeface(tf);
				this.time_settings_title.setTypeface(tf);
				this.athan_settings_title.setTypeface(tf);
				this.language_settings_title.setTypeface(tf);
				this.battery_settings_title.setTypeface(tf);
				this.time_adjustment_settings_title.setTypeface(tf);
				
				this.location_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.location)));
				this.time_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.time)));
				this.athan_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
				this.language_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.Language)));
				this.battery_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.battery)));
				this.time_adjustment_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.time_adjustment)));
				
				if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
				this.gps_location_label.setText(ArabicReshape.reshape(getResources().getString(R.string.location)));
				this.gps_location_value.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
				this.country_label.setText(ArabicReshape.reshape(getResources().getString(R.string.country)));
				this.country_value.setText(ArabicReshape.reshape(getResources().getString(R.string.not_set)));
				this.city_label.setText(ArabicReshape.reshape(getResources().getString(R.string.city)));
				this.city_value.setText(ArabicReshape.reshape(getResources().getString(R.string.not_set)));
				}
				else{
					this.gps_location_label.setText(ArabicReshape.reshape(getResources().getString(R.string.locationgps)));
					this.gps_location_value.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCountry())+" - "+(ArabicReshape.reshape(UserConfig.getSingleton().getCity())));
					this.country_label.setText(ArabicReshape.reshape(getResources().getString(R.string.country)));
					this.country_value.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCountry()));
					this.city_label.setText(ArabicReshape.reshape(getResources().getString(R.string.city)));
					this.city_value.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity()));
				}
				
				if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("UmmAlQuraUniv")){
					this.calculationMethod_label.setText(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
					this.calculationMethod_value.setText(ArabicReshape.reshape(getResources().getString(R.string.UmmAlQuraUniv)));
				}else{
					if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("EgytionGeneralAuthorityofSurvey")){
						this.calculationMethod_label.setText(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
						this.calculationMethod_value.setText(ArabicReshape.reshape(getResources().getString(R.string.EgytionGeneralAuthorityofSurvey)));
					}else{
						if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("UnivOfIslamicScincesKarachi")){
							this.calculationMethod_label.setText(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
							this.calculationMethod_value.setText(ArabicReshape.reshape(getResources().getString(R.string.UnivOfIslamicScincesKarachi)));
						}else{
							if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("IslamicSocietyOfNorthAmerica")){
								this.calculationMethod_label.setText(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
								this.calculationMethod_value.setText(ArabicReshape.reshape(getResources().getString(R.string.IslamicSocietyOfNorthAmerica)));
							}
							else{
								this.calculationMethod_label.setText(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
								this.calculationMethod_value.setText(ArabicReshape.reshape(getResources().getString(R.string.MuslimWorldLeague)));
							}
							}
						}
					}
				
				
				this.timeZone_label.setText(ArabicReshape.reshape(getResources().getString(R.string.time_zone)));
				this.timeZone_value.setText(UserConfig.getSingleton().getTimezone());				
				
				if(UserConfig.getSingleton().getMazhab().equalsIgnoreCase("hanafi")){
					this.mazhab_label.setText(ArabicReshape.reshape(getResources().getString(R.string.mazhab)));
					this.mazhab_value.setText(ArabicReshape.reshape(getResources().getString(R.string.hanafi)));
		    	}
		    	else{
		    		this.mazhab_label.setText(ArabicReshape.reshape(getResources().getString(R.string.mazhab)));
					this.mazhab_value.setText(ArabicReshape.reshape(getResources().getString(R.string.shafi3i)));
		    	}
				
				if(UserConfig.getSingleton().getTypetime().equalsIgnoreCase("standard")){
					this.typeTime_label.setText(ArabicReshape.reshape(getResources().getString(R.string.type_time)));
					this.typeTime_value.setText(ArabicReshape.reshape(getResources().getString(R.string.standard)));
				}
		    	else{
		    		this.typeTime_label.setText(ArabicReshape.reshape(getResources().getString(R.string.type_time)));
					this.typeTime_value.setText(ArabicReshape.reshape(getResources().getString(R.string.sayfi)));
		    	}
				
				this.hijriTime_label.setText(ArabicReshape.reshape(getResources().getString(R.string.hijri_adjustment)));
				this.hijriTime_value.setText(UserConfig.getSingleton().getHijri());

				this.time12or24_label.setText(ArabicReshape.reshape(getResources().getString(R.string.time12or24)));
				if(UserConfig.getSingleton().getTime12or24().equals("12")){
					this.time12or24_value.setText(UserConfig.getSingleton().getTime12or24()+" h");
				}else{
					this.time12or24_value.setText(UserConfig.getSingleton().getTime12or24()+" H");
				}

				if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("ali_ben_ahmed_mala")){
					this.athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
					this.athan_value.setText(ArabicReshape.reshape(getResources().getString(R.string.ali_ben_ahmed_mala)));
				}else{
					if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("abd_el_basset_abd_essamad")){
						this.athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
						this.athan_value.setText(ArabicReshape.reshape(getResources().getString(R.string.abd_el_basset_abd_essamad)));
					}else{
						if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("farou9_abd_errehmane_hadraoui")){
							this.athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
							this.athan_value.setText(ArabicReshape.reshape(getResources().getString(R.string.farou9_abd_errehmane_hadraoui)));
						}else{
							if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("mohammad_ali_el_banna")){
								this.athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
								this.athan_value.setText(ArabicReshape.reshape(getResources().getString(R.string.mohammad_ali_el_banna)));
							}
							else{
								this.athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.athan)));
								this.athan_value.setText(ArabicReshape.reshape(getResources().getString(R.string.mohammad_khalil_raml)));
							}
							}
						}
					}

				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					this.language_label.setText(ArabicReshape.reshape(getResources().getString(R.string.Language)));
					this.language_value.setText(ArabicReshape.reshape(getResources().getString(R.string.arabic)));
				}else{
					if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("en")){
						this.language_label.setText(ArabicReshape.reshape(getResources().getString(R.string.Language)));
						this.language_value.setText(ArabicReshape.reshape(getResources().getString(R.string.english)));
					}else{
							if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("fr")){
								this.language_label.setText(ArabicReshape.reshape(getResources().getString(R.string.Language)));
								this.language_value.setText(ArabicReshape.reshape(getResources().getString(R.string.french)));
							}else{
								this.language_label.setText(ArabicReshape.reshape(getResources().getString(R.string.Language)));
								this.language_value.setText(ArabicReshape.reshape(getResources().getString(R.string.italiano)));
							}
					}
					
				}

				if(UserConfig.getSingleton().getBattery_optimization().equalsIgnoreCase("no")){
					this.battery_label.setText(ArabicReshape.reshape(getResources().getString(R.string.battery_optimization)));
					this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.not_optimized)));
				}else{
					this.battery_label.setText(ArabicReshape.reshape(getResources().getString(R.string.battery_optimization)));
					this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.optimized)));
				}

				this.widget_background_color_label.setText(ArabicReshape.reshape(getResources().getString(R.string.widget_background_color)));
				this.widget_background_color_value.setText(ArabicReshape.reshape(UserConfig.getSingleton().getWidget_background_color()));
				this.widget_background_color_settings_title.setText(ArabicReshape.reshape(getResources().getString(R.string.widget)));
						
				this.fajr_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.fajr)));
				this.fajr_time_value.setText(UserConfig.getSingleton().getFajr_time());
				this.shorouk_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.shorouk)));
				this.shorouk_time_value.setText(UserConfig.getSingleton().getShorouk_time());
				this.duhr_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.duhr)));
				this.duhr_time_value.setText(UserConfig.getSingleton().getDuhr_time());
				this.asr_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.asr)));
				this.asr_time_value.setText(UserConfig.getSingleton().getAsr_time());
				this.maghrib_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.maghrib)));
				this.maghrib_time_value.setText(UserConfig.getSingleton().getMaghrib_time());
				this.ishaa_time_label.setText(ArabicReshape.reshape(getResources().getString(R.string.ishaa)));
				this.ishaa_time_value.setText(UserConfig.getSingleton().getIshaa_time());
			}
			else{
				
				this.location_settings_title.setText(getResources().getString(R.string.location));
				this.time_settings_title.setText(getResources().getString(R.string.time));
				this.athan_settings_title.setText(getResources().getString(R.string.athan));
				this.language_settings_title.setText(getResources().getString(R.string.Language));
				this.battery_settings_title.setText(getResources().getString(R.string.battery));
				this.time_adjustment_settings_title.setText(getResources().getString(R.string.time_adjustment));
				
				if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
					this.gps_location_label.setText(getResources().getString(R.string.location));
					this.gps_location_value.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
					this.country_label.setText(getResources().getString(R.string.country));
					this.country_value.setText(getResources().getString(R.string.not_set));
					this.city_label.setText(getResources().getString(R.string.city));
					this.city_value.setText(getResources().getString(R.string.not_set));
					}
					else{
						this.gps_location_label.setText(getResources().getString(R.string.locationgps));
						this.gps_location_value.setText(UserConfig.getSingleton().getCountry()+" - "+UserConfig.getSingleton().getCity());
						this.country_label.setText(getResources().getString(R.string.country));
						this.country_value.setText(UserConfig.getSingleton().getCountry());
						this.city_label.setText(getResources().getString(R.string.city));
						this.city_value.setText(UserConfig.getSingleton().getCity());
					}
				
				if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("UmmAlQuraUniv")){
					this.calculationMethod_label.setText(getResources().getString(R.string.calculationMethod));
					this.calculationMethod_value.setText(getResources().getString(R.string.UmmAlQuraUniv));
				}else{
					if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("EgytionGeneralAuthorityofSurvey")){
						this.calculationMethod_label.setText(getResources().getString(R.string.calculationMethod));
						this.calculationMethod_value.setText(getResources().getString(R.string.EgytionGeneralAuthorityofSurvey));
					}else{
						if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("UnivOfIslamicScincesKarachi")){
							this.calculationMethod_label.setText(getResources().getString(R.string.calculationMethod));
							this.calculationMethod_value.setText(getResources().getString(R.string.UnivOfIslamicScincesKarachi));
						}else{
							if(UserConfig.getSingleton().getCalculationMethod().equalsIgnoreCase("IslamicSocietyOfNorthAmerica")){
								this.calculationMethod_label.setText(getResources().getString(R.string.calculationMethod));
								this.calculationMethod_value.setText(getResources().getString(R.string.IslamicSocietyOfNorthAmerica));
							}
							else{
								this.calculationMethod_label.setText(getResources().getString(R.string.calculationMethod));
								this.calculationMethod_value.setText(getResources().getString(R.string.MuslimWorldLeague));
						}
					}
				}
			}
				
				
				this.timeZone_label.setText(getResources().getString(R.string.time_zone));
				this.timeZone_value.setText(UserConfig.getSingleton().getTimezone());				
				
				if(UserConfig.getSingleton().getMazhab().equalsIgnoreCase("hanafi")){
					this.mazhab_label.setText(getResources().getString(R.string.mazhab));
					this.mazhab_value.setText(getResources().getString(R.string.hanafi));
		    	}
		    	else{
		    		this.mazhab_label.setText(getResources().getString(R.string.mazhab));
					this.mazhab_value.setText(getResources().getString(R.string.shafi3i));
		    	}
				
				if(UserConfig.getSingleton().getTypetime().equalsIgnoreCase("standard")){
					this.typeTime_label.setText(getResources().getString(R.string.type_time));
					this.typeTime_value.setText(getResources().getString(R.string.standard));
				}
		    	else{
		    		this.typeTime_label.setText(getResources().getString(R.string.type_time));
					this.typeTime_value.setText(ArabicReshape.reshape(getResources().getString(R.string.sayfi)));
		    	}
				
				this.hijriTime_label.setText(getResources().getString(R.string.hijri_adjustment));
				this.hijriTime_value.setText(UserConfig.getSingleton().getHijri());
				
				this.hijriTime_label.setText(ArabicReshape.reshape(getResources().getString(R.string.hijri_adjustment)));
				this.hijriTime_value.setText(UserConfig.getSingleton().getHijri());

				this.time12or24_label.setText(getResources().getString(R.string.time12or24));
				if(UserConfig.getSingleton().getTime12or24().equals("12")){
					this.time12or24_value.setText(UserConfig.getSingleton().getTime12or24()+" h");
				}else{
					this.time12or24_value.setText(UserConfig.getSingleton().getTime12or24()+" H");
				}
				
				if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("ali_ben_ahmed_mala")){
					this.athan_label.setText(getResources().getString(R.string.athan));
					this.athan_value.setText(getResources().getString(R.string.ali_ben_ahmed_mala));
				}else{
					if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("abd_el_basset_abd_essamad")){
						this.athan_label.setText(getResources().getString(R.string.athan));
						this.athan_value.setText(getResources().getString(R.string.abd_el_basset_abd_essamad));
					}else{
						if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("farou9_abd_errehmane_hadraoui")){
							this.athan_label.setText(getResources().getString(R.string.athan));
							this.athan_value.setText(getResources().getString(R.string.farou9_abd_errehmane_hadraoui));
						}else{
							if(UserConfig.getSingleton().getAthan().equalsIgnoreCase("mohammad_ali_el_banna")){
								this.athan_label.setText(getResources().getString(R.string.athan));
								this.athan_value.setText(getResources().getString(R.string.mohammad_ali_el_banna));
							}
							else{
								this.athan_label.setText(getResources().getString(R.string.athan));
								this.athan_value.setText(getResources().getString(R.string.mohammad_khalil_raml));
							}
							}
						}
					}

				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					this.language_label.setText(getResources().getString(R.string.Language));
					this.language_value.setText(getResources().getString(R.string.arabic));
				}else{
					if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("en")){
						this.language_label.setText(getResources().getString(R.string.Language));
						this.language_value.setText(ArabicReshape.reshape(getResources().getString(R.string.english)));
					}else{
							if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("fr")){
								this.language_label.setText(getResources().getString(R.string.Language));
								this.language_value.setText(getResources().getString(R.string.french));
							}else{
								this.language_label.setText(getResources().getString(R.string.Language));
								this.language_value.setText(getResources().getString(R.string.italiano));
							}
					}
				}

				this.widget_background_color_label.setText(getResources().getString(R.string.widget_background_color));
				this.widget_background_color_value.setText(UserConfig.getSingleton().getWidget_background_color());
				this.widget_background_color_settings_title.setText(getResources().getString(R.string.widget));

				if(UserConfig.getSingleton().getBattery_optimization().equalsIgnoreCase("no")){
					this.battery_label.setText(getResources().getString(R.string.battery_optimization));
					this.battery_value.setText(getResources().getString(R.string.not_optimized));
				}else{
					this.battery_label.setText(getResources().getString(R.string.battery_optimization));
					this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.optimized)));
				}
			
				this.fajr_time_label.setText(getResources().getString(R.string.fajr));
				this.fajr_time_value.setText(UserConfig.getSingleton().getFajr_time());
				this.shorouk_time_label.setText(getResources().getString(R.string.shorouk));
				this.shorouk_time_value.setText(UserConfig.getSingleton().getShorouk_time());
				this.duhr_time_label.setText(getResources().getString(R.string.duhr));
				this.duhr_time_value.setText(UserConfig.getSingleton().getDuhr_time());
				this.asr_time_label.setText(getResources().getString(R.string.asr));
				this.asr_time_value.setText(UserConfig.getSingleton().getAsr_time());
				this.maghrib_time_label.setText(getResources().getString(R.string.maghrib));
				this.maghrib_time_value.setText(UserConfig.getSingleton().getMaghrib_time());
				this.ishaa_time_label.setText(getResources().getString(R.string.ishaa));
				this.ishaa_time_value.setText(UserConfig.getSingleton().getIshaa_time());
			}
			
			this.gps_location_row.setOnClickListener(new View.OnClickListener() {  
		         public void onClick(View view) {    
		            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { 
		            	getLocation(locationManager);
		            	findLocation(); 	            	
		              } else { 
		            	  if(isNetworkAvailable())
		            	  {
		            		  getLocation(locationManager);
				              findLocation();
		            	  }else
		            	  {
		            		  if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
			            	  {
			            		  Toast.makeText(getApplicationContext(),ArabicReshape.reshape(getResources().getString(R.string.gps_is_not_on)) , Toast.LENGTH_SHORT).show(); 
							  }
							  else
							  {
							      Toast.makeText(getApplicationContext(),getResources().getString(R.string.gps_is_not_on) , Toast.LENGTH_SHORT).show(); 
							  }  
		            	  } 
		              }  
		      
		          }  
		     }); 
			
			this.country_row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getCountries();
				}
			});
			
			this.city_row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getCities();
				}
			});
			
			this.calculatonMethode_row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					calulationMethodHandler();
				}
			});
			
			this.timeZone_row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					timeZoneHandler();
				}
			});
		
		this.mazhab_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mazhabHandler();
			}
		});
		
		this.typeTime_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				typeTimeHandler();
			}
		});
		
		this.hijriTime_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hijriTimeHandler();
			}
		});
		
		this.time12or24_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				time12or24Handler();
			}
		});
		
		this.athan_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				athanHandler();
			}
		});
		
		this.language_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				languageHandler();
			}
		});
		
		this.fajr_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FajrTimeHandler();
			}
		});
		
		this.shorouk_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShoroukTimeHandler();
			}
		});
		
		this.duhr_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DuhrTimeHandler();
			}
		});
		
		this.asr_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AsrTimeHandler();
			}
		});
		
		this.maghrib_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MaghribTimeHandler();
			}
		});
		
		this.ishaa_time_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IshaaTimeHandler();
			}
		});

		this.battery_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BatteryHandler();
			}
		});

		this.widget_background_color_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WidgetBackgroundColorHandler();
			}
		});
	}

	public void WidgetBackgroundColorHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.widget)));
		}
		else{
			builder.setTitle(getResources().getString(R.string.widget));
		}
		final String[] widgetColorsArray = getResources().getStringArray(R.array.widget_colors);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<widgetColorsArray.length ; i++){
				widgetColorsArray[i] = ArabicReshape.reshape(widgetColorsArray[i]);
			}
		}
		builder.setSingleChoiceItems(widgetColorsArray, selected, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				selected = item;
				finalColor = ((String) widgetColorsArray[item]).toLowerCase();
				try{
					if(!finalColor.equals("")){
						UserConfig.getSingleton().setWidget_background_color(finalColor);
					}

					setUserConfig();
					refreshService();
				}
				catch(Exception e){}
				widget_background_color_value.setText(finalColor);
				alertDialog.dismiss();
			}
		});
		this.alertDialog = builder.create();
		this.alertDialog.show();
	}

	public void BatteryHandler()
	{
		try
		{
			String packageName = getPackageName();
			if (!((PowerManager)getSystemService(POWER_SERVICE)).isIgnoringBatteryOptimizations(packageName))
			{
				if (Build.VERSION.SDK_INT >= 23)
				{
					try {
						Intent intent = new Intent();
						intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
						intent.setData(Uri.parse("package:" + packageName));
						startActivityForResult(intent , 70);
					} catch (Exception e) {
						Toast.makeText(this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
			else
			{
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
				{
					this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.optimized)));
					Toast.makeText(this, ArabicReshape.reshape(getResources().getString(R.string.application_already_optimized)), Toast.LENGTH_SHORT).show();
				}
				else
				{
					this.battery_value.setText(getResources().getString(R.string.optimized));
					Toast.makeText(this, getResources().getString(R.string.application_already_optimized), Toast.LENGTH_SHORT).show();
				}
			}
		}catch (NoSuchMethodError ex)
		{
			UserConfig.getSingleton().setBattery_optimization("yes");
			setUserConfig();
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
			{
				this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.optimized)));
				Toast.makeText(this, ArabicReshape.reshape(getResources().getString(R.string.optimized)), Toast.LENGTH_SHORT).show();
			}
			else
			{
				this.battery_value.setText(getResources().getString(R.string.optimized));
				Toast.makeText(this, getResources().getString(R.string.optimized), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed  here it is 70
		if(requestCode == 70)
		{
			if (resultCode == RESULT_OK)
			{
				UserConfig.getSingleton().setBattery_optimization("yes");
				setUserConfig();
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
				{
					this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.optimized)));
					Toast.makeText(this, ArabicReshape.reshape(getResources().getString(R.string.optimized)), Toast.LENGTH_SHORT).show();
				}
				else
				{
					this.battery_value.setText(getResources().getString(R.string.optimized));
					Toast.makeText(this, getResources().getString(R.string.optimized), Toast.LENGTH_SHORT).show();
				}
			}
			else
				{
					UserConfig.getSingleton().setBattery_optimization("no");
					setUserConfig();
					if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
					{
						this.battery_value.setText(ArabicReshape.reshape(getResources().getString(R.string.not_optimized)));
						Toast.makeText(this, ArabicReshape.reshape(getResources().getString(R.string.not_optimized)), Toast.LENGTH_SHORT).show();
					}
					else
					{
						this.battery_value.setText(getResources().getString(R.string.not_optimized));
						Toast.makeText(this, getResources().getString(R.string.not_optimized), Toast.LENGTH_SHORT).show();
					}
				}
		}
	}

	public void mazhabHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.mazhab)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.mazhab));
	    	}
		final String[] mazhabArray = getResources().getStringArray(R.array.mazhab_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<mazhabArray.length ; i++){
				mazhabArray[i] = ArabicReshape.reshape(mazhabArray[i]);
			}
		}
		builder.setSingleChoiceItems(mazhabArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalMazhab = (String) mazhabArray[item];
		    	try{					
					if(!finalMazhab.equals("")){
						if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
							if(finalMazhab.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.hanafi)))){
								UserConfig.getSingleton().setMazhab("hanafi");
					    	}
					    	else{
					    		UserConfig.getSingleton().setMazhab("shafi3i");
					    	}
						}
						else{
						if(finalMazhab.equalsIgnoreCase(getResources().getString(R.string.hanafi))){
							UserConfig.getSingleton().setMazhab("hanafi");
				    	}
				    	else{
				    		UserConfig.getSingleton().setMazhab("shafi3i");
				    	}
						}
					}
					setUserConfig();
					refreshService();
				}
				catch(Exception e){}
			    mazhab_value.setText(finalMazhab);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void calulationMethodHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.calculationMethod)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.calculationMethod));
	    	}
		final String[] calculationMethodArray = getResources().getStringArray(R.array.calculationMethod_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<calculationMethodArray.length ; i++){
				calculationMethodArray[i] = ArabicReshape.reshape(calculationMethodArray[i]);
			}
		}
		builder.setSingleChoiceItems(calculationMethodArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalCalculationMethode = (String) calculationMethodArray[item];
		    	try{
					
					if(!finalCalculationMethode.equals("")){
						if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
							if(finalCalculationMethode.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.UmmAlQuraUniv)))){
								UserConfig.getSingleton().setCalculationMethod("UmmAlQuraUniv");
							}else{
								if(finalCalculationMethode.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.EgytionGeneralAuthorityofSurvey)))){
									UserConfig.getSingleton().setCalculationMethod("EgytionGeneralAuthorityofSurvey");
								}else{
									if(finalCalculationMethode.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.UnivOfIslamicScincesKarachi)))){
										UserConfig.getSingleton().setCalculationMethod("UnivOfIslamicScincesKarachi");
									}else{
										if(finalCalculationMethode.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.IslamicSocietyOfNorthAmerica)))){
											UserConfig.getSingleton().setCalculationMethod("IslamicSocietyOfNorthAmerica");
										}else{
												UserConfig.getSingleton().setCalculationMethod("MuslimWorldLeague");
										}
									}
								}
							}	
						}
						else{
						if(finalCalculationMethode.equalsIgnoreCase(getResources().getString(R.string.UmmAlQuraUniv))){
							UserConfig.getSingleton().setCalculationMethod("UmmAlQuraUniv");
						}else{
							if(finalCalculationMethode.equalsIgnoreCase(getResources().getString(R.string.EgytionGeneralAuthorityofSurvey))){
								UserConfig.getSingleton().setCalculationMethod("EgytionGeneralAuthorityofSurvey");
							}else{
								if(finalCalculationMethode.equalsIgnoreCase(getResources().getString(R.string.UnivOfIslamicScincesKarachi))){
									UserConfig.getSingleton().setCalculationMethod("UnivOfIslamicScincesKarachi");
								}else{
									if(finalCalculationMethode.equalsIgnoreCase(getResources().getString(R.string.IslamicSocietyOfNorthAmerica))){
										UserConfig.getSingleton().setCalculationMethod("IslamicSocietyOfNorthAmerica");
									}else{
											UserConfig.getSingleton().setCalculationMethod("MuslimWorldLeague");
									}		
								}			
							}					
						}	
					  }
					}
					setUserConfig();
					refreshService();
				}
				catch(Exception e){}
			    calculationMethod_value.setText(finalCalculationMethode);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void typeTimeHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.type_time)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.type_time));
	    	}
		final String[] typeTimeArray = getResources().getStringArray(R.array.typeTime_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<typeTimeArray.length ; i++){
				typeTimeArray[i] = ArabicReshape.reshape(typeTimeArray[i]);
			}
		}
		builder.setSingleChoiceItems(typeTimeArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalTypeTime = (String) typeTimeArray[item];
		    	try{					
					if(!finalTypeTime.equals("")){
						if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
							if(finalTypeTime.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.standard)))){
								UserConfig.getSingleton().setTypetime("standard");
					    	}
					    	else{
					    		UserConfig.getSingleton().setTypetime("sayfi");
					    	}
						}
						else{
						if(finalTypeTime.equalsIgnoreCase(getResources().getString(R.string.standard))){
							UserConfig.getSingleton().setTypetime("standard");
				    	}
				    	else{
				    		UserConfig.getSingleton().setTypetime("sayfi");
				    	}
						}
					}
					setUserConfig();
					refreshService();
				}
				catch(Exception e){}
			    typeTime_value.setText(finalTypeTime);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void hijriTimeHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.hijri_adjustment)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.hijri_adjustment));
	    	}
		    builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					finalHijriTime = editText.getText().toString();
					try{
						if(!finalHijriTime.equals("")){
							UserConfig.getSingleton().setHijri(finalHijriTime);
						}
						setUserConfig();
					}
					catch(Exception e){}
					hijriTime_value.setText(finalHijriTime);
					alertDialog.dismiss();
				}
				});
		LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = li.inflate(R.layout.time_zone_layout, null);
	    
	    this.upButton = (Button) view.findViewById(R.id.upButton);
		this.downButton = (Button) view.findViewById(R.id.downButton);
		this.editText = (EditText) view.findViewById(R.id.numberEditText);
		this.editText.setText(String.valueOf(timeZoneHijriTimeValue));
		this.upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downButton.setBackgroundResource(R.drawable.timepicker_down_normal);
				upButton.setBackgroundResource(R.drawable.timepicker_up_pressed);
				if (timeZoneHijriTimeValue >= downrange && timeZoneHijriTimeValue <= uprange){
					timeZoneHijriTimeValue = timeZoneHijriTimeValue+1;
				}
				if (timeZoneHijriTimeValue > uprange){
					timeZoneHijriTimeValue = 0;
				}
				editText.setText(String.valueOf(timeZoneHijriTimeValue));
			}
		});

			this.downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					downButton .setBackgroundResource(R.drawable.timepicker_down_pressed);
					upButton.setBackgroundResource(R.drawable.timepicker_up_normal);
					if (timeZoneHijriTimeValue >= downrange && timeZoneHijriTimeValue <= uprange)
						timeZoneHijriTimeValue = timeZoneHijriTimeValue-1;
					if (timeZoneHijriTimeValue < downrange)
						timeZoneHijriTimeValue = 0;
					editText.setText(String.valueOf(timeZoneHijriTimeValue));
				}
			});
	    builder.setView(view);
		this.alertDialog = builder.create();
		this.alertDialog.show();
	}
	
	public void time12or24Handler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.time12or24)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.time12or24));
	    	}
		final String[] typeTimeArray = getResources().getStringArray(R.array.time12or24_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<typeTimeArray.length ; i++){
				typeTimeArray[i] = ArabicReshape.reshape(typeTimeArray[i]);
			}
		}
		builder.setSingleChoiceItems(typeTimeArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalTime12or24 = (String) typeTimeArray[item];
		    	try{					
					if(!finalTime12or24.equals("")){
						if(finalTime12or24.equalsIgnoreCase(getResources().getString(R.string.time12or24_12))){
							UserConfig.getSingleton().setTime12or24("12");
							AthanService.prayerTimes.setTime12or24(12);
				    	}
				    	else{
				    		UserConfig.getSingleton().setTime12or24("24");
				    		AthanService.prayerTimes.setTime12or24(24);
				    	}
					}
					setUserConfig();
					updateWidget();
				}
				catch(Exception e){}
			    time12or24_value.setText(finalTime12or24);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void athanHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.athan)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.athan));
	    	}
		final String[] athanArray = getResources().getStringArray(R.array.athan_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			for(int i = 0 ; i<athanArray.length ; i++){
				athanArray[i] = ArabicReshape.reshape(athanArray[i]);
			}
		}
		builder.setSingleChoiceItems(athanArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalAthan = (String) athanArray[item];
		    	try{
					
					if(!finalAthan.equals("")){
						if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
							if(finalAthan.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.ali_ben_ahmed_mala)))){
								UserConfig.getSingleton().setAthan("ali_ben_ahmed_mala");
							}else{
								if(finalAthan.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.abd_el_basset_abd_essamad)))){
									UserConfig.getSingleton().setAthan("abd_el_basset_abd_essamad");
								}else{
									if(finalAthan.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.farou9_abd_errehmane_hadraoui)))){
										UserConfig.getSingleton().setAthan("farou9_abd_errehmane_hadraoui");
									}else{
										if(finalAthan.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.mohammad_ali_el_banna)))){
											UserConfig.getSingleton().setAthan("mohammad_ali_el_banna");
										}else{
												UserConfig.getSingleton().setAthan("mohammad_khalil_raml");
										}
									}
								}
							}	
						}
						else{
						if(finalAthan.equalsIgnoreCase(getResources().getString(R.string.ali_ben_ahmed_mala))){
							UserConfig.getSingleton().setAthan("ali_ben_ahmed_mala");
						}else{
							if(finalAthan.equalsIgnoreCase(getResources().getString(R.string.abd_el_basset_abd_essamad))){
								UserConfig.getSingleton().setAthan("abd_el_basset_abd_essamad");
							}else{
								if(finalAthan.equalsIgnoreCase(getResources().getString(R.string.farou9_abd_errehmane_hadraoui))){
									UserConfig.getSingleton().setAthan("farou9_abd_errehmane_hadraoui");
								}else{
									if(finalAthan.equalsIgnoreCase(getResources().getString(R.string.mohammad_ali_el_banna))){
										UserConfig.getSingleton().setAthan("mohammad_ali_el_banna");
									}else{
											UserConfig.getSingleton().setAthan("mohammad_khalil_raml");
									}		
								}			
							}					
						}	
					  }
					}
					setUserConfig();
				}
				catch(Exception e){}
			    athan_value.setText(finalAthan);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void languageHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		final String[] languageArray = getResources().getStringArray(R.array.language_array);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.Language)));
			for(int i = 0 ; i<languageArray.length ; i++){
				languageArray[i] = (ArabicReshape.reshape(languageArray[i])).toString();
			}
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.Language));
	    	}
		builder.setSingleChoiceItems(languageArray, selected, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	selected = item;
		    	finalLanguage = (String) languageArray[item];
		    	try{
					if(!finalLanguage.equals("")){
						if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
							if(finalLanguage.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.arabic)))){
								UserConfig.getSingleton().setLanguage("ar");
							}else{
								if(finalLanguage.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.english)))){
									UserConfig.getSingleton().setLanguage("en");
								}
								else{
									if(finalLanguage.equalsIgnoreCase(ArabicReshape.reshape(getResources().getString(R.string.french)))){
										UserConfig.getSingleton().setLanguage("fr");
									}else{
										UserConfig.getSingleton().setLanguage("it");
									}
								}
							}
							
						}
						else{
						if(finalLanguage.equalsIgnoreCase(getResources().getString(R.string.arabic))){
							UserConfig.getSingleton().setLanguage("ar");
						}else{
							if(finalLanguage.equalsIgnoreCase(getResources().getString(R.string.english))){
								UserConfig.getSingleton().setLanguage("en");
							}
							else{
								if(finalLanguage.equalsIgnoreCase(getResources().getString(R.string.french))){
									UserConfig.getSingleton().setLanguage("fr");
								}else{
									UserConfig.getSingleton().setLanguage("it");
								}
							}
						}
						
						}
					}
					
					updateWidget();
					setUserConfig();
					TabActivity tabs = (TabActivity) getParent();
					tabs.getTabHost().setCurrentTab(3);
				    tabs.getTabHost().setCurrentTab(2);
				}
				catch(Exception e){}
			    language_value.setText(finalLanguage);
		    	alertDialog.dismiss();
		    }
		});
	 this.alertDialog = builder.create();
	 this.alertDialog.show();
	}
	
	public void timeZoneHandler(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.time_zone)));
	    	}
	    	else{
	    		builder.setTitle(getResources().getString(R.string.time_zone));
	    	}
		        builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					finalTimeZone = editText.getText().toString();
					try{
						if(!finalTimeZone.equals("")){
							UserConfig.getSingleton().setTimezone(finalTimeZone);
							}
						setUserConfig();
						refreshService();
					}
					catch(Exception e){}
					timeZone_value.setText(finalTimeZone);
					alertDialog.dismiss();
				}
				});
		LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = li.inflate(R.layout.time_zone_layout, null);
	    
	    this.upButton = (Button) view.findViewById(R.id.upButton);
		this.downButton = (Button) view.findViewById(R.id.downButton);
		this.editText = (EditText) view.findViewById(R.id.numberEditText);
		
		try {
	    	TimeZone timezone = TimeZone.getDefault();
	        String TimeZoneName = timezone.getDisplayName();
			int timeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);

			this.time_zone_calcolated = (TextView) view.findViewById(R.id.time_zone_calcolated);
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				this.time_zone_calcolated.setText(timeZoneOffset+" : "+ArabicReshape.reshape(TimeZoneName));
		    	}
		    	else{
		    		this.time_zone_calcolated.setText(TimeZoneName+" : "+timeZoneOffset);
		    	}
			this.time_zone_calcolated.setVisibility(View.VISIBLE);
			this.timeZoneHijriTimeValue = timeZoneOffset;
		} catch (Exception e) {}
		
		this.editText.setText(String.valueOf(timeZoneHijriTimeValue));
		
		this.upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downButton.setBackgroundResource(R.drawable.timepicker_down_normal);
				upButton.setBackgroundResource(R.drawable.timepicker_up_pressed);
				if (timeZoneHijriTimeValue >= downrange && timeZoneHijriTimeValue <= uprange){
					timeZoneHijriTimeValue = timeZoneHijriTimeValue+0.5;
				}
				if (timeZoneHijriTimeValue > uprange){
					timeZoneHijriTimeValue = 0;
				}
				editText.setText(String.valueOf(timeZoneHijriTimeValue));
			}
		});

			this.downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					downButton .setBackgroundResource(R.drawable.timepicker_down_pressed);
					upButton.setBackgroundResource(R.drawable.timepicker_up_normal);
					if (timeZoneHijriTimeValue >= downrange && timeZoneHijriTimeValue <= uprange)
						timeZoneHijriTimeValue = timeZoneHijriTimeValue-0.5;
					if (timeZoneHijriTimeValue < downrange)
						timeZoneHijriTimeValue = 0;
					editText.setText(String.valueOf(timeZoneHijriTimeValue));
				}
			});
	    builder.setView(view);
		this.alertDialog = builder.create();
		this.alertDialog.show();
	}
	
	public void setCities(){
		DatabaseManager myDbHelper = new DatabaseManager(context);
		 try {
		 myDbHelper.createDataBase();
		 } catch (IOException e) {}
		 try {
		 SQLiteDatabase db = myDbHelper.getReadableDatabase();
		 myDbHelper.openDataBase();
		 Cursor c = db.rawQuery("SELECT city,latitude,longitude,timezone FROM sally WHERE _id='"+finalCountry+"'", null);
		 this.cities = new ArrayList<City>();
		 for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			 City city = new City();
			 city.setName(c.getString(0));
			 city.setLatitude(c.getString(1));
			 city.setLongitude(c.getString(2));
			 city.setTimezone(c.getString(3));
			 cities.add(city);
	        }
		 this.citiesLocations = new String[cities.size()];
			for(int i = 0 ; i < cities.size() ; i++ ){
				citiesLocations[i] = cities.get(i).getName();
			}
			finalCity = (String)cities.get(0).getName();
			if(latitude==0 || longitude==0){
				latitude_fromList = Double.valueOf(cities.get(0).getLatitude())/10000;
	    		longitude_fromList = Double.valueOf(cities.get(0).getLongitude())/10000;
	    	}
			
			try{
				if(!finalCountry.equals("")){
					UserConfig.getSingleton().setCountry(finalCountry);
				}
				
				if(!finalCity.equals("")){
					UserConfig.getSingleton().setCity(finalCity);
				}
				
				if(longitude!=0 && latitude!=0){
					UserConfig.getSingleton().setLongitude(String.valueOf(longitude));
					UserConfig.getSingleton().setLatitude(String.valueOf(latitude));
				}
				else{
					if(longitude_fromList!=0 && latitude_fromList!=0){
						UserConfig.getSingleton().setLongitude(String.valueOf(longitude_fromList));
						UserConfig.getSingleton().setLatitude(String.valueOf(latitude_fromList));
						}
				}
				city_value.setText(citiesLocations[0]);
				gps_location_value.setText(finalCountry+"/"+citiesLocations[0]);
				setUserConfig();
				refreshService();
			}
			catch(Exception e){}
			
		 myDbHelper.close();
		 }catch(SQLException e){}
	}
	
	int selected = 0;
	public void getCountries(){
		if(this.countries==null || this.countries.length==0){ 
			this.progressDialog2 = new ProgressDialog(context,R.style.AlertProgressCustom);
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				this.progressDialog2.setMessage(ArabicReshape.reshape(getResources().getString(R.string.loading)));
		    	}
		    	else{
		    		this.progressDialog2.setMessage(getResources().getString(R.string.loading));
		    	}
			final DatabaseManager myDbHelper = new DatabaseManager(context);
				
			this.setGetCountriesAsynkTask(new AsyncTask<Void, Void, String[]>(){
				@Override
				protected String[] doInBackground(Void... params) {

					 try {
						myDbHelper.createDataBase();
					} catch (IOException e) {}

					 SQLiteDatabase db = myDbHelper.getReadableDatabase();
					 myDbHelper.openDataBase();
					 Cursor c = db.query(true,"sally", new String[]{"_id"}, null, null, null, null, null, null);
					 c.moveToFirst();
					 countries = new String[c.getCount()];
					 int ii=0;
					 for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
						 countries[ii]=c.getString(0);
						 ii++;
				        }
					return countries;
				}

				@Override
				protected void onPostExecute(String[] result) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
					if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
						builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.country)));
				    	}
				    	else{
				    		builder.setTitle(getResources().getString(R.string.country));
				    	}
					builder.setSingleChoiceItems(countries, selected, new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int item) {
					    	selected = item;
					    	finalCountry = (String) countries[item];
					    	country_value.setText(finalCountry);
					    	alertDialog.dismiss();
					    	setCities();
					    }
					});
				 alertDialog = builder.create();
				 progressDialog2.dismiss();
				 alertDialog.show();	
				 myDbHelper.close();
				}

				@Override
				protected void onPreExecute() {
					progressDialog2.show();
				}

				@Override
				protected void onProgressUpdate(Void... values) {
				}
				}.execute());
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.city)));
		    	}
		    	else{
		    		builder.setTitle(getResources().getString(R.string.city));
		    	}
			builder.setSingleChoiceItems(countries, selected, new DialogInterface.OnClickListener(){
				   public void onClick(DialogInterface dialog, int item){
			    	selected = item;
			    	finalCountry = (String) countries[item];
			    	country_value.setText(finalCountry);
			    	alertDialog.dismiss();
			    	setCities();
			    }
			});
		 this.alertDialog = builder.create();
		 this.alertDialog.show();
		}
	}
	
	public void getCities(){
		if(finalCountry.equals("")){
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				Toast.makeText(getApplicationContext(), ArabicReshape.reshape(getResources().getString(R.string.must_first_select_a_country)), Toast.LENGTH_SHORT).show();
		    	}
		    	else{
		    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.must_first_select_a_country), Toast.LENGTH_SHORT).show();
		    	}
		}
		else{
		if(this.citiesLocations==null || this.citiesLocations.length==0){
			DatabaseManager myDbHelper = new DatabaseManager(context);
		 try {
		 myDbHelper.createDataBase();
		  
		 } catch (IOException e) {}
		 try { 
			 SQLiteDatabase db = myDbHelper.getReadableDatabase();
			 myDbHelper.openDataBase();
			 Cursor c = db.rawQuery("SELECT city,latitude,longitude,timezone FROM sally WHERE _id='"+finalCountry+"'", null);
			 this.cities = new ArrayList<City>();
			 for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				 City city = new City();
				 city.setName(c.getString(0));
				 city.setLatitude(c.getString(1));
				 city.setLongitude(c.getString(2));
				 city.setTimezone(c.getString(3));
				 cities.add(city);
		        }
			 this.citiesLocations = new String[cities.size()];
				for(int i = 0 ; i < cities.size() ; i++ ){
					citiesLocations[i] = cities.get(i).getName();
				}
			 myDbHelper.close();
			this.citiesLocations = new String[cities.size()];
			for(int i = 0 ; i < cities.size() ; i++ ){
				citiesLocations[i] = cities.get(i).getName();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.city)));
		    	}
		    	else{
		    		builder.setTitle(getResources().getString(R.string.city));
		    	}
			builder.setSingleChoiceItems(citiesLocations, selected, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	selected = item;
			    	latitude_fromList = Double.valueOf(cities.get(item).getLatitude())/10000;
			    	longitude_fromList = Double.valueOf(cities.get(item).getLongitude())/10000;
			    	finalCity = (String)citiesLocations[item];
			    	try{
						if(!finalCity.equals("")){
							UserConfig.getSingleton().setCity(finalCity);
						}
						
						if(longitude!=0 && latitude!=0){
							UserConfig.getSingleton().setLongitude(String.valueOf(longitude));
							UserConfig.getSingleton().setLatitude(String.valueOf(latitude));
						}
						else{
							if(longitude_fromList!=0 && latitude_fromList!=0){
								UserConfig.getSingleton().setLongitude(String.valueOf(longitude_fromList));
								UserConfig.getSingleton().setLatitude(String.valueOf(latitude_fromList));
								}
						}
						setUserConfig();
						refreshService();
					}
					catch(Exception e){}
			    	city_label.setText(finalCity);
			    	gps_location_value.setText(finalCountry+"/"+finalCity);
			    	alertDialog.dismiss();
			    }
			});
		 this.alertDialog = builder.create();
		 this.alertDialog.show();
		 myDbHelper.close();
		 }catch(SQLException e){}
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.city)));
		    	}
		    	else{
		    		builder.setTitle(getResources().getString(R.string.city));
		    	}
			builder.setSingleChoiceItems(citiesLocations, selected, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	selected = item;
			    	latitude_fromList = Double.valueOf(cities.get(item).getLatitude())/10000;
			    	longitude_fromList = Double.valueOf(cities.get(item).getLongitude())/10000;
			    	finalCity = (String)citiesLocations[item];
			    	try{
						
						if(!finalCity.equals("")){
							UserConfig.getSingleton().setCity(finalCity);
						}
						
						if(longitude!=0 && latitude!=0){
							UserConfig.getSingleton().setLongitude(String.valueOf(longitude));
							UserConfig.getSingleton().setLatitude(String.valueOf(latitude));
						}
						else{
							if(longitude_fromList!=0 && latitude_fromList!=0){
								UserConfig.getSingleton().setLongitude(String.valueOf(longitude_fromList));
								UserConfig.getSingleton().setLatitude(String.valueOf(latitude_fromList));
								}
						}
						setUserConfig();
						refreshService();
					}
					catch(Exception e){}
			    	city_value.setText(finalCity);
			    	gps_location_value.setText(finalCountry+"/"+finalCity);
			    	alertDialog.dismiss();
			    }
			});
		 this.alertDialog = builder.create();
		 this.alertDialog.show();
		}
		}
	}

	public void findLocation(){
		this.progressDialog = new ProgressDialog(context,R.style.AlertProgressCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			this.progressDialog.setMessage(ArabicReshape.reshape(getResources().getString(R.string.loading)));
	    	}
	    	else{
	    		this.progressDialog.setMessage(getResources().getString(R.string.loading));
	    	}
		this.findLocationAsynkTask = new AsyncTask<Void, Void, Location>(){
			@Override
			protected Location doInBackground(Void... params){

				while (longitude == 0.0 || latitude == 0.0) {
                      getLocation(locationManager);
                      if (isCancelled()) break;
	            }

				try {
					 geocoder = new Geocoder(context, Locale.getDefault());
					 addresses = geocoder.getFromLocation(latitude, longitude, 1);
					 address = addresses.get(0).getAddressLine(0);
				     city = addresses.get(0).getLocality();
				     country = addresses.get(0).getCountryName();
				     finalCountry = country;
				     finalCity = city;
				} catch (Exception e) {
					address="";
					country="";
					city="";
				}
				
				//return getLocation(locationManager);
	            return null;
			}
			
			@Override
	        protected void onCancelled(){
				findLocationAsynkTask.cancel(true);
	            progressDialog.dismiss();
	            locationManager.removeUpdates(locationListener);
	        }

			@Override
			protected void onPostExecute(final Location actualLocation){
				
				progressDialog.dismiss();

				AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
				String confirm ="";
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					confirm = ArabicReshape.reshape(getResources().getString(R.string.confirm));
					builder.setTitle(ArabicReshape.reshape(getResources().getString(R.string.latitude))+"/"+ArabicReshape.reshape(getResources().getString(R.string.longitude)));
			    	}
			    	else{
			    		confirm = getResources().getString(R.string.confirm);
			    		builder.setTitle(getResources().getString(R.string.latitude)+"/"+getResources().getString(R.string.longitude));
			    	}
				builder.setPositiveButton(confirm,
						new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							
							country = country_edittext.getText().toString();
							city = city_edittext.getText().toString();
							finalCountry = country;
						    finalCity = city;
							if(country.trim().equals("")){
								country = String.valueOf(latitude);
								finalCountry = "none";
							}
							if(city.trim().equals("")){
								city = String.valueOf(longitude);
								finalCity = "none";
							}
							
							gps_location_value.setText(country+"/"+city+" "+address);
					    	country_value.setText(country);
					    	city_value.setText(city);
					    
							try{
								if(!finalCountry.equals("")){
									UserConfig.getSingleton().setCountry(finalCountry);
								}
								
								if(!finalCity.equals("")){
									UserConfig.getSingleton().setCity(finalCity);
								}
								
								if(longitude!=0 && latitude!=0){
									setDefaultLanguage("en");
									DecimalFormat formatter = new DecimalFormat("##0.00##");
									UserConfig.getSingleton().setLongitude(String.valueOf(formatter.format(longitude)));
									UserConfig.getSingleton().setLatitude(String.valueOf(formatter.format(latitude)));
									try {
								    	TimeZone timezone = TimeZone.getDefault();
										int timeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);
										UserConfig.getSingleton().setTimezone(String.valueOf(timeZoneOffset));
										time_zone_calcolated.setText(timeZoneOffset);
                                        timeZone_value.setText(finalTimeZone);
									} catch (Exception e) {}
									setDefaultLanguage(UserConfig.getSingleton().getLanguage());
								}

								setUserConfig();
								refreshService();
							}
							catch(Exception e){}
							
							alertDialog.dismiss();
						}
						});
				String cancel ="";
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					cancel = ArabicReshape.reshape(getResources().getString(R.string.cancel));
		    	}
		    	else{
		    		cancel = getResources().getString(R.string.cancel);
		    	}
				builder.setNegativeButton(cancel,
						new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							alertDialog.dismiss();
						}
						});
				LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    View view = li.inflate(R.layout.location_layout, null);
			    
			    latitude_value = (TextView) view.findViewById(R.id.latitude_value);
				longitude_value = (TextView) view.findViewById(R.id.longitude_value);
				country_edittext = (EditText) view.findViewById(R.id.country_edittext);
				city_edittext = (EditText) view.findViewById(R.id.city_edittext);
				
				latitude_label_inflat = (TextView) view.findViewById(R.id.latitude_label);
				longitude_label_inflat = (TextView) view.findViewById(R.id.longitude_label);
				country_label_inflat = (TextView) view.findViewById(R.id.country_label);
				city_label_inflat = (TextView) view.findViewById(R.id.city_label);
				
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					latitude_label_inflat.setText(ArabicReshape.reshape(getResources().getString(R.string.latitude)));
					longitude_label_inflat.setText(ArabicReshape.reshape(getResources().getString(R.string.longitude)));
					country_label_inflat.setText(ArabicReshape.reshape(getResources().getString(R.string.country)));
					city_label_inflat.setText(ArabicReshape.reshape(getResources().getString(R.string.city)));
					
					country_edittext.setTypeface(tf);
					city_edittext.setTypeface(tf);
					country_edittext.setText(ArabicReshape.reshape(String.valueOf(country)));
					city_edittext.setText(ArabicReshape.reshape(String.valueOf(city)));
					
					latitude_value.setText(String.valueOf(latitude));
					longitude_value.setText(String.valueOf(longitude));
					
			    	}
			    	else{
			    		latitude_value.setText(String.valueOf(latitude));
						longitude_value.setText(String.valueOf(longitude));
						country_edittext.setText(String.valueOf(country));
						city_edittext.setText(String.valueOf(city));
			    	}

			    builder.setView(view);
				alertDialog = builder.create();
				alertDialog.show();
			}

			@Override
			protected void onPreExecute() {
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						findLocationAsynkTask.cancel(true);
					}
				});
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
			}
			}.execute();
	}
	
	public Location getLocation(LocationManager locationManager) {
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				this.progressDialog.dismiss();
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					Toast.makeText(getApplicationContext(),ArabicReshape.reshape(getResources().getString(R.string.no_network_provider_is_enabled)), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network_provider_is_enabled), Toast.LENGTH_LONG).show();
				}
			} else
				{
					if (isNetworkEnabled) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								if(latitude == 0 || longitude == 0)
								{
									location = null;
								}else
								{
									latitude = location.getLatitude();
									longitude = location.getLongitude();
									return  location;
								}
							}else{
								location = null;
							}
						}
					}

					if (isGPSEnabled) {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								if(latitude == 0 || longitude == 0)
								{
									location = null;
								}else
								{
									latitude = location.getLatitude();
									longitude = location.getLongitude();
								}
							}else{
								location = null;
							}
						}
					}
				}
 
        } catch (Exception e) {
        	location = null;
        }
 
        return location;
    }
 

	@Override
	public void onLocationChanged(Location location) {  
		this.location = location;
        latitude=location.getLatitude();  
        longitude=location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public AsyncTask<Void, Void, Location> getFindLocationAsynkTask() {
		return findLocationAsynkTask;
	}

	public void setFindLocationAsynkTask(AsyncTask<Void, Void, Location> findLocationAsynkTask) {
		this.findLocationAsynkTask = findLocationAsynkTask;
	}

	public AsyncTask<Void, Void, String[]> getGetCountriesAsynkTask() {
		return getCountriesAsynkTask;
	}

	public void setGetCountriesAsynkTask(AsyncTask<Void, Void, String[]> getCountriesAsynkTask) {
		this.getCountriesAsynkTask = getCountriesAsynkTask;
	}
	
	public void FajrTimeHandler(){
		timesAdjustmentHandler(getResources().getString(R.string.fajr) , fajr_time_label , fajr_time_value , "fajr");
	}
	
   public void ShoroukTimeHandler(){
	   timesAdjustmentHandler(getResources().getString(R.string.shorouk) , shorouk_time_label , shorouk_time_value , "shorouk");
	}
   
   public void DuhrTimeHandler(){
	   timesAdjustmentHandler(getResources().getString(R.string.duhr) , duhr_time_label , duhr_time_value , "duhr");
	}
   
   public void AsrTimeHandler(){
	   timesAdjustmentHandler(getResources().getString(R.string.asr) , asr_time_label , asr_time_value , "asr");
	}
   
   public void MaghribTimeHandler(){
	   timesAdjustmentHandler(getResources().getString(R.string.maghrib) , maghrib_time_label , maghrib_time_value , "maghrib");
	}
   
   public void IshaaTimeHandler(){
	   timesAdjustmentHandler(getResources().getString(R.string.ishaa) , ishaa_time_label , ishaa_time_value , "ishaa");
   }
	
	public void timesAdjustmentHandler(final String title , final TextView textViewLabel , final TextView textViewValue , final String property){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			builder.setTitle(ArabicReshape.reshape(title));
	    	}
	    	else{
	    		builder.setTitle(title);
	    	}
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String value = editText.getText().toString();
				try{						
					if(!value.trim().equals("")){
						if(property.equalsIgnoreCase("fajr")){
							UserConfig.getSingleton().setFajr_time(value);
						}
						else{
							if(property.equalsIgnoreCase("shorouk")){
								UserConfig.getSingleton().setShorouk_time(value);
							}
							else{
								if(property.equalsIgnoreCase("duhr")){
									UserConfig.getSingleton().setDuhr_time(value);
								}
								else{
									if(property.equalsIgnoreCase("asr")){
										UserConfig.getSingleton().setAsr_time(value);
									}
									else{
										if(property.equalsIgnoreCase("maghrib")){
											UserConfig.getSingleton().setMaghrib_time(value);
										}
										else{
											UserConfig.getSingleton().setIshaa_time(value);
										}
									}
								}
							}
						}
						}
					setUserConfig();
					refreshService();
				}
				catch(Exception e){}
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					textViewLabel.setText(ArabicReshape.reshape(title));
			    	}
				else{
					textViewLabel.setText(title);
				}
				textViewValue.setText(value);
				alertDialog.dismiss();
			}
		});
		LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = li.inflate(R.layout.time_zone_layout, null);
	    this.upButton = (Button) view.findViewById(R.id.upButton);
		this.downButton = (Button) view.findViewById(R.id.downButton);
		this.editText = (EditText) view.findViewById(R.id.numberEditText);
		
		this.editText.setText("0");
		
		this.upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downButton.setBackgroundResource(R.drawable.timepicker_down_normal);
				upButton.setBackgroundResource(R.drawable.timepicker_up_pressed);
				if (timeZoneHijriTimeValue >= downrange_time && timeZoneHijriTimeValue <= uprange_time){
					timeZoneHijriTimeValue = timeZoneHijriTimeValue+1;
				}
				if (timeZoneHijriTimeValue > uprange_time){
					timeZoneHijriTimeValue = 0;
				}
				editText.setText(String.valueOf(timeZoneHijriTimeValue));
			}
		});
		this.downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downButton .setBackgroundResource(R.drawable.timepicker_down_pressed);
				upButton.setBackgroundResource(R.drawable.timepicker_up_normal);
				if (timeZoneHijriTimeValue >= downrange_time && timeZoneHijriTimeValue <= uprange_time)
					timeZoneHijriTimeValue = timeZoneHijriTimeValue-1;
				if (timeZoneHijriTimeValue < downrange_time)
					timeZoneHijriTimeValue = 0;
				editText.setText(String.valueOf(timeZoneHijriTimeValue));
			}
		});
    builder.setView(view);
	this.alertDialog = builder.create();
	this.alertDialog.show();
	}
	
	
	public void setUserConfig(){
		   try {
			   PreferenceHandler.getSingleton().addUserConfig(UserConfig.getSingleton());
		} catch (Exception e) {
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
				Toast.makeText(getApplicationContext(),ArabicReshape.reshape(e.getMessage()), Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
			finish();
		}
	}
	
	public void refreshService(){
		stopService(new Intent(this,AthanService.class));
		startService(new Intent(this,AthanService.class));
	}
	
	public void updateWidget(){
		context.startService(new Intent(context, MyWidgetProviderService.class));
		context.startService(new Intent(context, MyWidgetProviderService2.class));
	}
	
	private void setDefaultLanguage(String language){
		String languageToLoad = language;
	    Locale locale = new Locale(languageToLoad);
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
	}
	
	@Override
    protected void onStop() {
        super.onStop();     
        try{
        	this.findLocationAsynkTask.cancel(true);
        	locationManager.removeUpdates(locationListener);
            progressDialog.dismiss();
        }catch(Exception e){}
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try{
			this.findLocationAsynkTask.cancel(true);
			locationManager.removeUpdates(locationListener);
            progressDialog.dismiss();
	}catch(Exception e){}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
}
