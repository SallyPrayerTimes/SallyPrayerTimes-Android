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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import classes.PreferenceHandler;
import widget.MyWidgetProvider;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import classes.ArabicReshape;
import classes.AthanService;
import classes.KiblaDirectionCalculator;
import classes.UserConfig;
import widget.MyWidgetProvider2;

import com.sally.R;


public class Kibla_Activity extends Activity implements LocationListener , SensorEventListener{

	private boolean sensorrunning;
	private float[] mGravity;
    private float[] mMagnetic;
	private float currentDegree1 = 0.0f;
	private float currentDegree2 = 0.0f;
	private float currentDegree3 = 0.0f;
	private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private float kiblaDegree = 0.0F;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	private double latitude = 0;
	private double longitude = 0;
    private AsyncTask<Void, Void, Location> findLocationAsynkTask;
	private TextView kibla_location_text;
	private TextView kibla_warning_text;
	private Typeface tf;
	private String address;
	private String city;
    private String country;
	private Geocoder geocoder;
	private List<Address> addresses = null;
	private LocationListener locationListener;
	private ImageView imageCompass;
	private ImageView imagePointer;
	private RelativeLayout imageLayout;
	private LocationManager locationManager;
	private Location actualLocation;
	private SensorManager mySensorManager;
	private Sensor myAccelerometer;
	private Sensor myField;
	private boolean isSensorActive = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kibla_activity);
		
		this.mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.myField = mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

		if (mySensors.size() > 0) {
			this.mySensorManager.registerListener(this,mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			this.sensorrunning = true;
		} else {
			this.sensorrunning = false;
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
     		 Toast.makeText(getApplicationContext(),ArabicReshape.reshape(getResources().getString(R.string.sensor_is_not_exist)) , Toast.LENGTH_LONG).show();  
     	 }else{
     		 Toast.makeText(getApplicationContext(),getResources().getString(R.string.sensor_is_not_exist) , Toast.LENGTH_LONG).show();  
     	 }
			 this.isSensorActive = false;
		}
		
	    this.imageCompass = ((ImageView)findViewById(R.id.imageViewCompass));
	    this.imagePointer = ((ImageView)findViewById(R.id.imageViewPointer));
	    this.imageLayout = ((RelativeLayout)findViewById(R.id.image_layout));
	      
	    this.imageCompass.setDrawingCacheEnabled(true);
	    this.imagePointer.setDrawingCacheEnabled(true);
	    this.imageLayout.setDrawingCacheEnabled(true);
	      
		this.longitude = Double.parseDouble(UserConfig.getSingleton().getLongitude());
		this.latitude = Double.parseDouble(UserConfig.getSingleton().getLatitude());
        
        
		this.actualLocation = new Location("actualLocation");
		this.actualLocation.setLongitude(longitude);
		this.actualLocation.setLatitude(latitude);
		
	    this.kibla_location_text = (TextView)findViewById(R.id.kibla_location);
		this.kibla_warning_text = (TextView)findViewById(R.id.kibla_warning);
		
        this.kiblaDegree = KiblaDirectionCalculator.getQiblaDirectionFromNorth(longitude, latitude);
		
		this.locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.locationListener = new Kibla_Activity();
		
		if(isSensorActive == false)
		{
			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
    			this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
    			this.kibla_location_text.setTypeface(tf);
    			this.kibla_warning_text.setTypeface(tf);
    			this.kibla_location_text.setText(ArabicReshape.reshape(getResources().getString(R.string.sensor_is_not_exist)));
    			this.kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
    		}else{
    			this.kibla_location_text.setText(getResources().getString(R.string.sensor_is_not_exist));
    			this.kibla_warning_text.setText(getResources().getString(R.string.kibla_warning));
    		}
		}
		else
		{
		 if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { 
			 if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
	    			this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
	    			this.kibla_location_text.setTypeface(tf);
	    			this.kibla_warning_text.setTypeface(tf);
	    			 if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
	    				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
	    				}
	    				else{
	    					this.kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry()));
	    				}
	    			this.kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
	    		}else{
	    			if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
	    				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
	    				}
	    				else{
	    					this.kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry());
	    				}
	    			this.kibla_warning_text.setText(getResources().getString(R.string.kibla_warning));
	    		}
         	getLocation(locationManager);
         	findLocation(); 	            	
           } else { 
         	  if(isNetworkAvailable())
         	  {
         		 if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
 	    			this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
 	    			this.kibla_location_text.setTypeface(tf);
 	    			this.kibla_warning_text.setTypeface(tf);
 	    			 if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
 	    				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
 	    				}
 	    				else{
 	    					this.kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry()));
 	    				}
 	    			this.kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
 	    		}else{
 	    			if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
 	    				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
 	    				}
 	    				else{
 	    					this.kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry());
 	    				}
 	    			this.kibla_warning_text.setText(getResources().getString(R.string.kibla_warning));
 	    		}
         		  getLocation(locationManager);
		          findLocation();
         	  }else
         	  {
         		 if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
	         			this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
	         			this.kibla_location_text.setTypeface(tf);
	         			this.kibla_warning_text.setTypeface(tf);
	         			 if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
	         				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude()+"\n"+ArabicReshape.reshape(getResources().getString(R.string.gps_is_not_on)));
	         				}
	         				else{
	         					this.kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry()+"\n"+ArabicReshape.reshape(getResources().getString(R.string.gps_is_not_on))));
	         				}
	         			this.kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
	            	 }else{
	         			if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
	         				this.kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude()+"\n"+getResources().getString(R.string.gps_is_not_on));
	         				}
	         				else{
	         					this.kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry()+"\n"+getResources().getString(R.string.gps_is_not_on));
	         				}
	         			this.kibla_warning_text.setText(getResources().getString(R.string.kibla_warning)); 
	         		} 
         	  } 
           }
	    }
		
	}

	public void findLocation(){
		this.findLocationAsynkTask = new AsyncTask<Void, Void, Location>(){
			@Override
			protected Location doInBackground(Void... params) {
				
				while (longitude == 0.0 || latitude == 0.0) {
                      getLocation(locationManager);
                      if (isCancelled()) break;
	            }

				try {
					 geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
					 addresses = geocoder.getFromLocation(latitude, longitude, 1);
					 address = addresses.get(0).getAddressLine(0);
				     city = addresses.get(0).getLocality();
				     country = addresses.get(0).getCountryName();
				} catch (Exception e) {
					address="";
					country="";
					city="";
				}

				return null;
			}
			
			@Override
	        protected void onCancelled(){
				findLocationAsynkTask.cancel(true);
	            locationManager.removeUpdates(locationListener);
	        }

			@Override
			protected void onPostExecute(Location result) {
				
			    kiblaDegree = KiblaDirectionCalculator.getQiblaDirectionFromNorth(longitude,latitude);
				
				if(country != null && !country.equalsIgnoreCase(""))
				{
					UserConfig.getSingleton().setCountry(country);
				}
				
				if(city != null && !city.equalsIgnoreCase(""))
				{
					UserConfig.getSingleton().setCity(city);
				}
				
				if(longitude != 0.0 && latitude != 0.0)
					{
					try{
						setDefaultLanguage("en");
						DecimalFormat formatter = new DecimalFormat("##0.00##");
						UserConfig.getSingleton().setLongitude(String.valueOf(formatter.format(longitude)));
						UserConfig.getSingleton().setLatitude(String.valueOf(formatter.format(latitude)));
						setDefaultLanguage(UserConfig.getSingleton().getLanguage());

						PreferenceHandler.getSingleton().addUserConfig(UserConfig.getSingleton());
						//setUserConfig();
					}catch(Exception ex){}
					}
				
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
					tf = Typeface.createFromAsset(getAssets(), "arabic_font.ttf");
					kibla_location_text.setTypeface(tf);
					kibla_warning_text.setTypeface(tf);
					if(address!=null && !address.equals("")){
						kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry())+"\n"+ArabicReshape.reshape(address));
					}
					else{
					 if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
						kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
						}
						else{
							kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry()));
						}
				}
					kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
				}else{
					if(address!=null && !address.equals("")){
						kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry()+"\n"+address);
					}
					else{
					if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
						kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
						}
						else{
							kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry());
						}
				}
					kibla_warning_text.setText(getResources().getString(R.string.kibla_warning));
				}
				
			}
			@Override
			protected void onPreExecute() {
				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
 	    			tf = Typeface.createFromAsset(getAssets(), "arabic_font.ttf");
 	    			kibla_location_text.setTypeface(tf);
 	    			kibla_warning_text.setTypeface(tf);
 	    			 if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
 	    				kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
 	    				}
 	    				else{
 	    					kibla_location_text.setText(ArabicReshape.reshape(UserConfig.getSingleton().getCity())+" - "+ArabicReshape.reshape(UserConfig.getSingleton().getCountry()));
 	    				}
 	    			kibla_warning_text.setText(ArabicReshape.reshape(getResources().getString(R.string.kibla_warning)));
 	    		}else{
 	    			if(UserConfig.getSingleton().getCountry().equalsIgnoreCase("none") || UserConfig.getSingleton().getCity().equalsIgnoreCase("none")){
 	    				kibla_location_text.setText(UserConfig.getSingleton().getLongitude()+" - "+UserConfig.getSingleton().getLatitude());
 	    				}
 	    				else{
 	    					kibla_location_text.setText(UserConfig.getSingleton().getCity()+" - "+UserConfig.getSingleton().getCountry());
 	    				}
 	    			kibla_warning_text.setText(getResources().getString(R.string.kibla_warning));
 	    		}
			}
			@Override
			protected void onProgressUpdate(Void... values){
			}
			}.execute();
	}
	
	public Location getLocation(LocationManager locationManager) {
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager != null) {
                        actualLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (actualLocation != null) {
                            latitude = actualLocation.getLatitude();
                            longitude = actualLocation.getLongitude();
                            if(latitude == 0 || longitude == 0)
                            {
                            	actualLocation = null;
                            }else
                            {
                                latitude = actualLocation.getLatitude();
                                longitude = actualLocation.getLongitude();
                                kiblaDegree = KiblaDirectionCalculator.getQiblaDirectionFromNorth(longitude,latitude);
                            }
                        }else{
                        	actualLocation = null;
                        }
                    }
                }

                if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                        	actualLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (actualLocation != null) {
                            	latitude = actualLocation.getLatitude();
                                longitude = actualLocation.getLongitude();
                                if(latitude == 0 || longitude == 0)
                                {
                                	actualLocation = null;
                                }else
                                {
                                    latitude = actualLocation.getLatitude();
                                    longitude = actualLocation.getLongitude();
                                    kiblaDegree = KiblaDirectionCalculator.getQiblaDirectionFromNorth(longitude,latitude);
                                }
                            }else{
                            	actualLocation = null;
                            }
                        }
                }
 
        } catch (Exception e) {
        	actualLocation = null;
        }
 
        return actualLocation;
    }

	@Override
	public void onSensorChanged(SensorEvent event){
		switch(event.sensor.getType()) {
	        case Sensor.TYPE_ACCELEROMETER:
	            mGravity = event.values.clone();
	            break;
	        case Sensor.TYPE_MAGNETIC_FIELD:
	            mMagnetic = event.values.clone();
	            break;
	        default:
	            return;
	        }

			if (mGravity != null && mMagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mMagnetic);
			if (success) {
			float orientation[] = new float[3];
			SensorManager.getOrientation(R, orientation);
	
		    float f = (float)Math.round(360.0D * orientation[0] / 6.283180236816406D);
			setComapssWithLocation(f);
			}
		}
	}
	
	
	public AsyncTask<Void, Void, Location> getFindLocationAsynkTask() {
		return findLocationAsynkTask;
	}
	public void setFindLocationAsynkTask(AsyncTask<Void, Void, Location> findLocationAsynkTask) {
		this.findLocationAsynkTask = findLocationAsynkTask;
	}
	
	@Override
	public void onLocationChanged(Location location) { 
        this.actualLocation = location;
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.kiblaDegree = KiblaDirectionCalculator.getQiblaDirectionFromNorth(longitude,latitude);
	}
	
	@Override
	public void onProviderDisabled(String provider) {}
	@Override
	public void onProviderEnabled(String provider) {}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sensorrunning) {
			mySensorManager.unregisterListener(this);
		}
	}
	
	protected void onResume() {
        super.onResume();
        this.mySensorManager.registerListener(this, myAccelerometer, SensorManager.SENSOR_DELAY_UI);
        this.mySensorManager.registerListener(this, myField, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(this);
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
	
	@Override
    protected void onStop() {
        super.onStop();     
        try{
        	this.findLocationAsynkTask.cancel(true);
        	locationManager.removeUpdates(locationListener);
        	if (sensorrunning) {
    			mySensorManager.unregisterListener(this);
    		}
        }catch(Exception e){}
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try{
			this.findLocationAsynkTask.cancel(true);
			locationManager.removeUpdates(locationListener);
			if (sensorrunning) {
				mySensorManager.unregisterListener(this);
			}
	}catch(Exception e){}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

		stopService(new Intent(this,AthanService.class));
		startService(new Intent(this,AthanService.class));

		try//update large widget
		{
			Intent intent = new Intent(getApplicationContext() , MyWidgetProvider.class);
			intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MyWidgetProvider.class));
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
			sendBroadcast(intent);
		}catch(Exception e){}

		try//update small widget
		{
			Intent intent = new Intent(getApplicationContext() , MyWidgetProvider2.class);
			intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MyWidgetProvider2.class));
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
			sendBroadcast(intent);
		}catch(Exception e){}
	}
	
	private void setDefaultLanguage(String language){
		String languageToLoad = language;
	    Locale locale = new Locale(languageToLoad);
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
	}

	  public void setComapssWithLocation(float paramFloat)
	  {
	    RotateAnimation localRotateAnimation1 = new RotateAnimation(this.currentDegree1, -this.kiblaDegree, 1, 0.5F, 1, 0.5F);
	    localRotateAnimation1.setInterpolator(new LinearInterpolator());
	    localRotateAnimation1.setDuration(1000);
	    localRotateAnimation1.setFillAfter(true);
	    this.imageCompass.startAnimation(localRotateAnimation1);
	    this.currentDegree1 = (-this.kiblaDegree);
	    
	    float f1 = (float)(0.01744444444444445D * (this.currentDegree1 + this.kiblaDegree));
	    RotateAnimation localRotateAnimation2 = new RotateAnimation(this.currentDegree2, -f1, 1, 0.5F, 1, 0.5F);
	    localRotateAnimation2.setInterpolator(new LinearInterpolator());
	    localRotateAnimation2.setDuration(1000);
	    localRotateAnimation2.setFillAfter(true);
	    this.imagePointer.startAnimation(localRotateAnimation2);
	    this.currentDegree2 = (-f1);
	    
	    float f2 = paramFloat - this.kiblaDegree;
	    RotateAnimation localRotateAnimation3 = new RotateAnimation(this.currentDegree3, -f2, 1, 0.5F, 1, 0.5F);
	    localRotateAnimation3.setInterpolator(new LinearInterpolator());
	    localRotateAnimation3.setDuration(1000);
	    localRotateAnimation3.setFillAfter(true);
	    this.imageLayout.startAnimation(localRotateAnimation3);
	    this.currentDegree3 = (-f2);
	  }
	
}
