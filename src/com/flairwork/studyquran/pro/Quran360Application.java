/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flairwork.studyquran.pro;

import java.io.IOException;

import com.flairwork.studyquran.pro.R;

import android.content.Intent;
import android.net.Uri;
import greendroid.app.GDApplication;

public class Quran360Application extends GDApplication {

    @SuppressWarnings("finally")
	@Override
    public Class<?> getHomeActivityClass() {
    	try{
    		QuranDbAdapter myDBHelper = new QuranDbAdapter(this);

    		myDBHelper.createDataBase();
    		myDBHelper.close();
	    } catch(IOException ioe) {
	    	ioe.printStackTrace();
	    	//throw new Error("Unable to create database");
		} catch(Exception e){
			//System.out.println("Unable to create database");
	    	e.printStackTrace();
	    	//throw new Error(e.getMessage());
	    	//Toast.makeText(getApplicationContext(), "Error: "+e.getCause(), Toast.LENGTH_SHORT).show();
	    }finally{
	    	//return Quran360Activity.class;
	    	return ReadActivity.class;
	    }
    }
    
    @Override
    public Intent getMainApplicationIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_url)));
    }

}
