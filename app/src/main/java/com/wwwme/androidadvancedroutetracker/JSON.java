package com.wwwme.androidadvancedroutetracker;

/**
 * Created by Zlamala on 12.12.2015
 * Tutorial Android SQLite database and content provider - http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * 4.3. Database and Data Model - Comment-Class = JSON-Class
 */

public class JSON {
    private long id;
    private String jsonObject;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJSON() {
        return jsonObject;
    }

    public void setJSON(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return jsonObject;
    }

}

