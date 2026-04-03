package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.OptionVO;
import com.akshita.jad.core.command.model.OptionsModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.text.Decoration;
import com.akshita_sahu.text.ui.Element;
import com.akshita_sahu.text.ui.TableElement;
import com.akshita_sahu.text.util.RenderUtil;

import java.util.Collection;

import static com.akshita_sahu.text.ui.Element.label;

/**
 * @author gongdewei 2020/4/15
 */
public class OptionsView extends ResultView<OptionsModel> {
    @Override
    public void draw(CommandProcess process, OptionsModel result) {
        if (result.getOptions() != null) {
            process.write(RenderUtil.render(drawShowTable(result.getOptions()), process.width()));
        } else if (result.getChangeResult() != null) {
            TableElement table = ViewRenderUtil.renderChangeResult(result.getChangeResult());
            process.write(RenderUtil.render(table, process.width()));
        }
    }

    private Element drawShowTable(Collection<OptionVO> options) {
        TableElement table = new TableElement(1, 1, 2, 1, 3, 6)
                .leftCellPadding(1).rightCellPadding(1);
        table.row(true, label("LEVEL").style(Decoration.bold.bold()),
                label("TYPE").style(Decoration.bold.bold()),
                label("NAME").style(Decoration.bold.bold()),
                label("VALUE").style(Decoration.bold.bold()),
                label("SUMMARY").style(Decoration.bold.bold()),
                label("DESCRIPTION").style(Decoration.bold.bold()));

        for (final OptionVO optionVO : options) {
            table.row("" + optionVO.getLevel(),
                    optionVO.getType(),
                    optionVO.getName(),
                    optionVO.getValue(),
                    optionVO.getSummary(),
                    optionVO.getDescription());
        }
        return table;
    }

}
