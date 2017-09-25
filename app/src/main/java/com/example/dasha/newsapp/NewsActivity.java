package com.example.dasha.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>{

    private static String THEGUARDIAN_REQUEST_URL = "";/* =
            "http://content.guardianapis.com/search?q=immigration&api-key=test";*/

    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    private static final int NEWS_LOADER_ID = 1;

    NewsAdapter mAdapter;
    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.listview);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                String url = currentNews.getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        loadingSpinner.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "onCreateLoader");

        return new NewsLoader(NewsActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        mAdapter.clear();
        loadingSpinner.setVisibility(View.GONE);
        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }

        Log.e(LOG_TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "onLoadReset");
    }

    private static class NewsLoader extends AsyncTaskLoader<ArrayList<News>>{

        public NewsLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading(){

            Log.e(LOG_TAG, "onStartLoading");
        }

        @Override
        public ArrayList<News> loadInBackground() {
            Log.e(LOG_TAG, "loadInBackground");
            return QueryUtils.getNews(THEGUARDIAN_REQUEST_URL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Fetch the data remotely
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("content.guardianapis.com")
                        .appendPath("search")
                        .appendQueryParameter("q", query)
                        .appendQueryParameter("api-key", "test");

                THEGUARDIAN_REQUEST_URL = builder.build().toString();

                getSupportLoaderManager().initLoader(NEWS_LOADER_ID,null,NewsActivity.this).forceLoad();
                Log.e(LOG_TAG, "initLoader");


                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                NewsActivity.this.setTitle(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {




                return false;
            }
        });
        return true;
    }
}
