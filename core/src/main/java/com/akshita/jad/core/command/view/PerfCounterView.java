package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.PerfCounterModel;
import com.akshita.jad.core.command.model.PerfCounterVO;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.text.Decoration;
import com.akshita_sahu.text.ui.TableElement;
import com.akshita_sahu.text.util.RenderUtil;

import java.util.List;

import static com.akshita_sahu.text.ui.Element.label;

/**
 * View of 'perfcounter' command
 *
 * @author gongdewei 2020/4/27
 */
public class PerfCounterView extends ResultView<PerfCounterModel> {
    @Override
    public void draw(CommandProcess process, PerfCounterModel result) {
        List<PerfCounterVO> perfCounters = result.getPerfCounters();
        boolean details = result.isDetails();
        TableElement table;
        if (details) {
            table = new TableElement(3, 1, 1, 10).leftCellPadding(1).rightCellPadding(1);
            table.row(true, label("Name").style(Decoration.bold.bold()),
                    label("Variability").style(Decoration.bold.bold()),
                    label("Units").style(Decoration.bold.bold()), label("Value").style(Decoration.bold.bold()));
        } else {
            table = new TableElement(4, 6).leftCellPadding(1).rightCellPadding(1);
            table.row(true, label("Name").style(Decoration.bold.bold()),
                    label("Value").style(Decoration.bold.bold()));
        }

        for (PerfCounterVO counter : perfCounters) {
            if (details) {
                table.row(counter.getName(), counter.getVariability(),
                        counter.getUnits(), String.valueOf(counter.getValue()));
            } else {
                table.row(counter.getName(), String.valueOf(counter.getValue()));
            }
        }
        process.write(RenderUtil.render(table, process.width()));
    }
}
