package com.kk.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kk.bakingapp.data.Ingredient;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.ui.MainActivity;
import com.kk.bakingapp.util.Jsons;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static List<Recipe> mRecipes;
    private static int mRecipeId;


    static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                    int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateRecipeWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                   int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        if (mRecipes != null) {
            mRecipeId = getNewRandomRecipeId();
            setTextViews(views, mRecipes.get(mRecipeId));
        }
        setLaunchApplicationPendingIntent(context, views);
        setChangeRecipePendingIntent(context, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static int getNewRandomRecipeId() {
        int newId = ThreadLocalRandom.current().nextInt(mRecipes.size());
        while (newId == mRecipeId) {
            newId = ThreadLocalRandom.current().nextInt(mRecipes.size());
        }
        return newId;
    }

    private static void setTextViews(RemoteViews views, Recipe recipe) {
        views.setTextViewText(R.id.widget_recipe_name_tv, recipe.getName());
        views.setTextViewText(R.id.widget_recipe_tv, getIngredients(recipe));
    }

    @NonNull
    private static String getIngredients(Recipe recipe) {
        StringBuilder ingredients = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        int index = 1;
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.append(index++).append(". ")
                    .append(ingredient.getName()).append(lineSeparator);
        }
        ingredients.deleteCharAt(ingredients.lastIndexOf(lineSeparator));
        return ingredients.toString();
    }

    private static void setLaunchApplicationPendingIntent(Context context, RemoteViews views) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_rl, pendingIntent);
    }

    private static void setChangeRecipePendingIntent(Context context, RemoteViews views) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(RecipeService.ACTION_CHANGE_RECIPE);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_next_recipe_iv, pendingIntent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeService.startActionUpdateRecipeWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        fetchRecipes(context, RECIPES_URL);
    }

    private void fetchRecipes(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> mRecipes = Jsons.fromJsonArrayToObjects(response, Recipe.class),
                Timber::e);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

