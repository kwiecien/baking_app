package com.kk.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kk.bakingapp.R;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        setupActionBar();
        if (savedInstanceState == null) {
            createNewFragment();
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createNewFragment() {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepFragment.ARG_RECIPE_STEP,
                getIntent().getParcelableExtra(RecipeStepFragment.ARG_RECIPE_STEP));
        recipeStepFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_step_container, recipeStepFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
