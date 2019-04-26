package ru.tinkoff.fintech.mobile.elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.fastaccess.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Objects;

import ru.tinkoff.fintech.mobile.pages.MainPage;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

final public class DrawerElement {

    public DrawerElement open() {
        onView(allOf(withParent(withId(R.id.toolbar)), isAssignableFrom(ImageButton.class)))
                .perform(click()); // TODO мы же не ищем легких путей 8-)
        return this;
    }

    public MainPage close() {
        onView(withId(R.id.drawer)).perform(DrawerActions.close()); // TODO или ищем)))
        return new MainPage();
    }

    public DrawerElement assertUsername(Matcher<String> matcher) {
        onView(withId(R.id.navUsername)).check(ViewAssertions.matches(withText(matcher)));
        return this;
    }

    public DrawerElement assertMenuItem(
            final int position,
            final @StringRes int stringRes,
            final @DrawableRes int iconRes
    ) {
        onView(allOf(isAssignableFrom(NavigationView.class), withId(R.id.mainNav)))
                .check(ViewAssertions.matches(withTextAndIcon(position, stringRes, iconRes)));
        return this;
    }

    private BoundedMatcher<View, NavigationView> withTextAndIcon(
            final int position,
            final @StringRes int stringRes,
            final @DrawableRes int iconRes
    ) {
        return new BoundedMatcher<View, NavigationView>(NavigationView.class) {
            @Override
            protected boolean matchesSafely(NavigationView view) {
                MenuItem item = view.getMenu().getItem(position);
                String actualTitle = item.getTitle().toString();
                Bitmap actualIcon = getBitmap(item.getIcon());
                Context targetContext = view.getContext();
                String expectedTitle = targetContext.getResources().getString(stringRes);
                Drawable expectedDrawable = VectorDrawableCompat.create(
                        targetContext.getResources(),
                        iconRes,
                        targetContext.getTheme()
                );
                Bitmap expectedIcon = getBitmap(Objects.requireNonNull(expectedDrawable));
                return actualIcon.sameAs(expectedIcon) && actualTitle.equals(expectedTitle);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("mismatched text and icon");
            }


            private Bitmap getBitmap(final Drawable drawable) {
                final Bitmap bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888
                );
                final Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        };
    }

}
