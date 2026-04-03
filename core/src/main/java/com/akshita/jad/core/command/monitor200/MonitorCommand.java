package com.akshita.jad.core.command.monitor200;


import com.akshita.jad.core.GlobalOptions;
import com.akshita.jad.core.advisor.AdviceListener;
import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.util.SearchUtils;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.matcher.Matcher;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Option;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * <br/>
 * @author vlinux
 */
@Name("monitor")
@Summary("Monitor method execution statistics, e.g. total/success/failure count, average rt, fail rate, etc. ")
@Description("\nExamples:\n" +
        "  monitor org.apache.commons.lang.StringUtils isBlank\n" +
        "  monitor org.apache.commons.lang.StringUtils isBlank -c 5\n" +
        "  monitor org.apache.commons.lang.StringUtils isBlank params[0]!=null\n" +
        "  monitor -b org.apache.commons.lang.StringUtils isBlank params[0]!=null\n" +
        "  monitor -E org\\.apache\\.commons\\.lang\\.StringUtils isBlank\n" +
        Constants.WIKI + Constants.WIKI_HOME + "monitor")
public class MonitorCommand extends EnhancerCommand {

    private String classPattern;
    private String methodPattern;
    private String conditionExpress;
    private int cycle = 60;
    private boolean isRegEx = false;
    private int numberOfLimit = 100;
    private boolean isBefore = false;

    @Argument(argName = "class-pattern", index = 0)
    @Description("Path and classname of Pattern Matching")
    public void setClassPattern(String classPattern) {
        this.classPattern = StringUtils.normalizeClassName(classPattern);
    }

    @Argument(argName = "method-pattern", index = 1)
    @Description("Method of Pattern Matching")
    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
    }

    @Argument(argName = "condition-express", index = 2, required = false)
    @Description(Constants.CONDITION_EXPRESS)
    public void setConditionExpress(String conditionExpress) {
        this.conditionExpress = conditionExpress;
    }

    @Option(shortName = "c", longName = "cycle")
    @Description("The monitor interval (in seconds), 60 seconds by default")
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    @Option(shortName = "E", longName = "regex", flag = true)
    @Description("Enable regular expression to match (wildcard matching by default)")
    public void setRegEx(boolean regEx) {
        isRegEx = regEx;
    }

    @Option(shortName = "n", longName = "limits")
    @Description("Threshold of execution times")
    public void setNumberOfLimit(int numberOfLimit) {
        this.numberOfLimit = numberOfLimit;
    }

    @Option(shortName = "b", longName = "before", flag = true)
    @Description("Evaluate the condition-express before method invoke")
    public void setBefore(boolean before) {
        isBefore = before;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public String getConditionExpress() {
        return conditionExpress;
    }

    public int getCycle() {
        return cycle;
    }

    public boolean isRegEx() {
        return isRegEx;
    }

    public int getNumberOfLimit() {
        return numberOfLimit;
    }

    public boolean isBefore() {
        return isBefore;
    }

    @Override
    protected Matcher getClassNameMatcher() {
        if (classNameMatcher == null) {
            classNameMatcher = SearchUtils.classNameMatcher(getClassPattern(), isRegEx());
        }
        return classNameMatcher;
    }

    @Override
    protected Matcher getClassNameExcludeMatcher() {
        if (classNameExcludeMatcher == null && getExcludeClassPattern() != null) {
            classNameExcludeMatcher = SearchUtils.classNameMatcher(getExcludeClassPattern(), isRegEx());
        }
        return classNameExcludeMatcher;
    }

    @Override
    protected Matcher getMethodNameMatcher() {
        if (methodNameMatcher == null) {
            methodNameMatcher = SearchUtils.classNameMatcher(getMethodPattern(), isRegEx());
        }
        return methodNameMatcher;
    }

    @Override
    protected AdviceListener getAdviceListener(CommandProcess process) {
        final AdviceListener listener = new MonitorAdviceListener(this, process, GlobalOptions.verbose || this.verbose);
        /*
         * handle，suspendtimer，resumetimer
         */
        process.suspendHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                listener.destroy();
            }
        });
        process.resumeHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                listener.create();
            }
        });
        return listener;
    }
}
