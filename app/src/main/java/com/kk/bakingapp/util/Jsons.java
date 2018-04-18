package com.kk.bakingapp.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Jsons {

    private Jsons() {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @NonNull
    public static <T> List<T> fromJsonArrayToObjects(final JSONArray response, Class<T> clazz) {
        ArrayList<T> objects = new ArrayList<>(response.length());
        for (int i = 0; i < response.length(); i++) {
            try {
                objects.add(new Gson().fromJson(response.get(i).toString(), clazz));
            } catch (JSONException e) {
                Timber.d("Error while parsing response to type!");
            }
        }
        return objects;
    }


}
