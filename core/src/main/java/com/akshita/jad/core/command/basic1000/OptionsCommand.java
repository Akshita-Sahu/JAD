package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.core.GlobalOptions;
import com.akshita.jad.core.Option;
import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.command.model.ChangeResultVO;
import com.akshita.jad.core.command.model.OptionVO;
import com.akshita.jad.core.command.model.OptionsModel;
import com.akshita.jad.core.shell.cli.CliToken;
import com.akshita.jad.core.shell.cli.Completion;
import com.akshita.jad.core.shell.cli.CompletionUtils;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.command.ExitStatus;
import com.akshita.jad.core.util.CommandUtils;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.TokenUtils;
import com.akshita.jad.core.util.matcher.EqualsMatcher;
import com.akshita.jad.core.util.matcher.Matcher;
import com.akshita.jad.core.util.matcher.RegexMatcher;
import com.akshita.jad.core.util.reflect.FieldUtils;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.akshita.jad.core.util.JADCheckUtils.isIn;
import static java.lang.String.format;

/**
 * 
 *
 * @author vlinux on 15/6/6.
 */
// @formatter:off
@Name("options")
@Summary("View and change various JAD options")
@Description(Constants.EXAMPLE +
        "options       # list all options\n" +
        "options json-format true\n" +
        "options dump true\n" +
        "options unsafe true\n" +
        Constants.WIKI + Constants.WIKI_HOME + "options")
//@formatter:on
public class OptionsCommand extends AnnotatedCommand {

    private static final Logger logger = LoggerFactory.getLogger(OptionsCommand.class);

    private String optionName;
    private String optionValue;

    @Argument(index = 0, argName = "options-name", required = false)
    @Description("Option name")
    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    @Argument(index = 1, argName = "options-value", required = false)
    @Description("Option value")
    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public void process(CommandProcess process) {
        try {
            ExitStatus status = null;
            if (isShow()) {
                status = processShow(process);
            } else if (isShowName()) {
                status = processShowName(process);
            } else {
                status = processChangeNameValue(process);
            }

            CommandUtils.end(process, status);
        } catch (Throwable t) {
            logger.error("processing error", t);
            process.end(-1, "processing error");
        }
    }

    /**
     * complete first argument(options-name), other case use default complete
     *
     * @param completion the completion object
     */
    @Override
    public void complete(Completion completion) {
        int argumentIndex = CompletionUtils.detectArgumentIndex(completion);
        List<CliToken> lineTokens = completion.lineTokens();
        if (argumentIndex == 1) {
            String laseToken = TokenUtils.getLast(lineTokens).value().trim();
            //prefix match options-name
            String pattern = "^" + laseToken + ".*";
            Collection<String> optionNames = findOptionNames(new RegexMatcher(pattern));
            CompletionUtils.complete(completion, optionNames);
        } else {
            super.complete(completion);
        }
    }

    private ExitStatus processShow(CommandProcess process) throws IllegalAccessException {
        Collection<Field> fields = findOptionFields(new RegexMatcher(".*"));
        process.appendResult(new OptionsModel(convertToOptionVOs(fields)));
        return ExitStatus.success();
    }

    private ExitStatus processShowName(CommandProcess process) throws IllegalAccessException {
        Collection<Field> fields = findOptionFields(new EqualsMatcher<String>(optionName));
        process.appendResult(new OptionsModel(convertToOptionVOs(fields)));
        return ExitStatus.success();
    }

