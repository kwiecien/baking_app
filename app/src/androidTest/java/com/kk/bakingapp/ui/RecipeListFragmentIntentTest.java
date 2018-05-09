package com.kk.bakingapp.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.kk.bakingapp.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListFragmentIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        Intents.intending(Matchers.not(IntentMatchers.isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void shouldOpenRecipeDetailWhenListItemClicked() {
        int position = 0;
        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, ViewActions.click()));
        // Nothing to check, no external intents. Below code just for future use
        /*Intents.intended(AllOf.allOf(
                IntentMatchers.hasExtra(key, value),
                IntentMatchers.hasAction(Intent.ACTION_SENDTO))
        );*/
    }

}