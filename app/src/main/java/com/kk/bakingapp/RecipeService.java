package com.kk.bakingapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RecipeService extends IntentService {

    public static final String ACTION_CHANGE_RECIPE = "com.kk.bakingapp.action.change_recipe";

    public RecipeService() {
        super("RecipeService");
    }

    public static void startActionChangeRecipe(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_CHANGE_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_CHANGE_RECIPE.equals(action)) {
                handleActionChangeRecipe();
            }
        }
    }

    private void handleActionChangeRecipe() {
        // TODO Provide sensible implementation
    }

}
