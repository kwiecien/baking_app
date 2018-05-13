package com.kk.bakingapp.ui;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Ingredient;
import com.kk.bakingapp.data.Measure;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.util.Recipes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.kk.bakingapp.util.CustomMatchers.CollapsingToolbarLayoutMatcher.withCollapsingToolbarLayoutTitle;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    Recipe recipe = createRecipe();
                    intent.putExtra(RecipeDetailFragment.ARG_RECIPE, Parcels.wrap(recipe));
                    return intent;
                }
            };

    private Recipe mRecipe = createRecipe();

    @Before
    public void addGlobalRecipes() {
        Recipes.setRecipes(Collections.singletonList(mRecipe));
    }

    @Test
    public void shouldDisplayRecipeDetails() {
        onView(isAssignableFrom(CollapsingToolbarLayout.class))
                .check(matches(withCollapsingToolbarLayoutTitle(is("Nutella Pie"))));
        onView(withText("Ingredients"))
                .check(matches(isDisplayed()));
        onView(withText("Measure"))
                .check(matches(isDisplayed()));
        onView(withText("Quantity"))
                .check(matches(isDisplayed()));
        onView(withText("Ingredient"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_rv))
                .check(matches(isDisplayed()));
        onView(withText("Steps"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.steps_rv))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenRecipeStepWhenRecipeStepItemClicked() {
        onView(withId(R.id.steps_rv))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_step_container))
                .check(ViewAssertions.matches(is(notNullValue())));
    }

    private Recipe createRecipe() {
        return new Recipe(
                1,
                "Nutella Pie",
                Collections.singletonList(new Ingredient(2.0, Measure.CUP, "Graham Cracker crumbs")),
                Collections.singletonList(new Recipe.Step(1, "Recipe Introduction", "Recipe Description", "http://www.abc.com", null)),
                0,
                "");
    }

}