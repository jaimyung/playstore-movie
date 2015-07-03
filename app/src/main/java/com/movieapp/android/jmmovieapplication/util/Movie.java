package com.movieapp.android.jmmovieapplication.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {

    public static List<MovieItem> ITEMS = new ArrayList<MovieItem>();

    public static Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    static {
        addItem(new MovieItem("1", "Item 1"));
        addItem(new MovieItem("2", "Item 2"));
        addItem(new MovieItem("3", "Item 3"));
    }

    private static void addItem(MovieItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    public static class MovieItem {
        public String id;
        public String content;

        public MovieItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }

        public String getId() { return id;}


    }
}
