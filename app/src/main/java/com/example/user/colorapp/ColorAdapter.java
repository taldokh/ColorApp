package com.example.user.colorapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.colorapp.UnmatchedColor;

import java.util.ArrayList;

public class ColorAdapter extends ArrayAdapter<UnmatchedColor> {

    public ColorAdapter(Activity context, ArrayList<UnmatchedColor> unmatchedColors){
        super(context, 0, unmatchedColors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if( listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        UnmatchedColor currentColors = getItem(position);

        TextView resultTextView = (TextView) listItemView.findViewById(R.id.result);

        resultTextView.setText(currentColors.getResult());

        TextView wrongColorTextView = (TextView) listItemView.findViewById(R.id.wrong_color);

        wrongColorTextView.setText(currentColors.getWrongColor());

        return listItemView;

    }



}
