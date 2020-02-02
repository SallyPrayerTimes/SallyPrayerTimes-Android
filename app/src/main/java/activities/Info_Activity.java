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

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import classes.ArabicReshape;
import classes.UserConfig;

import com.sally.R;

public class Info_Activity extends Activity {
	
	private TextView app_info;
	private TextView app_contact;
	private TextView app_version;
	private TextView app_footer;
	private TextView app_site;
	private Typeface tf;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		 
		 setDefaultLanguage(UserConfig.getSingleton().getLanguage());
		 
		 this.app_info = (TextView)findViewById(R.id.app_info);
		 this.app_contact = (TextView)findViewById(R.id.app_contact);
		 this.app_version = (TextView)findViewById(R.id.app_version);
		 this.app_footer = (TextView)findViewById(R.id.app_footer);
		 this.app_site = (TextView)findViewById(R.id.app_site);

		 
		 if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			 this.tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
			 this.app_info.setTypeface(tf);
			 this.app_contact.setTypeface(tf);
			 this.app_version.setTypeface(tf);
			 this.app_footer.setTypeface(tf);
			 this.app_site.setTypeface(tf);
			 this.app_info.setText(ArabicReshape.reshape(getResources().getString(R.string.app_info)));
			 this.app_contact.setText(ArabicReshape.reshape(getResources().getString(R.string.app_contact)));
			 this.app_version.setText(ArabicReshape.reshape(getResources().getString(R.string.app_version)));
			 this.app_footer.setText(ArabicReshape.reshape(getResources().getString(R.string.app_footer)));
			 this.app_site.setText(ArabicReshape.reshape(getResources().getString(R.string.app_site)));
		 }
		 else{
			 this.app_info.setText(getResources().getString(R.string.app_info));
			 this.app_contact.setText(getResources().getString(R.string.app_contact));
			 this.app_version.setText(getResources().getString(R.string.app_version));
			 this.app_footer.setText(getResources().getString(R.string.app_footer));
			 this.app_site.setText(getResources().getString(R.string.app_site));
		 }
	}
	
	private void setDefaultLanguage(String language){
		String languageToLoad = language;
	    Locale locale = new Locale(languageToLoad);
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
	}
}
