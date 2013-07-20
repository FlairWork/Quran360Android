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

import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.widget.MenuDialog;

import greendroid.app.GDTabActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class TopicsTabActivity extends GDTabActivity {

	private AlertDialog alert;
	
	private static final String TAB0 = "Top";
	private static final String TAB1 = "Recent";
    private static final String TAB2 = "All";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Topics in the Quran");

        addTab(TAB0, "Top", new Intent(this, TopicsTopActivity.class));
        addTab(TAB1, "Recent", new Intent(this, TopicsRecentActivity.class));
        addTab(TAB2, "All", new Intent(this, TopicsActivity.class));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setItems(MenuDialog.menu_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), MenuDialog.menu_items[item], Toast.LENGTH_SHORT).show();
                //route to proper activity
                MenuDialog.RouteActivity(getApplicationContext(), item);
            }
        });
        alert = builder.create();

        addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(R.drawable.menu)
                .setContentDescription(R.string.menu), R.id.action_bar_menu);
        
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
        	case R.id.action_bar_menu:
        		alert.show();        		
        		return true;
        	default:
        		return super.onHandleActionBarItemClick(item, position);
	    }
	}
    

}
