package com.android.example.newsapp;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils () {
        // No constructor necessary -- this just holds JSON query utilities
    }

    /**
     * Creates a URL from the input String
     * @param urlString the string version of the JSON query URL
     * @return          the input as a URL
     */
    private static URL createUrl (String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL.", e);
        }

        return url;
    }

    /**
     * Reads from the input stream
     * @param inputStream  the JSON input stream
     * @return             the JSON response as a string to be parsed
     * @throws IOException if the BufferedReader is empty
     */
    private static String readFromInputStream (InputStream inputStream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();

        // Check if there is an input stream. If so, read it
        if (inputStream != null) {
            // Set up the reader
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferStream = new BufferedReader(streamReader);

            // Read the JSON response
            String line = bufferStream.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = bufferStream.readLine();
            }
        }

        return streamOutput.toString();
    }

    /**
     * Performs the HTTP request to the server
     * @param url          the JSON query URL
     * @return             the parsable JSON response from the server
     * @throws IOException if the InputStream isn't working
     */
    private static String makeHttpRequest (URL url) throws IOException {
        String jsonResponse = "";

        // Check if the URL exists. If it doesn't, return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // Try connecting
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            // If successful, read the input stream and parse response
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Problem connecting. Response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            // Perform cleanup
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Gets the parsable JSON information from the server
     * @param jsonUrlString the URL to be queried, as a String
     * @return              the parsable JSON info
     */
    private static String getJsonInfo (String jsonUrlString) {
        URL jsonUrl = createUrl(jsonUrlString);
        String jsonResponse = null;

        // Try to make an HTTP request with the provided URL and grab results
        try {
            jsonResponse = makeHttpRequest(jsonUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream. ", e);
        }
        return jsonResponse;
    }

    /**
     * Gets the news stories from the JSON query
     * @param jsonQuery the URL to query, as a string
     * @return          an ArrayList of news stories
     */
    public static ArrayList<NewsStory> extractNewsStories (String jsonQuery) {

        ArrayList<NewsStory> newsStories = new ArrayList<>();

        // Try to parse the JSON info given
        try {
            // Grab the JSON info
            String jsonResponse = getJsonInfo(jsonQuery);

            // Grab the results
            JSONObject storyList = new JSONObject(jsonResponse);
            JSONArray stories = storyList.getJSONArray("results");

            // Parse each story, add the relevant information to the list
            for (int i = 0; i < stories.length(); i++) {
                JSONObject story = stories.getJSONObject(i);
                JSONArray authorList = story.getJSONArray("tags");

                // Get author first, as it takes the most work
                StringBuilder authors = new StringBuilder();
                for (int j = 0; j < authorList.length(); j++) {
                    JSONObject author = authorList.getJSONObject(j);
                    authors.append(author.getString("webTitle"));

                    // If the list is not almost done, add a comma
                    if (j < authorList.length() - 1) {
                        authors.append(", ");
                    }
                }

                // Set up the parameters for the constructor
                String title = story.getString("webTitle");
                String author = authors.toString();
                String category = story.getString("sectionName");
                String date = story.getString("webPublicationDate");
                String url = story.getString("webUrl");

                // Create a new NewsStory
                newsStories.add(new NewsStory(title, author, category, date, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON results.", e);
        }

        // Return the list
        return newsStories;
    }
}
