package com.example.dasha.newsapp;

/**
 * Created by Dasha on 20.09.2017.
 */

public class News {
    private String mInfo;
    private String mSection;
    private String mDate;

    public News(String info, String section) {
        mInfo = info;
        mSection = section;
    }

    public News(String info, String section, String date) {
        mInfo = info;
        mSection = section;
        mDate = date;
    }

    public String getInfo() {
        return mInfo;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }
}
