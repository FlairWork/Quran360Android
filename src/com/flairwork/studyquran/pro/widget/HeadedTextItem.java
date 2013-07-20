package com.flairwork.studyquran.pro.widget;


import com.flairwork.studyquran.pro.R;

import greendroid.widget.item.TextItem;
import greendroid.widget.itemview.ItemView;
import android.content.Context;
import android.view.ViewGroup;

public class HeadedTextItem extends TextItem {

    public String headerText;
    
    public HeadedTextItem(String text) {
        super(text);
    } 

    @Override
    public ItemView newView(Context context, ViewGroup parent) {
        return createCellFromXml(context, R.layout.headed_text_item_view, parent);
    }
}
