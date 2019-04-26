package ru.tinkoff.fintech.mobile.matcher;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;

import com.fastaccess.ui.adapter.viewholder.TrendingViewHolder;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

final public class TrendingRecyclerViewMatchers {

    public static Matcher<RecyclerView.ViewHolder> withItemTitle(final Matcher<String> matcher) {
        return new BoundedMatcher<RecyclerView.ViewHolder, TrendingViewHolder>(
                TrendingViewHolder.class
        ) {
            @Override
            protected boolean matchesSafely(TrendingViewHolder holder) {
                return matcher.matches(holder.title.getText());
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(matcher);
            }
        };
    }

}
