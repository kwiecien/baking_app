package com.kk.bakingapp.util;

import com.kk.bakingapp.data.Recipe;

import java.util.List;

public class Recipes {

    private static List<Recipe> mRecipes;

    public static List<Recipe> getRecipes() {
        return mRecipes;
    }

    public static void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
    }
}
