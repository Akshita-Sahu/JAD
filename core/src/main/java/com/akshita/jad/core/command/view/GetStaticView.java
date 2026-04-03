package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.GetStaticModel;
import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.ClassUtils;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.view.ObjectView;
import com.akshita_sahu.text.ui.Element;
import com.akshita_sahu.text.util.RenderUtil;

/**
 * @author gongdewei 2020/4/20
 */
public class GetStaticView extends ResultView<GetStaticModel> {

    @Override
    public void draw(CommandProcess process, GetStaticModel result) {
        if (result.getMatchedClassLoaders() != null) {
            process.write("Matched classloaders: \n");
            ClassLoaderView.drawClassLoaders(process, result.getMatchedClassLoaders(), false);
            process.write("\n");
            return;
        }
        if (result.getField() != null) {
            ObjectVO field = result.getField();
            String valueStr = StringUtils.objectToString(field.needExpand() ? new ObjectView(field).draw() : field.getObject());
            process.write("field: " + result.getFieldName() + "\n" + valueStr + "\n");
        } else if (result.getMatchedClasses() != null) {
            Element table = ClassUtils.renderMatchedClasses(result.getMatchedClasses());
            process.write(RenderUtil.render(table)).write("\n");
        }
    }
}
