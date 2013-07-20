package com.flairwork.studyquran.pro.widget;

import com.flairwork.studyquran.pro.R;

import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeadedTextItemView extends LinearLayout implements ItemView {

    private TextView mHeaderView;
    private TextView mTextView; 

    public HeadedTextItemView(Context context) {
        this(context, null);
    }

    public HeadedTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void prepareItemView() {
        mHeaderView = (TextView) findViewById(R.id.gd_separator_text);
        mTextView = (TextView) findViewById(R.id.gd_text);
    }

    public void setObject(Item object) {

        final HeadedTextItem item = (HeadedTextItem) object;
        final String headerText = item.headerText;

        if (!TextUtils.isEmpty(headerText)) {
            mHeaderView.setText(headerText);
            mHeaderView.setVisibility(View.VISIBLE);
        } else {
            mHeaderView.setVisibility(View.GONE);
        }

        mTextView.setText(item.text);
    }

}
