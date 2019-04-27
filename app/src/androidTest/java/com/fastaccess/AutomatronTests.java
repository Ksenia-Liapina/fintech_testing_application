package com;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.fastaccess.R;
import com.fastaccess.helper.PrefHelper;
import com.jaredrummler.android.device.DeviceName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AutomatronTests {

    private UiDevice mDevice;

    private static final int LAUNCH_TIMEOUT = 4000;

    @Before
    public void startMainActivity(){
        mDevice = UiDevice.getInstance(getInstrumentation());
        final String TARGET_PACKAGE =
                InstrumentationRegistry.getTargetContext().getPackageName();
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(TARGET_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(TARGET_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    public void loginIfNeed() throws UiObjectNotFoundException {
        UiObject usernameField = mDevice.findObject(new UiSelector()
                .resourceId("usernameEditText")
                .className("android.support.design.widget.TextInputEditText"));

        usernameField.setText("TestSampleFotTin");

        UiObject passwordField = mDevice.findObject(new UiSelector()
                .resourceId("passwordEditText")
                .className("android.support.design.widget.TextInputEditText"));

        passwordField.setText("TestSample1");

    }

    @Test
    public void testTrendingMenu() throws UiObjectNotFoundException, InterruptedException {
        openMainNavMenu();

        UiObject trendingMenuItem = mDevice.findObject(new UiSelector()
                .text("Trending"));
        trendingMenuItem.click();

        mDevice.waitForWindowUpdate(InstrumentationRegistry.getTargetContext().getPackageName(), 2000);

        String title = mDevice.findObject(By.res("com.fastaccess.github.debug:id/toolbar"))
                .findObject(By.clazz("android.widget.TextView"))
                .getText();
        assertEquals("Trending", title);
    }

    @Test
    public void testThemeChange() throws Throwable {
        openMainNavMenu();

        UiObject settingsMenuItem = mDevice.findObject(new UiSelector()
                .text("Settings"));
        settingsMenuItem.click();

        UiObject themeMenuItem = mDevice.findObject(new UiSelector()
                .text("Theme"));
        themeMenuItem.click();

        UiObject pager = mDevice.findObject(new UiSelector()
                .className(android.support.v4.view.ViewPager.class));
        pager.swipeLeft(2);
        pager.swipeRight(3);

        mDevice.waitForWindowUpdate(InstrumentationRegistry.getTargetContext().getPackageName(), 2000);

        UiObject applyButton = mDevice.findObject(new UiSelector()
                .resourceId("com.fastaccess.github.debug:id/apply"));
        applyButton.click();

        mDevice.waitForWindowUpdate(InstrumentationRegistry.getTargetContext().getPackageName(), 2000);

        assertEquals("Dark Theme",PrefHelper.getString("appTheme"));
    }

    @Test
    public void testRestorePurchase() throws Throwable {
        openMainNavMenu();

        UiObject settingsMenuItem = mDevice.findObject(new UiSelector()
                .text("Settings"));
        settingsMenuItem.swipeUp(2);

        UiObject purchaseRestoreMenuItem = mDevice.findObject(new UiSelector()
                .text("Restore Purchases"));
        purchaseRestoreMenuItem.click();

        Activity activity = getCurrentActivity();
        /*assertEquals("CheckPurchaseActivity", activity.getClass().getSimpleName());*/
    }

    @Test
    public void testSendFeedback() throws Throwable {
        openMainNavMenu();

        UiObject settingsMenuItem = mDevice.findObject(new UiSelector()
                .text("Send feedback"));
        settingsMenuItem.click();

        UiObject okDebugButton = mDevice.findObject(new UiSelector()
                .resourceId("android:id/button1"));
        if(okDebugButton != null && okDebugButton.exists()){
            okDebugButton.click();
        }

        UiObject inputText = mDevice.findObject(new UiSelector()
                .text("Title"))
                .getChild(new UiSelector()
                        .className("android.widget.EditText"));
        inputText.setText("Hello");

        UiObject inputDescription = mDevice.findObject(new UiSelector()
                .text("Description"));
        inputDescription.click();

        UiObject fullDesc = mDevice.findObject(new UiSelector()
                .resourceId("com.fastaccess.github.debug:id/editText"));

        UiObject submitButton = mDevice.findObject(new UiSelector()
                .resourceId("com.fastaccess.github.debug:id/submit"));
        submitButton.click();

        UiObject mainSubmitButton = mDevice.findObject(new UiSelector()
                .resourceId("com.fastaccess.github.debug:id/submit"));
        mainSubmitButton.click();

        assertTrue(fullDesc.getText().contains(DeviceName.getDeviceName()));

        UiObject toast = mDevice.findObject(new UiSelector()
                .className(android.widget.Toast.class));
        assertEquals("Message was sent", toast.getText());
    }

    @Test
    public void testChangeLogText() throws Throwable {
        openMainNavMenu();

        UiObject settingsMenuItem = mDevice.findObject(new UiSelector()
                .text("Send feedback"));
        settingsMenuItem.swipeUp(2);

        UiObject aboutMenuItem = mDevice.findObject(new UiSelector()
                .text("About"));
        aboutMenuItem.click();

        List<UiObject2> layouts = mDevice.findObjects(By
               .clazz("android.widget.LinearLayout")).stream().filter(layout ->
                layout.findObject(By.res("com.fastaccess.github.debug:id/mal_list_card_title")).getText().equals("About"))
                .collect(Collectors.toList());

        UiObject2 aboutLay = layouts.get(0);
        UiObject2 secondLayoutLine = aboutLay.getChildren().get(1).getChildren().get(1);
        String textOfLine = secondLayoutLine
                .findObject(By.res("com.fastaccess.github.debug:id/mal_item_text"))
                .getText();
        assertEquals("Changelog", textOfLine);
    }

    private void openMainNavMenu() throws UiObjectNotFoundException {
        UiObject mainNavMenu = mDevice.findObject(new UiSelector()
                .className(android.widget.ImageButton.class)
                .descriptionContains("Navigate up")
        );
        mainNavMenu.click();
    }

    private Activity getCurrentActivity() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                java.util.Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                activity[0] = Iterables.getOnlyElement(activities);
            }});
        return activity[0];
    }

    public boolean isDarkThemeSelected() {

        return "Dark Theme".equals(PrefHelper.getString("appTheme"));
    }
}
