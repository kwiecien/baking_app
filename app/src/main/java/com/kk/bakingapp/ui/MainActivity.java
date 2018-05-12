package com.kk.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.databinding.ActivityMainBinding;
import com.kk.bakingapp.viewmodel.RecipeViewModel;

import org.parceler.Parcels;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements RecipeListFragment.OnRecipeClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setRecipeViewModel(new RecipeViewModel(new Recipe()));
        Timber.plant(new Timber.DebugTree());

        setToolbar();
        if (isInTwoPaneMode()) {
            addRecipeDetailsFragment();
            showEmptyView();
        }
    }

    private void addRecipeDetailsFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

    private void showEmptyView() {
        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        findViewById(R.id.recipe_detail_with_title_ll).setVisibility(View.GONE);
    }

    private boolean isInTwoPaneMode() {
        return findViewById(R.id.recipe_detail_container) != null;
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        if (isInTwoPaneMode()) {
            showRecipeDetailView();
            showSelectedRecipe(recipe);
            mBinding.getRecipeViewModel().setRecipe(recipe);
        } else {
            startRecipeDetailActivity(recipe);
        }
    }

    private void showRecipeDetailView() {
        findViewById(R.id.empty_view).setVisibility(View.GONE);
        findViewById(R.id.recipe_detail_with_title_ll).setVisibility(View.VISIBLE);
    }

    private void showSelectedRecipe(Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeDetailFragment.ARG_RECIPE, Parcels.wrap(recipe));
        arguments.putLong(RecipeDetailFragment.ARG_RECIPE_ID, recipe.getId());
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_container, fragment)
                .commit();
    }

    private void startRecipeDetailActivity(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailFragment.ARG_RECIPE, Parcels.wrap(recipe));
        intent.putExtra(RecipeDetailFragment.ARG_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
