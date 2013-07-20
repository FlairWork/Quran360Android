package com.flairwork.studyquran.pro;

import com.flairwork.studyquran.pro.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class Preferences extends PreferenceActivity {
		boolean SynchronizePreference;
		boolean SharePreference;
		String emailTextPreference;
	    
		boolean ArabicVersesPreference;

	    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
                
                /*
                // SyncNowPref
                Preference customPref = (Preference) findPreference("syncNowPref");
                customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                	public boolean onPreferenceClick(Preference preference) {
                		try {
                        	SharedPreferences prefs = PreferenceManager .getDefaultSharedPreferences(getBaseContext());
                    		if(prefs.getBoolean("isSynchronizePref", true)){
                    			String userEmail = prefs.getString("emailTextPref", "your@email.com");
                    			if( !userEmail.equals("your@email.com") && !userEmail.equals("") && !userEmail.equals(" ")){
                    				System.out.println("Synchronizing for "+userEmail);
                        			Toast.makeText(getBaseContext(),"Synchronizing for "+userEmail, Toast.LENGTH_LONG).show();
                            		
                        			//SyncNOW
                        			Config.SyncData(getBaseContext());
                        			
                    			}else{
                    				Toast.makeText(getBaseContext(), "Please enter your e-mail", Toast.LENGTH_SHORT).show();
                    			}
                    		}else{
                    			Toast.makeText(getBaseContext(), "Please enable Synchronize and Share", Toast.LENGTH_SHORT).show();
                    		}
						} catch (Exception e) {
							e.printStackTrace();
						}
                        return true;
                    }
                });
                */
        }
        /*
        private void getPrefs() {
            // Get the xml/preferences.xml preferences
            SharedPreferences prefs = PreferenceManager .getDefaultSharedPreferences(getBaseContext());
            
            SynchronizePreference = prefs.getBoolean("isSynchronizePref", true);
            //SharePreference = prefs.getBoolean("isSharePref", true);
            
            emailTextPreference = prefs.getString("emailTextPref", "Nothing has been entered");
            
    		ArabicChaptersPreference = prefs.getBoolean("isArabicChapters", true);

    		ArabicVersesPreference = prefs.getBoolean("isArabicVerses", true);
    		
/*
            ListPreference = prefs.getString("listPref", "nr1");
            
            editTextPreference = prefs.getString("editTextPref",
                            "Nothing has been entered");
            
            ringtonePreference = prefs.getString("ringtonePref",
                            "DEFAULT_RINGTONE_URI");
            
            secondEditTextPreference = prefs.getString("SecondEditTextPref",
                            "Nothing has been entered");
            
            // Get the custom preference
            SharedPreferences mySharedPreferences = getSharedPreferences(
                            "myCustomSharedPrefs", Activity.MODE_PRIVATE);
            
            customPref = mySharedPreferences.getString("myCusomPref", "");

    }
*/        
}