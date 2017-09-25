package com.example.dasha.newsapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dasha on 20.09.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();


    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        News currentNews = getItem(position);

        TextView text = (TextView) listItemView.findViewById(R.id.text);
        text.setText(currentNews.getInfo());

        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentNews.getSection());

        if(currentNews.getDate() !=null){
            String formatDate = currentNews.getDate().substring(0,10);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date dateFromFormat =null;
            try{
                dateFromFormat = format.parse(formatDate);
            }catch(ParseException e){
                Log.e(LOG_TAG, "ParseException");
            }

            format.applyPattern("dd MMM yyyy");
            String formatedDate = format.format(dateFromFormat);

            TextView date = (TextView) listItemView.findViewById(R.id.date);
            date.setText(formatedDate);
        }

        return listItemView;
    }
}
