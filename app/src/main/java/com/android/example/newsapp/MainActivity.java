package com.android.example.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
                          implements LoaderManager.LoaderCallbacks<ArrayList<NewsStory>> {

    private NewsStoryAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the empty view
        ListView newsStoryList = findViewById(R.id.list);
        emptyView = findViewById(R.id.noConnection);
        newsStoryList.setEmptyView(emptyView);

        // Get the current network state
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // Check if there is a connection. If so, set up the loader. If not, display to user.
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            emptyView.setText(R.string.no_connection);
        }

        // Create the adapter, and set it to the ListView
        adapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        newsStoryList.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsStory>> onCreateLoader(int id, @Nullable Bundle args) {
        String apiKey = getString(R.string.api_key);
        String searchTarget = "world cup 2018";
        String baseString = "http://content.guardianapis.com/search?q=";
        String endingString = "&show-tags=contributor&api-key=";

        // Sample JSON query: http://content.guardianapis.com/search?q=debates&show-tags=contributor&api-key=69f99965-469b-4cb1-823f-fb535b57ff81
        String jsonQuery = baseString + searchTarget + endingString + apiKey;
        return new NewsStoryLoader(this, jsonQuery);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsStory>> loader, ArrayList<NewsStory> stories) {
        adapter.clear();

        if (stories != null && !stories.isEmpty()) {
            adapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsStory>> loader) {
        adapter.clear();
    }
}
