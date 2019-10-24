package com.zaffierce.taskplanner;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void headingIsVisible() {
        onView(withText("こんにちは")).check(matches(isDisplayed()));
    }

    @Test
    public void enteringItemNameResultsInItemNameBeingDisplayed() {
//        onView(withId(R.id.editText)).perform(typeText("thing!"));
//        onView(withId(R.id.button)).perform(click());
//        onView(withId(R.id.itemTitle)).check(matches(withSubstring("thing!")));
    }
}