package com.kk.bakingapp.ui;

import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kk.bakingapp.R;
import com.kk.bakingapp.idle.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListFragmentIdlingResourcesTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        RecipeListFragment recipeListFragment = (RecipeListFragment) mActivityTestRule.getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.recipe_list_fragment);
        mIdlingResource = recipeListFragment.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void shouldFetchRecipesWhenCreateRecipeListFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
    }

}