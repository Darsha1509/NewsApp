package com.example.dasha.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dasha on 20.09.2017.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    public static ArrayList<News> getNews(String urlString){

        String jsonResponse = "";

        try{
            jsonResponse = makeHttpRequest(createUrl(urlString));
        }catch(IOException e){
            Log.e(LOG_TAG, "IO exception in makeHttpRequest/getNews");
        }
        return extractNewsFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException exception){
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line !=null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException{
        Log.e(LOG_TAG, "makeHttpRequest");

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error: response code is " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "IOException getThrown");
        } finally {
            if (urlConnection !=null){
                urlConnection.disconnect();
            }
            if(inputStream !=null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static ArrayList<News> extractNewsFromJson(String jsonResponse){
        ArrayList<News> news = new ArrayList<>();

        try{
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONObject response = rootObject.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            for(int i =0; i<newsArray.length(); i++){
                JSONObject currentResult = newsArray.getJSONObject(i);
                String title = currentResult.getString("webTitle");
                String section = currentResult.getString("sectionName");
                String url = currentResult.getString("webUrl");

                News oneNews = null;
                if(currentResult.has("webPublicationDate")){
                    String date = currentResult.getString("webPublicationDate");
                    oneNews = new News(title, section, date);
                    oneNews.setUrl(url);
                }else{
                    oneNews = new News(title, section);
                    oneNews.setUrl(url);
                }
                news.add(oneNews);
            }

        }catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing the news JSON result", e);
        }
        return news;
    }
}
