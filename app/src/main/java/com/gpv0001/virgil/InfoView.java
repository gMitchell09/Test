package com.gpv0001.virgil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Whitney on 2/25/2017.
 */

public class InfoView extends LinearLayout {
    private TextView lblName, lblStatus, lblType;

    public InfoView(Context context) {
        super(context);
        init(context);
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.info_view, this, true);

        Log.w("Children: ", "" + this.getChildCount());
        lblName = (TextView) findViewById(R.id.info_name);
        lblStatus = (TextView) findViewById(R.id.info_status);
        lblType = (TextView) findViewById(R.id.info_type);

        lblName.setText("Name");
        lblStatus.setText("Status");
        lblType.setText("Type");

    }

    public void setName(String nameText) {
        lblName.setText(nameText);
        invalidate();
        requestLayout();
    }

    public void setStatus(String statusText) {
        lblStatus.setText(statusText);
        invalidate();
        requestLayout();
    }

    public void setType(String typeText) {
        lblType.setText(typeText);
        invalidate();
        requestLayout();
    }
}
