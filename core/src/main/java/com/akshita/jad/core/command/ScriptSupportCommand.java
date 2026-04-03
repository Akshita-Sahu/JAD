package com.akshita.jad.core.command;

import com.akshita.jad.core.advisor.Advice;

/**
 * 
 * Created by vlinux on 15/6/1.
 */
public interface ScriptSupportCommand {

    /**
     * 
     */
    interface ScriptListener {

        /**
         * 
         *
         * @param output 
         */
        void create(Output output);

        /**
         * 
         *
         * @param output 
         */
        void destroy(Output output);

        /**
         * 
         *
         * @param output 
         * @param advice 
         */
        void before(Output output, Advice advice);

        /**
         * 
         *
         * @param output 
         * @param advice 
         */
        void afterReturning(Output output, Advice advice);

        /**
         * 
         *
         * @param output 
         * @param advice 
         */
        void afterThrowing(Output output, Advice advice);

    }

    /**
     * 
     */
    class ScriptListenerAdapter implements ScriptListener {

        @Override
        public void create(Output output) {

        }

        @Override
        public void destroy(Output output) {

        }

        @Override
        public void before(Output output, Advice advice) {

        }

        @Override
        public void afterReturning(Output output, Advice advice) {

        }

        @Override
        public void afterThrowing(Output output, Advice advice) {

        }
    }


    /**
     * 
     */
    interface Output {

        /**
         * ()
         *
         * @param string 
         * @return this
         */
        Output print(String string);

        /**
         * ()
         *
         * @param string 
         * @return this
         */
        Output println(String string);

        /**
         * 
         *
         * @return this
         */
        Output finish();

    }

}
