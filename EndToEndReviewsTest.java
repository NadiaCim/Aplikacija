package com.example.androidproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * E2E test:
 * 1) Lista u ReviewsActivity je vidljiva
 * 2) Klik na bottom nav otvara SearchActivity i MainActivity
 */
@RunWith(AndroidJUnit4.class)
public class EndToEndReviewsTest {

    @Rule
    public ActivityScenarioRule<ReviewsActivity> rule =
            new ActivityScenarioRule<>(ReviewsActivity.class);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void list_is_visible() {
        onView(withId(R.id.rvTopPerCity)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNav_navigates_to_search_and_home() {
        // Klik na "Pretraga"
        onView(withId(R.id.nav_search)).perform(click());
        intended(hasComponent(SearchActivity.class.getName()));

        // Klik na "Početna"
        onView(withId(R.id.nav_home)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }
}
