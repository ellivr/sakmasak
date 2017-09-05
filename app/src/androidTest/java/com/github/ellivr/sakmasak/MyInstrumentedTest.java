package com.github.ellivr.sakmasak;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by ref on 2017/09/04.
 */

@RunWith(AndroidJUnit4.class)
public class MyInstrumentedTest {

    private Fragment fragment;
    private RecipeListActivity activity;
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class, true, false);

    @Before
    public void init(){
        fragment = new RecipeListFragment();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.github.ellivr.sakmasak", appContext.getPackageName());
    }

    @Test
    public void RecipeListActivity_RecipeList() {

        activity = mActivityTestRule.launchActivity(new Intent());
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recipe_list_fragment, fragment)
                .commit();

        onView(withId(R.id.recipe_list_relativelayout))
                .check(matches(isDisplayed()));

        onView(allOf(withParent(withId(R.id.recipe_list_relativelayout)), withId(R.id.recipe_grid)))
                .check(matches(isDisplayed()));

    }

}
