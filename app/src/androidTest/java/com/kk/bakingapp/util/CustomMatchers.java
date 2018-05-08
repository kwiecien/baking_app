package com.kk.bakingapp.util;

import android.content.res.Resources;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {

    public static class CollapsingToolbarLayoutMatcher {
        public static Matcher<Object> withCollapsingToolbarLayoutTitle(
                final Matcher<CharSequence> textMatcher) {
            return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
                @Override
                public boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                    return textMatcher.matches(toolbar.getTitle());
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("with toolbar title: ");
                    textMatcher.describeTo(description);
                }
            };
        }
    }

    /**
     * Created by dannyroa on 5/10/15.
     */
    public static class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
            return new RecyclerViewMatcher(recyclerViewId);
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                @Override
                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (resources != null) {
                        try {
                            idDescription = resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)", recyclerViewId);
                        }
                    }
                    description.appendText("with id: " + idDescription);
                }

                @Override
                public boolean matchesSafely(View view) {
                    resources = view.getResources();
                    if (childView == null) {
                        RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                        } else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
    }
}
