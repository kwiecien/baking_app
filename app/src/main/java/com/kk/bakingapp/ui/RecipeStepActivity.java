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
        arguments.putLong(RecipeStepFragment.ARG_RECIPE_ID, getIntent().getLongExtra(RecipeStepFragment.ARG_RECIPE_ID, 0));
        recipeStepFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_step_container, recipeStepFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO
        /*Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(this, upIntent);
        }*/
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
