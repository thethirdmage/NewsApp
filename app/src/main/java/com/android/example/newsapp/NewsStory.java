package com.android.example.newsapp;

public class NewsStory {
    private String mTitle;
    private String mAuthor;
    private String mCategory;
    private String mDate;
    private String mUrl;

    public NewsStory (String title, String author, String category, String date, String url) {
        mTitle = title;
        mUrl = url;

        // Set up for the text boxes
        mAuthor = "by " + author;
        mCategory = "in " + category;

        // Get the date in a proper format here
        date = date.split("T")[0];
        String[] dateComponents = date.split("-");
        mDate = getMonth(dateComponents[1]) + " " + dateComponents[2] + ", " + dateComponents[0];
    }

    public String getTitle () {
        return mTitle;
    }

    public String getAuthor () {
        return mAuthor;
    }

    public String getCategory () {
        return mCategory;
    }

    public String getDate () {
        return mDate;
    }

    public String getUrl () {
        return mUrl;
    }

    private static String getMonth (String month) {
        switch (month) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return null;
        }
    }
}
