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

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import classes.ArabicReshape;
import classes.AthanService;
import classes.PreferenceHandler;
import classes.UserConfig;

import com.sally.R;


public class Home_Programe_Activity extends TabActivity{

    private TabHost tabHost;
	private TabSpec prayersTabSpec;
	private TabSpec kiblaTabSpec;
	private TabSpec settingsTabSpec;
	private TabSpec infoTabSpec;
	
	private Intent prayers_activity_intent;
	private Intent kibla_activity_intent;
	private Intent settings_activity_intent;
	private Intent info_activity_intent;
    
	private int drawablePrayersTab;
	private int drawableKiblaTab;
	private int drawableSettingsTab;
	private int drawableInfoTab;
    
    private String prayers;
	private String kibla;
	private String settings;
	private String info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_programe_activity);

		String permission1 = Manifest.permission.ACCESS_FINE_LOCATION;
		if (ContextCompat.checkSelfPermission(getApplicationContext(), permission1) != PackageManager.PERMISSION_GRANTED){
			if(!ActivityCompat.shouldShowRequestPermissionRationale(Home_Programe_Activity.this, permission1)){
				requestPermissions(new String[]{permission1},1);
			}
		}

		changeMainBackground();
		
		this.prayers = getResources().getString(R.string.prayers);
		this.kibla = getResources().getString(R.string.kibla);
		this.settings = getResources().getString(R.string.settings);
		this.info = getResources().getString(R.string.info);
		
		this.tabHost= (TabHost)findViewById(android.R.id.tabhost); 
		
		this.drawablePrayersTab = R.drawable.prayers_tab_style;
		this.drawableKiblaTab = R.drawable.kibla_tab_style;
		this.drawableSettingsTab = R.drawable.settings_tab_style;
		this.drawableInfoTab = R.drawable.info_tab_style;

        this.prayersTabSpec = this.tabHost.newTabSpec(prayers); 
        this.kiblaTabSpec = this.tabHost.newTabSpec(kibla);
        this.settingsTabSpec = this.tabHost.newTabSpec(settings);
        this.infoTabSpec = this.tabHost.newTabSpec(info);
  
        this.prayersTabSpec.setIndicator(getIndicator("",drawablePrayersTab));
        this.kiblaTabSpec.setIndicator(getIndicator("",drawableKiblaTab));
        this.settingsTabSpec.setIndicator(getIndicator("",drawableSettingsTab));
        this.infoTabSpec.setIndicator(getIndicator("",drawableInfoTab));

        this.prayers_activity_intent = new Intent().setClass(Home_Programe_Activity.this,Prayers_Activity.class);
        this.kibla_activity_intent = new Intent().setClass(Home_Programe_Activity.this,Kibla_Activity.class);
        this.settings_activity_intent = new Intent().setClass(Home_Programe_Activity.this,Settings_Activity.class);
        this.info_activity_intent = new Intent().setClass(Home_Programe_Activity.this,Info_Activity.class);
       
        this.prayersTabSpec.setContent(prayers_activity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
        this.kiblaTabSpec.setContent(kibla_activity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));   
        this.settingsTabSpec.setContent(settings_activity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
        this.infoTabSpec.setContent(info_activity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
        
        this.tabHost.addTab(prayersTabSpec);
        this.tabHost.addTab(kiblaTabSpec);
        this.tabHost.addTab(settingsTabSpec);
        this.tabHost.addTab(infoTabSpec);
       
        this.tabHost.getTabWidget().setCurrentTab(0);  	
	}
	
	
	private View getIndicator(String tabTitle, int tabImage)
	  {
	    View localView = View.inflate(this, R.layout.tabs_menu_background, null);
	    
	    if (tabImage != 0){
	      ((ImageView)localView.findViewById(R.id.tab_icon)).setImageResource(tabImage);
	    }
	    return localView;
	  }
	
	public void changeMainBackground()
	{
		    Drawable image;
			switch (AthanService.nextPrayerCode) {
	   		case 1020:
	   			image = getResources().getDrawable(R.drawable.fajr_background); break;
	   		case 1021:
	   			image = getResources().getDrawable(R.drawable.shorou9_background); break;
	   		case 1022:
	   			image = getResources().getDrawable(R.drawable.duhr_background); break;
	   		case 1023:
	   			image = getResources().getDrawable(R.drawable.asr_background); break;
	   		case 1024:
	   			image = getResources().getDrawable(R.drawable.maghrib_background); break;
	   		case 1025:
	   			image = getResources().getDrawable(R.drawable.ishaa_background); break;
	   		default: image = getResources().getDrawable(R.drawable.shorou9_background); break;
	   		}
			LinearLayout layout = (LinearLayout)this.findViewById(R.id.mainBackground);
			layout.setBackgroundDrawable(image);
	}
}
