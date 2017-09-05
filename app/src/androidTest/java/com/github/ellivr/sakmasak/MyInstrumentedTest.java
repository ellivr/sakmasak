package com.github.ellivr.sakmasak;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyInstrumentedTest {

    final String TEST_RECIPENAME = "Nutella Pie";
    final int TEST_RECIPEPOS = 0;
    final String TEST_INGREDIENTSSTRING = "Ingredients for " + TEST_RECIPENAME;

    final int TEST_STEPPOS = 1;
    final String TEST_STEPDESCRIPTION = "Starting prep";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.github.ellivr.sakmasak", appContext.getPackageName());
    }

    @Test
    public void CheckingTheRecipeList() {
        onView(ViewMatchers.withId(R.id.recipe_grid))
                .check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.recipe_grid)).perform(RecyclerViewActions.scrollToPosition(TEST_RECIPEPOS));
        onView(withText(TEST_RECIPENAME)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckingTheRecipeDetails(){
        onView(ViewMatchers.withId(R.id.recipe_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(TEST_RECIPEPOS,click()));
        onView(withId(R.id.detail_title)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_ingredients)).check(matches(isDisplayed()));
        onView(allOf(withParent(withId(R.id.detail_steps_relativelayout)), withId(R.id.detail_steps)))
                .check(matches(isDisplayed()));

        onView(withText(TEST_INGREDIENTSSTRING)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckingTheStepDetails(){
        onView(ViewMatchers.withId(R.id.recipe_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(TEST_RECIPEPOS,click()));
        onView(ViewMatchers.withId(R.id.detail_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(TEST_STEPPOS,click()));

        onView(withId(R.id.btn_next)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_prev)).check(matches(isDisplayed()));
        onView(withId(R.id.step_short_description)).check(matches(isDisplayed()));
        onView(withId(R.id.step_description)).check(matches(isDisplayed()));

        onView(withText(TEST_STEPDESCRIPTION)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
