package com.example.birdsofafeather;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class signinTest {
    ActivityScenario<LoginActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void signinButtonTest() {
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            com.google.android.gms.common.SignInButton signin = activity.findViewById(R.id.sign_in_button);
            assertTrue(signin.getVisibility() == View.VISIBLE);
        });
    }

    @Test
    public void indicatorInitialTest() {
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            TextView indicator = activity.findViewById(R.id.indicator);
            assertEquals("Signed Out", indicator.getText());
        });
    }

    @After
    public void after() { scenario.close(); }
}
