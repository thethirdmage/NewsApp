package com.android.example.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {

    /**
     * Custom constructor for the adapter
     * @param context     the current application context
     * @param newsStories an ArrayList of news stories
     */
    public NewsStoryAdapter (Activity context, ArrayList<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }

    /**
     * Gets a view for the adapter to display
     * @param position    the position in the list
     * @param convertView the view to populate
     * @param parent      the parent ViewGroup
     * @return            a View to display
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the view is being reused; otherwise, create a new one
        View newsStoryView = convertView;
        if (newsStoryView == null) {
            newsStoryView = LayoutInflater.from(getContext()).inflate(
                                R.layout.news_item, parent, false);

        }

        // Get the news story at the requested position in the array
        NewsStory currentNewsStory = getItem(position);

        // Get the various TextViews for the story
        TextView titleView = newsStoryView.findViewById(R.id.headline);
        TextView authorView = newsStoryView.findViewById(R.id.author);
        TextView categoryView = newsStoryView.findViewById(R.id.section);
        TextView dateView = newsStoryView.findViewById(R.id.published);

        // Populate the views
        titleView.setText(currentNewsStory.getTitle());
        authorView.setText(currentNewsStory.getAuthor());
        categoryView.setText(currentNewsStory.getCategory());
        dateView.setText(currentNewsStory.getCategory());

        // Finally, return the news story view
        return newsStoryView;
    }

}
