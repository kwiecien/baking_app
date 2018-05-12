package com.kk.bakingapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.kk.bakingapp.data.Recipe;

public class RecipeViewModel extends BaseObservable {
    private Recipe mRecipe;

    public RecipeViewModel(Recipe recipe) {
        mRecipe = recipe;
    }

    @Bindable
    public String getName() {
        return mRecipe.getName();
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        notifyChange();
    }
}
