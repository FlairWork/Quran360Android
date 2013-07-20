package com.flairwork.studyquran.pro;

import com.flairwork.studyquran.pro.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 1000;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash);

        final SplashScreen sPlashScreen = this;   
        
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing

                } finally {
                    // Run next activity
                    Intent intent = new Intent();
                    //intent.setClass(sPlashScreen, StudyQuran.class);
                    //intent.setClass(sPlashScreen, Quran360Activity.class);
                    intent.setClass(sPlashScreen, ReadActivity.class);
                    startActivity(intent); 
                    //stop();
                    finish();
                }
            }
        };

        splashTread.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }
    
    
    
}