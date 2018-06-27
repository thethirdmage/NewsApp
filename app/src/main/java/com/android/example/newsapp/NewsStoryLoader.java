package com.android.example.newsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import java.util.ArrayList;

public class NewsStoryLoader extends AsyncTaskLoader<ArrayList<NewsStory>> {

    private String mUrl;

    NewsStoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsStory> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.extractNewsStories(mUrl);
    }
}
