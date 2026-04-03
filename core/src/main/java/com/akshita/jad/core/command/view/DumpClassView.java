package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.DumpClassModel;
import com.akshita.jad.core.command.model.DumpClassVO;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.ClassUtils;
import com.akshita.jad.core.util.TypeRenderUtils;
import com.akshita_sahu.text.Color;
import com.akshita_sahu.text.Decoration;
import com.akshita_sahu.text.ui.Element;
import com.akshita_sahu.text.ui.LabelElement;
import com.akshita_sahu.text.ui.TableElement;
import com.akshita_sahu.text.util.RenderUtil;

import java.util.List;

import static com.akshita_sahu.text.ui.Element.label;

/**
 * @author gongdewei 2020/4/21
 */
public class DumpClassView extends ResultView<DumpClassModel> {

    @Override
    public void draw(CommandProcess process, DumpClassModel result) {
        if (result.getMatchedClassLoaders() != null) {
            process.write("Matched classloaders: \n");
            ClassLoaderView.drawClassLoaders(process, result.getMatchedClassLoaders(), false);
            process.write("\n");
            return;
        }
        if (result.getDumpedClasses() != null) {
            drawDumpedClasses(process, result.getDumpedClasses());

        } else if (result.getMatchedClasses() != null) {
            Element table = ClassUtils.renderMatchedClasses(result.getMatchedClasses());
            process.write(RenderUtil.render(table)).write("\n");
        }
    }

    private void drawDumpedClasses(CommandProcess process, List<DumpClassVO> classVOs) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(new LabelElement("HASHCODE").style(Decoration.bold.bold()),
                new LabelElement("CLASSLOADER").style(Decoration.bold.bold()),
                new LabelElement("LOCATION").style(Decoration.bold.bold()));

        for (DumpClassVO clazz : classVOs) {
            table.row(label(clazz.getClassLoaderHash()).style(Decoration.bold.fg(Color.red)),
                    TypeRenderUtils.drawClassLoader(clazz),
                    label(clazz.getLocation()).style(Decoration.bold.fg(Color.red)));
        }

        process.write(RenderUtil.render(table, process.width()))
                .write(com.akshita.jad.core.util.Constants.EMPTY_STRING);
    }

}
