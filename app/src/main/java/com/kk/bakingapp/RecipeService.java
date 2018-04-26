package com.kk.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RecipeService extends IntentService {

    public static final String ACTION_CHANGE_RECIPE = "com.kk.bakingapp.action.change_recipe";
    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.kk.bakingapp.action.update_recipe_widgets";

    public RecipeService() {
        super("RecipeService");
    }

    public static void startActionChangeRecipe(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_CHANGE_RECIPE);
        context.startService(intent);
    }

    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_CHANGE_RECIPE.equals(action)) {
                handleActionChangeRecipe();
            } else if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateRecipeWidgets();
            }
        }
    }

    private void handleActionChangeRecipe() {
        startActionUpdateRecipeWidgets(this);
    }

    private void handleActionUpdateRecipeWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_rl);
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
    }

}
