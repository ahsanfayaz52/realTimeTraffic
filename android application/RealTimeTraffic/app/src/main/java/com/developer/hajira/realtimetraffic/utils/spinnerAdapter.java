package com.developer.hajira.realtimetraffic.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

public class spinnerAdapter extends ArrayAdapter<String> {

    public spinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        int count = super.getCount();

        return count>0 ? count-1 : count ;


    }


}