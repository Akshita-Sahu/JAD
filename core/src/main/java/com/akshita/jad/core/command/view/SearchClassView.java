package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.FieldVO;
import com.akshita.jad.core.command.model.SearchClassModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.ClassUtils;
import com.akshita_sahu.text.util.RenderUtil;

/**
 * @author gongdewei 2020/4/8
 */
public class SearchClassView extends ResultView<SearchClassModel> {
    @Override
    public void draw(CommandProcess process, SearchClassModel result) {
        if (result.getMatchedClassLoaders() != null) {
            process.write("Matched classloaders: \n");
            ClassLoaderView.drawClassLoaders(process, result.getMatchedClassLoaders(), false);
            process.write("\n");
            return;
        }

        if (result.isDetailed()) {
            process.write(RenderUtil.render(ClassUtils.renderClassInfo(result.getClassInfo(),
                    result.isWithField()), process.width()));
            process.write("\n");
        } else if (result.getClassNames() != null) {
            for (String className : result.getClassNames()) {
                process.write(className).write("\n");
            }
        }
    }

}
