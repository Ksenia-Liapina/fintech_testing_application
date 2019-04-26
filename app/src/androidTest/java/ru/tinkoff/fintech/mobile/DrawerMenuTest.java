package ru.tinkoff.fintech.mobile;

import android.app.Activity;
import android.support.test.espresso.intent.Intents;

import com.fastaccess.R;
import com.fastaccess.ui.modules.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.tinkoff.fintech.mobile.base.ConcreteApplicationTest;
import ru.tinkoff.fintech.mobile.pages.MainPage;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class DrawerMenuTest extends ConcreteApplicationTest<MainActivity> {

    private Activity activity;

    public DrawerMenuTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        Intents.init();
        activity = launch();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void openDrawer_checkNoBugs() {
        new MainPage()
                .getDrawer() // TODO (21) page element паттерн
                .open()  // TODO (22) откроем меню
                .assertUsername(anyOf(not(equalTo("Bug")), not(equalTo("Heisen_Bug"))))// TODO (23) проверим
                .assertMenuItem(1, R.string.home, R.drawable.ic_home) // TODO (24) и еще проверим
                .close() // TODO (25) закроем
                .validate(activity);
    }

}