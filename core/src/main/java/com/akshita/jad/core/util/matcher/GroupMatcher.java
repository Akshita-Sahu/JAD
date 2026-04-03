package com.akshita.jad.core.util.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author ralf0131 2017-01-06 13:29.
 */
public interface GroupMatcher<T> extends Matcher<T> {

    /**
     * 
     *
     * @param matcher 
     */
    void add(Matcher<T> matcher);

    /**
     * 
     *
     * @param <T> 
     */
    class And<T> implements GroupMatcher<T> {

        private final Collection<Matcher<T>> matchers;

        /**
         * <br/>
         * 
         *
         * @param matchers 
         */
        public And(Matcher<T>... matchers) {
            this.matchers = Arrays.asList(matchers);
        }

        @Override
        public boolean matching(T target) {
            for (Matcher<T> matcher : matchers) {
                if (!matcher.matching(target)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void add(Matcher<T> matcher) {
            matchers.add(matcher);
        }
    }

    /**
     * 
     *
     * @param <T> 
     */
    class Or<T> implements GroupMatcher<T> {

        private final Collection<Matcher<T>> matchers;

        public Or() {
            this.matchers = new ArrayList<Matcher<T>>();
        }

        /**
         * <br/>
         * 
         *
         * @param matchers 
         */
        public Or(Matcher<T>... matchers) {
            this.matchers = Arrays.asList(matchers);
        }

        public Or(Collection<Matcher<T>> matchers) {
            this.matchers = matchers;
        }

        @Override
        public boolean matching(T target) {
            for (Matcher<T> matcher : matchers) {
                if (matcher.matching(target)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void add(Matcher<T> matcher) {
            matchers.add(matcher);
        }
    }

}
