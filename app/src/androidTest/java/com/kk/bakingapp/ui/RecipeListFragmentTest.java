package com.kk.bakingapp.ui;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kk.bakingapp.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.kk.bakingapp.util.CustomMatchers.CollapsingToolbarLayoutMatcher.withCollapsingToolbarLayoutTitle;
import static com.kk.bakingapp.util.CustomMatchers.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class RecipeListFragmentTest {

    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String BROWNIES = "Brownies";
    private static final String YELLOW_CAKE = "Yellow Cake";
    private static final String CHEESECAKE = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldDisplayRecipes() {
        checkRecipeAtPosition(0, NUTELLA_PIE);
        checkRecipeAtPosition(1, BROWNIES);
        checkRecipeAtPosition(2, YELLOW_CAKE);
        checkRecipeAtPosition(3, CHEESECAKE);
    }

    private void checkRecipeAtPosition(int position, String recipeTitle) {
        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.scrollToPosition(position));
        Espresso.onView(withRecyclerView(R.id.recipe_list_rv).atPosition(position))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(recipeTitle))));
    }

    @Test
    public void shouldOpenCorrectRecipeDetailWhenListItemClicked() {
        String nutellaPieTitle = NUTELLA_PIE;
        int nutellaPiePosition = 0;

        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(nutellaPiePosition, ViewActions.click()));

        Espresso.onView(ViewMatchers.withId(R.id.recipe_detail_ll))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.detail_toolbar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.isAssignableFrom(CollapsingToolbarLayout.class))
                .check(ViewAssertions.matches(withCollapsingToolbarLayoutTitle(Matchers.is(nutellaPieTitle))));
    }

}