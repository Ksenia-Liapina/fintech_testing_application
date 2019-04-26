package ru.tinkoff.fintech.mobile;

import com.fastaccess.R;
import com.fastaccess.ui.modules.trending.TrendingActivity;

import org.junit.Test;

import ru.tinkoff.fintech.mobile.base.ConcreteApplicationTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.tinkoff.fintech.mobile.matcher.TrendingRecyclerViewMatchers.withItemTitle;

public class TrendingActivityTest extends ConcreteApplicationTest<TrendingActivity> {

    private static final String TITLE = "flutter";

    public TrendingActivityTest() {
        super(TrendingActivity.class);
    }

    @Test
    public void recyclerScroll() throws InterruptedException {
        // TODO (17) затустим нужный нам экран
        TrendingActivity activity = launch();
        // TODO(18) и получаем доступ с активности
        assertThat("Вход в приложение не был выполнен", activity.isLoggedIn(), is(equalTo(true)));
        // TODO (19) обсудить обходные пути, получше чем sleep
        Thread.sleep(5_000);
        // TODO (20) выклюкчить анимации, обсудить анимации
        onView(withId(R.id.recycler)).perform(scrollToHolder(withItemTitle(containsString(TITLE))));
    }

}
