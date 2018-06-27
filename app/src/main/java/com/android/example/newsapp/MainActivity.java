package com.android.example.newsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
                          implements LoaderManager.LoaderCallbacks<ArrayList<NewsStory>> {

    private NewsStoryAdapter adapter;
    private TextView emptyView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the empty view and progress bar
        ListView newsStoryList = findViewById(R.id.list);
        emptyView = findViewById(R.id.noConnection);
        newsStoryList.setEmptyView(emptyView);
        progressBar = findViewById(R.id.loading);

        // Get the current network state
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }

        // Check if there is a connection. If so, set up the loader. If not, display to user.
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_connection);
        }

        // Create the adapter, and set it to the ListView
        adapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        newsStoryList.setAdapter(adapter);

        // Create an on-click listener
        newsStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsStory story = adapter.getItem(i);

                // If the story is null, or there is no URL (somehow), just go to The Guardian's website
                String url = "https://www.theguardian.com/";

                // All this eliminates the possibility of producing a NullPointerException
                if (story != null) {
                    url = story.getUrl();
                }

                Intent viewStoryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Check if there's a browser. If so, then go to the story.
                if (viewStoryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(viewStoryIntent);
                }
            }
        });
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

        emptyView.setText(R.string.no_stories);
        progressBar.setVisibility(View.GONE);

        if (stories != null && !stories.isEmpty()) {
            adapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsStory>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Check if the button pressed was the refresh button. If so, refresh.
        if (id == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
            return true;
        }

        // Check if the button pressed was the settings button. If so, open settings.
        if (id == R.id.action_filter) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
