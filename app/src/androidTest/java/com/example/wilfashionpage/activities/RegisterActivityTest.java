package com.example.wilfashionpage.activities;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> RActivityScenarioRule = new ActivityScenarioRule<RegisterActivity>(RegisterActivity.class);

    private ActivityScenario<RegisterActivity> registerActivity = null;

    @Before
    public void setUp() throws Exception {
        registerActivity = RActivityScenarioRule.getScenario();
    }

    @Test
    public void testLoginLaunch() {
        assertNotNull(registerActivity);


    }

    @After
    public void tearDown() throws Exception {
        registerActivity = null;
    }
}