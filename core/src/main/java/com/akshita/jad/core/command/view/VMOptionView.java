package com.akshita.jad.core.command.view;

import com.sun.management.VMOption;
import com.akshita.jad.core.command.model.VMOptionModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.text.Decoration;
import com.akshita_sahu.text.ui.TableElement;
import com.akshita_sahu.text.util.RenderUtil;

import java.util.List;

import static com.akshita_sahu.text.ui.Element.label;

/**
 * @author gongdewei 2020/4/15
 */
public class VMOptionView extends ResultView<VMOptionModel> {

    @Override
    public void draw(CommandProcess process, VMOptionModel result) {
        if (result.getVmOptions() != null) {
            process.write(renderVMOptions(result.getVmOptions(), process.width()));
        } else if (result.getChangeResult() != null) {
            TableElement table = ViewRenderUtil.renderChangeResult(result.getChangeResult());
            process.write(RenderUtil.render(table, process.width()));
        }
    }

    private static String renderVMOptions(List<VMOption> diagnosticOptions, int width) {
        TableElement table = new TableElement(1, 1, 1, 1).leftCellPadding(1).rightCellPadding(1);
        table.row(true, label("KEY").style(Decoration.bold.bold()),
                label("VALUE").style(Decoration.bold.bold()),
                label("ORIGIN").style(Decoration.bold.bold()),
                label("WRITEABLE").style(Decoration.bold.bold()));

        for (VMOption option : diagnosticOptions) {
            table.row(option.getName(), option.getValue(), "" + option.getOrigin(), "" + option.isWriteable());
        }

        return RenderUtil.render(table, width);
    }
}
