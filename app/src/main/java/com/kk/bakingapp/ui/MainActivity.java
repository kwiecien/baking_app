package com.kk.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kk.bakingapp.R;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());

        setToolbar();
        if (isInTwoPaneMode()) {
            addRecipeDetailsFragment();
        }
    }

    private void addRecipeDetailsFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

    private boolean isInTwoPaneMode() {
        return findViewById(R.id.recipe_detail_container) != null;
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

}