    private ExitStatus processChangeNameValue(CommandProcess process) throws IllegalAccessException {
        Collection<Field> fields = findOptionFields(new EqualsMatcher<String>(optionName));

        // name not exists
        if (fields.isEmpty()) {
            return ExitStatus.failure(-1, format("options[%s] not found.", optionName));
        }

        Field field = fields.iterator().next();
        Option optionAnnotation = field.getAnnotation(Option.class);
        Class<?> type = field.getType();
        Object beforeValue = FieldUtils.readStaticField(field);
        Object afterValue;

        try {
            // try to case string to type
            if (isIn(type, int.class, Integer.class)) {
                afterValue = Integer.valueOf(optionValue);
            } else if (isIn(type, long.class, Long.class)) {
                afterValue = Long.valueOf(optionValue);
            } else if (isIn(type, boolean.class, Boolean.class)) {
                afterValue = Boolean.valueOf(optionValue);
            } else if (isIn(type, double.class, Double.class)) {
                afterValue = Double.valueOf(optionValue);
            } else if (isIn(type, float.class, Float.class)) {
                afterValue = Float.valueOf(optionValue);
            } else if (isIn(type, byte.class, Byte.class)) {
                afterValue = Byte.valueOf(optionValue);
            } else if (isIn(type, short.class, Short.class)) {
                afterValue = Short.valueOf(optionValue);
            } else if (isIn(type, short.class, String.class)) {
                afterValue = optionValue;
            } else {
                return ExitStatus.failure(-1, format("Options[%s] type[%s] was unsupported.", optionName, type.getSimpleName()));
            }
        } catch (Throwable t) {
            return ExitStatus.failure(-1, format("Cannot cast option value[%s] to type[%s].", optionValue, type.getSimpleName()));
        }

        String validateError = validateOptionValue(optionAnnotation.name(), afterValue);
        if (validateError != null) {
            return ExitStatus.failure(-1, validateError);
        }

        FieldUtils.writeStaticField(field, afterValue);

        // FIXME hack for ongl strict
        if (field.getName().equals("strict")) {
            GlobalOptions.updateOnglStrict(Boolean.valueOf(optionValue));
            logger.info("update ongl strict to: {}", optionValue);
        }

        ChangeResultVO changeResultVO = new ChangeResultVO(optionAnnotation.name(), beforeValue, afterValue);
        process.appendResult(new OptionsModel(changeResultVO));
        return ExitStatus.success();
    }

    static String validateOptionValue(String optionName, Object optionValue) {
        if ("object-size-limit".equals(optionName)
                && optionValue instanceof Integer
                && ((Integer) optionValue).intValue() <= 0) {
            return "options[object-size-limit] must be greater than 0.";
        }
        return null;
    }


    /**
     * options
     */
    private boolean isShow() {
        return StringUtils.isBlank(optionName) && StringUtils.isBlank(optionValue);
    }


    /**
     * Name
     */
    private boolean isShowName() {
        return !StringUtils.isBlank(optionName) && StringUtils.isBlank(optionValue);
    }

    private Collection<Field> findOptionFields(Matcher<String> optionNameMatcher) {
        final Collection<Field> matchFields = new ArrayList<Field>();
        for (final Field optionField : FieldUtils.getAllFields(GlobalOptions.class)) {
            if (isMatchOptionAnnotation(optionField, optionNameMatcher)) {
                matchFields.add(optionField);
            }
        }
        return matchFields;
    }

    private Collection<String> findOptionNames(Matcher<String> optionNameMatcher) {
        final Collection<String> matchOptionNames = new ArrayList<String>();
        for (final Field optionField : FieldUtils.getAllFields(GlobalOptions.class)) {
            if (isMatchOptionAnnotation(optionField, optionNameMatcher)) {
                final Option optionAnnotation = optionField.getAnnotation(Option.class);
                matchOptionNames.add(optionAnnotation.name());
            }
        }
        return matchOptionNames;
    }

    private boolean isMatchOptionAnnotation(Field optionField, Matcher<String> optionNameMatcher) {
        if (!optionField.isAnnotationPresent(Option.class)) {
            return false;
        }
        final Option optionAnnotation = optionField.getAnnotation(Option.class);
        return optionAnnotation != null && optionNameMatcher.matching(optionAnnotation.name());
    }

    private List<OptionVO> convertToOptionVOs(Collection<Field> fields) throws IllegalAccessException {
        List<OptionVO> list = new ArrayList<OptionVO>();
        for (Field field : fields) {
            list.add(convertToOptionVO(field));
        }
        return list;
    }

    private OptionVO convertToOptionVO(Field optionField) throws IllegalAccessException {
        Option optionAnnotation = optionField.getAnnotation(Option.class);
        OptionVO optionVO = new OptionVO();
        optionVO.setLevel(optionAnnotation.level());
        optionVO.setName(optionAnnotation.name());
        optionVO.setSummary(optionAnnotation.summary());
        optionVO.setDescription(optionAnnotation.description());
        optionVO.setType(optionField.getType().getSimpleName());
        optionVO.setValue(""+optionField.get(null));
        return optionVO;
    }

}
