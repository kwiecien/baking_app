package com.kk.bakingapp.ui;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers.Visibility;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Ingredient;
import com.kk.bakingapp.data.Measure;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.util.Recipes;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class RecipeStepActivityTest {

    @Rule
    public ActivityTestRule<RecipeStepActivity> mActivityTestRule =
            new ActivityTestRule<RecipeStepActivity>(RecipeStepActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra(RecipeStepFragment.ARG_RECIPE_STEP, Parcels.wrap(createStep(0)));
                    intent.putExtra(RecipeStepFragment.ARG_RECIPE_ID, 1L);
                    return intent;
                }
            };

    @BeforeClass
    public static void addGlobalRecipes() {
        Recipes.setRecipes(Collections.singletonList(createRecipe()));
    }

    private static Recipe createRecipe() {
        return new Recipe(
                1,
                "Nutella Pie",
                Collections.singletonList(new Ingredient(2.0, Measure.CUP, "Graham Cracker crumbs")),
                Arrays.asList(createStep(0), createStep(1)),
                0,
                "");
    }

    private static Recipe.Step createStep(int id) {
        return new Recipe.Step(id,
                "Recipe Introduction",
                "Recipe Description",
                "http://www.abc.com",
                null);
    }

    @Test
    public void shouldDisplayStepDetails() {
        onView(withId(R.id.recipe_step_container))
                .check(matches(isDisplayed()));
        onView(isAssignableFrom(SimpleExoPlayerView.class))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recipe_step_ll))
                .check(matches(allOf(
                        isAssignableFrom(LinearLayout.class),
                        hasDescendant(isAssignableFrom(TextView.class)),
                        hasChildCount(2))));
        onView(withContentDescription(R.string.previous_step))
                .check(matches(is(notNullValue())));
        onView(withContentDescription(R.string.next_step))
                .check(matches(is(notNullValue())));
    }

    @Test
    public void shouldSetVisibilityOfButtons() {
        onView(withContentDescription(R.string.previous_step))
                .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)));
        onView(withContentDescription(R.string.next_step))
                .check(matches(isDisplayed()));
    }

}