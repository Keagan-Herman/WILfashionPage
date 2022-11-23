package com.example.wilfashionpage.activities;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> lActivityScenarioRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    private ActivityScenario<LoginActivity> loginActivity = null;

    @Before
    public void setUp() throws Exception {
        loginActivity = lActivityScenarioRule.getScenario();
    }

    @Test
    public void testLoginLaunch() {
        assertNotNull(loginActivity);

    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}