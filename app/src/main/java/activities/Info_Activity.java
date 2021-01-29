package activities;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import classes.ArabicReshape;
import classes.UserConfig;

import com.sallyprayertimes.R;

public class Info_Activity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);

		setDefaultLanguage(UserConfig.getSingleton().getLanguage());

		TextView app_info = findViewById(R.id.app_info);
		TextView app_contact = findViewById(R.id.app_contact);
		TextView app_version = findViewById(R.id.app_version);
		TextView app_footer = findViewById(R.id.app_footer);
		TextView app_site = findViewById(R.id.app_site);
		TextView donation_label_info = findViewById(R.id.donation_label_info);
		TextView donation_url_info = findViewById(R.id.donation_site_info);


		if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
			Typeface tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
			app_info.setTypeface(tf);
			app_contact.setTypeface(tf);
			app_version.setTypeface(tf);
			app_footer.setTypeface(tf);
			app_site.setTypeface(tf);
			donation_label_info.setTypeface(tf);
			donation_url_info.setTypeface(tf);
			app_info.setText(ArabicReshape.reshape(getResources().getString(R.string.app_info)));
			app_contact.setText(ArabicReshape.reshape(getResources().getString(R.string.app_contact)));
			app_version.setText(ArabicReshape.reshape(getResources().getString(R.string.app_version)));
			app_footer.setText(ArabicReshape.reshape(getResources().getString(R.string.app_footer)));
			app_site.setText(ArabicReshape.reshape(getResources().getString(R.string.app_site)));
			donation_label_info.setText(ArabicReshape.reshape(getResources().getString(R.string.donation)+": "+getResources().getString(R.string.donation_text)));
			donation_url_info.setText(ArabicReshape.reshape(getResources().getString(R.string.donation_url)));
		}
		else{
			app_info.setText(getResources().getString(R.string.app_info));
			app_contact.setText(getResources().getString(R.string.app_contact));
			app_version.setText(getResources().getString(R.string.app_version));
			app_footer.setText(getResources().getString(R.string.app_footer));
			app_site.setText(getResources().getString(R.string.app_site));
			donation_label_info.setText(getResources().getString(R.string.donation)+": "+getResources().getString(R.string.donation_text));
			donation_url_info.setText(getResources().getString(R.string.donation_url));
		}
	}

	private void setDefaultLanguage(String language){
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
	}
}
