package com.movieapp.android.jmmovieapplication.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jmyoo on 2015-06-24.
 */
public class JSONUtil {
    private static final String LOG_TAG = "JSONUtil";

    public static Record jsonHttpRequestGET(String url) throws IOException {

        String json = "";
        Log.d("LOG_TAG", "jsonHttpRequestGET " + url);

        Record result = new Record();
        try {

            URL urlString = new URL(url);
            URLConnection weatherBugConnection = urlString.openConnection();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            weatherBugConnection.getInputStream()));
            String inputLine;
            // Keep reading lines until there is no more to be read
            while ((inputLine = input.readLine()) != null) {
                json += inputLine;
            }
            input.close();

        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (json == null) {
            Log.d("LOG_TAG","==========>  null ");
            return null;
        }

        try {
            Log.d("jmyoo"," try ==========>");
            JSONObject obj = new JSONObject(json);
            Log.e(LOG_TAG, "JSON: " + json);
            if (obj != null)
            {
                result = jsonToMap(obj);
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG, "Error: " + e.toString());
           /* Log.e(LOG_TAG, "JSON: " + json);*/
        }

        return result;
    }

    public static Record jsonToMap(JSONObject json) throws JSONException {
        //Map<String, Object> retMap = new HashMap<String, Object>();
        Record retMap = new Record();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Record toMap(JSONObject object) throws JSONException {
        //Map<String, Object> map = new HashMap<String, Object>();
        Record map = new Record();


        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static JSONObject jsonHttpRequest(String url) throws IOException {

        String json = "";
        Log.d("LOG_TAG", "jsonHttpRequest " + url);

        try {
            URL urlString = new URL(url);
            URLConnection weatherBugConnection = urlString.openConnection();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            weatherBugConnection.getInputStream()));
            String inputLine;
            // Keep reading lines until there is no more to be read
            while ((inputLine = input.readLine()) != null) {
                json += inputLine;
            }
            input.close();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        if (json == null) {
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            Log.e(LOG_TAG, "JSON: " + json);
            if (obj != null)
            {
                return obj;
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG, "Error: " + e.toString());
        }
        return null;
    }
}
